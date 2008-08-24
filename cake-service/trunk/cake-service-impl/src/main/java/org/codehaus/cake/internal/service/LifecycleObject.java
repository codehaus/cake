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
import org.codehaus.cake.service.Disposable;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.Stoppable;

class LifecycleObject {

    private final InternalExceptionService<?> ies;
    private final Object o;
    private final RunState state;
    private final Class<?> serviceFactoryKey;

    LifecycleObject(RunState state, InternalExceptionService<?> ies, Object service) {
        this.ies = ies;
        if (service instanceof ServiceList.Factory) {
            ServiceList.Factory sf = (Factory) service;
            o = sf.getFactory();
            serviceFactoryKey = sf.getKey();
        } else {
            this.o = service;
            serviceFactoryKey = null;
        }
        this.state = state;
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
        return hasAnnotation(Stoppable.class) || hasAnnotation(Disposable.class);
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
                state.trySetStartupException(e);
                throw e;
            }

            Object oo = null;
            try {
                oo = mpc.getComponentInstanceOfType(type);
            } catch (AmbiguousComponentResolutionException ee) {
                RuntimeException e = new IllegalStateException("Method " + m + "." + ee.getMessage());
                state.trySetStartupException(e);
                throw e;
            }
            if (oo == null) {
                final RuntimeException e;
                if (start) {
                    if (Container.class.isAssignableFrom(type)) {
                        e = new IllegalStateException("An instance of " + type.getSimpleName()
                                + " is not available while running methods annotated with @"
                                + Startable.class.getSimpleName() + ". The @" + AfterStart.class.getSimpleName()
                                + " annotation can be used instead if a " + type.getSimpleName() + " is needed."
                                + " [method = " + m + "]");
                    } else {
                        e = new IllegalStateException("An object of type " + type.getName()
                                + " is not available for methods with @" + Startable.class.getSimpleName()
                                + " [method = " + m + "]");
                    }
                } else {
                    e = new IllegalStateException("Instances of " + type.getName() + " cannot be injected [method = "
                            + m + "]");
                }
                state.trySetStartupException(e);
                throw e;
            }
            obs[i] = oo;
        }
        m.invoke(o, obs);
    }

    void runStart(Set<?> all, ContainerConfiguration<?> configuration, ServiceRegistrant registrant) {
        ArrayList<Object> al = new ArrayList<Object>();
        al.add(configuration);
        al.add(registrant);
        for (Object o : configuration.getConfigurations()) {
            al.add(o);
        }
        if (serviceFactoryKey != null) {
            if (o instanceof ServiceFactory) {
                registrant.registerFactory(serviceFactoryKey, (ServiceFactory) o);
            } else {
                registrant.registerService((Class) serviceFactoryKey, o);
            }
        }
        for (Method m : o.getClass().getMethods()) {
            boolean isInternal = m.getAnnotation(UseInternals.class) != null;
            Annotation a = m.getAnnotation(Startable.class);
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
                    state.trySetStartupException(cause);
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
                    state.trySetStartupException(e);
                    throw new IllegalStateException("Failed to start", e);
                }
            }
        }
    }

    void runAfterStart(ContainerConfiguration<?> configuration, Container container) {
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
                    state.trySetStartupException(cause);
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
                    state.trySetStartupException(e);
                    ies.error("Started of service failed [method=" + m + "]", e.getCause());
                }
            }
        }
    }

    void runStop() {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(Stoppable.class);
            if (a != null) {
                if (m.getParameterTypes().length > 0) {
                    ies.error("@" + Stoppable.class.getSimpleName()
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

    void runDispose() {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(Disposable.class);
            if (a != null) {
                if (m.getParameterTypes().length > 0) {
                    ies.error("@" + Disposable.class.getSimpleName()
                            + " annotation does not accept arguments, method will not be run [method=" + m + "]");
                } else {
                    try {
                        m.invoke(o);
                    } catch (InvocationTargetException e) {
                        ies.error("Disposal of service failed [method=" + m + "]", e.getCause()); // ies
                        // .
                        // error
                        // rethrows
                        // errors
                    } catch (IllegalAccessException e) {
                        // Should never happen because we only iterating on
                        // public methods
                        ies.error("Disposal of service failed [method=" + m + "]", e.getCause());
                    }
                }
            }
        }
    }
}
