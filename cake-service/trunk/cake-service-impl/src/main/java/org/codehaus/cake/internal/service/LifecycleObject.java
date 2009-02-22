/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.internal.picocontainer.MutablePicoContainer;
import org.codehaus.cake.internal.picocontainer.defaults.AmbiguousComponentResolutionException;
import org.codehaus.cake.internal.picocontainer.defaults.DefaultPicoContainer;
import org.codehaus.cake.internal.service.ServiceList.Factory;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.service.OnShutdown;
import org.codehaus.cake.service.OnStart;
import org.codehaus.cake.service.OnTermination;
import org.codehaus.cake.service.ServiceFactory;

class LifecycleObject {

    private final Object o;
    private final LifecycleManager parent;
    private final Class<?> serviceFactoryKey;

    LifecycleObject(LifecycleManager parent, Object service) {
        if (service instanceof ServiceList.Factory) {
            ServiceList.Factory sf = (Factory) service;
            o = sf.getFactory();
            serviceFactoryKey = sf.getKey();
        } else {
            this.o = service;
            serviceFactoryKey = null;
        }
        this.parent = parent;
    }

    private boolean hasAnnotation(Class<? extends Annotation> annotation) {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(annotation);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

    boolean isStoppableOrDisposable() {
        return hasAnnotation(OnShutdown.class) || hasAnnotation(OnTermination.class);
    }

    private void matchAndInvoke(Method m, Iterable<?> parameters, boolean start) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        for (Object o : parameters) {
            if (o != null && mpc.getComponentAdapterOfType(o.getClass()) == null) {
                mpc.registerComponentInstance(o);
            }
        }
        Object[] obs = new Object[m.getParameterTypes().length];
        for (int i = 0; i < m.getParameterTypes().length; i++) {
            Class<?> type = m.getParameterTypes()[i];
            if (type.equals(Object.class)) {
                RuntimeException e = new IllegalStateException("Cannot depend on an Object as a parameter [method ="
                        + m.toString() + "]");
                parent.trySetStartupException(e);
                throw e;
            }

            Object oo = null;
            try {
                oo = mpc.getComponentInstanceOfType(type);
            } catch (AmbiguousComponentResolutionException ee) {
                RuntimeException e = new IllegalStateException("Method " + m + "." + ee.getMessage());
                parent.trySetStartupException(e);
                throw e;
            }
            if (oo == null) {
                final RuntimeException e;
                if (start) {
                    if (Container.class.isAssignableFrom(type)) {
                        e = new IllegalStateException("An instance of " + type.getSimpleName()
                                + " is not available while running methods annotated with @"
                                + OnStart.class.getSimpleName() + ". The @" + AfterStart.class.getSimpleName()
                                + " annotation can be used instead if a " + type.getSimpleName() + " is needed."
                                + " [method = " + m + "]");
                    } else {
                        e = new IllegalStateException("An object of type " + type.getName()
                                + " is not available for methods with @" + OnStart.class.getSimpleName()
                                + " [method = " + m + "]");
                    }
                } else {
                    e = new IllegalStateException("Instances of " + type.getName() + " cannot be injected [method = "
                            + m + "]");
                }
                parent.trySetStartupException(e);
                throw e;
            }
            obs[i] = oo;
        }
        m.invoke(o, obs);
    }

    void runStart(Set<?> all, ContainerConfiguration configuration, ServiceManager registrant, InternalExceptionService<?> ies) {
        ArrayList<Object> al = new ArrayList<Object>();
        al.add(configuration);
        al.add(registrant);
        for (Object o : configuration.getConfigurations()) {
            al.add(o);
        }
        if (serviceFactoryKey != null) {
            if (o instanceof ServiceFactory) {
                registrant.registerServiceFactory(serviceFactoryKey, (ServiceFactory) o);
            } else {
                registrant.registerService((Class) serviceFactoryKey, o);
            }
        }
        ExportAsService exportedKey = o.getClass().getAnnotation(ExportAsService.class);
        if (exportedKey != null) {
            for (Class c: exportedKey.value()) {
                if (o instanceof ServiceFactory) {
                    registrant.registerServiceFactory(c, (ServiceFactory) o);
                } else {
                    registrant.registerService(c, o);
                }
            }
        }
        for (Method m : o.getClass().getMethods()) {
            boolean isInternal = m.getAnnotation(UseInternals.class) != null;
            Annotation a = m.getAnnotation(OnStart.class);
            if (a != null) {
                if (ies.isDebugEnabled()) {
                    ies.debug("@Startable (Invoking) -> " + m.getDeclaringClass().getName() + "." + m.getName() + "()");
                }
                try {
                    if (isInternal) {
                        matchAndInvoke(m, all, true);
                    } else {
                        matchAndInvoke(m, al, true);
                    }
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    parent.trySetStartupException(cause);
                    if (cause instanceof Error) {
                        throw (Error) cause;
                    }
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    }
                    throw new IllegalStateException("Failed to start", cause);
                    // ies.error("Start of service failed [service=" + o +
                    // ", type=" + o.getClass() + ", method=" + m
                    // + "]", cause);
                } catch (IllegalAccessException e) {
                    parent.trySetStartupException(e);
                    throw new IllegalStateException("Failed to start", e);
                }
            }
        }
    }

    void runAfterStart(InternalExceptionService<?> ies, ContainerConfiguration configuration, Container container) {
        ArrayList<Object> objectsAvailable = new ArrayList<Object>();
        objectsAvailable.add(container); // add container
        objectsAvailable.add(configuration); // add Base configuration
        for (Object o : configuration.getConfigurations()) {
            objectsAvailable.add(o); // add each sub configuration
        }
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(AfterStart.class);
            if (a != null) {
                if (ies.isDebugEnabled()) {
                    ies.debug("@AfterStart -> " + m.getDeclaringClass().getName() + "." + m.getName() + "()");
                }
                try {
                    matchAndInvoke(m, objectsAvailable, false);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    parent.trySetStartupException(cause);
                    if (cause instanceof Error) {
                        throw (Error) cause;
                    }
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    }
                    throw new IllegalStateException("Started failed", cause);

                    // ies.error("Started of service failed [service=" + o +
                    // ", type=" + o.getClass() + ", method=" + m
                    // + "]", cause);
                } catch (IllegalAccessException e) {
                    parent.trySetStartupException(e);
                    ies.error("Started of service failed [method=" + m + "]", e.getCause());
                }
            }
        }
    }

    void runStop(InternalExceptionService<?> ies) {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(OnShutdown.class);
            if (a != null) {
                if (m.getParameterTypes().length > 0) {
                    ies.error("@" + OnShutdown.class.getSimpleName()
                            + " annotation does not accept arguments, method will not be run [method=" + m + "]");
                } else {
                    try {
                        m.invoke(o);
                    } catch (InvocationTargetException e) {
                        ies.error("Shutdown of service failed [method=" + m + "]", e.getCause());// ies
                        // .
                        // error
                        // rethrows
                        // errors
                    } catch (IllegalAccessException e) {
                        // Should never happen because we only iterating on
                        // public methods
                        ies.error("Shutdown of service failed [method=" + m + "]", e.getCause());
                    }
                }
            }
        }
    }

    void runDispose(InternalExceptionService<?> ies) {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(OnTermination.class);
            if (a != null) {
                if (m.getParameterTypes().length > 0) {
                    ies.error("@" + OnTermination.class.getSimpleName()
                            + " annotation does not accept arguments, method will not be run [method=" + m + "]");
                } else {
                    try {
                        m.invoke(o);
                    } catch (InvocationTargetException e) {
                        // ies.error rethrows errors
                        ies.error("Disposal of service failed [method=" + m + "]", e.getCause()); 
                    } catch (IllegalAccessException e) {
                        // Should never happen because we only iterating on public methods
                        ies.error("Disposal of service failed [method=" + m + "]", e.getCause());
                    }
                }
            }
        }
    }
}
