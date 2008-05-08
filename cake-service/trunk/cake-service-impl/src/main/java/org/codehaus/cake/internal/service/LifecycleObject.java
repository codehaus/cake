package org.codehaus.cake.internal.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.codehaus.cake.internal.picocontainer.MutablePicoContainer;
import org.codehaus.cake.internal.picocontainer.defaults.DefaultPicoContainer;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Disposable;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.Stoppable;

public class LifecycleObject {

    private final Object o;
    private final InternalExceptionService ies;
    private final RunState state;

    LifecycleObject(RunState state, InternalExceptionService ies, Object o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        this.ies = ies;
        this.o = o;
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

    public boolean stopOrDisposeShouldRun() {
        return hasAnnotation(Stoppable.class) || hasAnnotation(Disposable.class);
    }

    private void matchAndInvoke(Method m, Iterable parameters) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        for (Object o : parameters) {
            mpc.registerComponentInstance(o);
        }
        Object[] obs = new Object[m.getParameterTypes().length];
        for (int i = 0; i < m.getParameterTypes().length; i++) {
            Object oo = mpc.getComponentInstanceOfType(m.getParameterTypes()[i]);
            if (oo == null) {
                RuntimeException e = new IllegalStateException("No service registered for type "
                        + m.getParameterTypes()[i]);
                state.trySetStartupException(e);
                throw e;
            }
            obs[i] = oo;
        }
        try {
            m.invoke(o, obs);
        } catch (RuntimeException e) {
            state.trySetStartupException(e);
            ies.fatal("Failed To start service", e);
            throw e;
        }
    }

    public void startRun(ContainerConfiguration configuration, ServiceRegistrant registrant) {
        ArrayList al = new ArrayList();
        al.add(configuration);
        al.add(registrant);
        for (Object o : configuration.getConfigurations()) {
            al.add(o);
        }
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(Startable.class);
            if (a != null) {
                try {
                    matchAndInvoke(m, al);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    state.trySetStartupException(cause);
                    ies.error("Start of service failed [service=" + o + ", type=" + o.getClass()
                            + ", method=" + m + "]", cause);
                    if (cause instanceof Error) {
                        throw (Error) cause;
                    }
                } catch (IllegalAccessException e) {
                    state.trySetStartupException(e);
                    ies.error("Started of service failed [service=" + o + ", type=" + o.getClass()
                            + ", method=" + m + "]", e.getCause());
                }
            }
        }
    }

    public void startedRun(Container container) {
        ArrayList al = new ArrayList();
        al.add(container);
        for (Object o : container.getAllServices().values()) {
            al.add(o);
        }
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(AfterStart.class);
            if (a != null) {
                try {
                    matchAndInvoke(m, al);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    state.trySetStartupException(cause);
                    ies.error("Started of service failed [service=" + o + ", type=" + o.getClass()
                            + ", method=" + m + "]", cause);
                    if (cause instanceof Error) {
                        throw (Error) cause;
                    }
                } catch (IllegalAccessException e) {
                    state.trySetStartupException(e);
                    ies.error("Started of service failed [service=" + o + ", type=" + o.getClass()
                            + ", method=" + m + "]", e.getCause());
                }
            }
        }
    }

    public void stopRun() {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(Stoppable.class);
            if (a != null) {
                if (m.getParameterTypes().length > 0) {
                    ies
                            .error(Stoppable.class
                                    + " annotation does not accept arguments, method will not be run [service="
                                    + o + ", type=" + o.getClass() + ", method=" + m + "]");
                } else {
                    try {
                        m.invoke(o);
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        ies.error("Disposal of service failed [service=" + o + ", type="
                                + o.getClass() + ", method=" + m + "]", cause);
                        if (cause instanceof Error) {
                            throw (Error) cause;
                        }
                    } catch (IllegalAccessException e) {
                        ies.error("Disposal of service failed [service=" + o + ", type="
                                + o.getClass() + ", method=" + m + "]", e.getCause());
                    }
                }
            }
        }
    }

    public void disposeRun() {
        for (Method m : o.getClass().getMethods()) {
            Annotation a = m.getAnnotation(Disposable.class);
            if (a != null) {
                if (m.getParameterTypes().length > 0) {
                    ies
                            .error(Disposable.class
                                    + " annotation does not accept arguments, method will not be run [service="
                                    + o + ", type=" + o.getClass() + ", method=" + m + "]");
                } else {
                    try {
                        m.invoke(o);
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        ies.error("Disposal of service failed [service=" + o + ", type="
                                + o.getClass() + ", method=" + m + "]", cause);
                        if (cause instanceof Error) {
                            throw (Error) cause;
                        }
                    } catch (IllegalAccessException e) {
                        ies.error("Disposal of service failed [service=" + o + ", type="
                                + o.getClass() + ", method=" + m + "]", e.getCause());
                    }
                }
            }
        }
    }
}