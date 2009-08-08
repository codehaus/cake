package org.codehaus.cake.internal.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.internal.picocontainer.MutablePicoContainer;
import org.codehaus.cake.internal.picocontainer.defaults.AmbiguousComponentResolutionException;
import org.codehaus.cake.internal.picocontainer.defaults.DefaultPicoContainer;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.Container.State;

public abstract class StateTransitioner {
    private Map<Method, Object[]> methods;

    private final State state;

    public StateTransitioner(State state) {
        this.state = state;
    }

    private Object getMatchingObject(Class<?> type) {
        return null;
    }

    private void runAll() {
        for (Map.Entry<Method, Object[]> e : methods.entrySet()) {
            Method m = e.getKey();
            Object[] parameters = e.getValue();

            // m.invoke(obj, args)
        }
    }

    Map<Method, Object[]> tryMatch(State state, Object target, List<?> parameters) {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        for (Object o : parameters) {
            if (o != null && mpc.getComponentAdapterOfType(o.getClass()) == null) {
                mpc.registerComponentInstance(o);
            }
        }
        HashMap<Method, Object[]> methods = new HashMap<Method, Object[]>();
        for (Method m : target.getClass().getMethods()) {
            RunAfter ra = m.getAnnotation(RunAfter.class);
            if (ra != null && contains(ra.value(), state)) {
                Object[] arguments = new Object[m.getParameterTypes().length];
                for (int i = 0; i < m.getParameterTypes().length; i++) {
                    Class<?> type = m.getParameterTypes()[i];
                    if (type.equals(State.class)) {
                        // If argument is of type (Container) State then parse along the current state
                        arguments[i] = state;
                    } else if (type.equals(Object.class)) {
                        // Cannot depend on parameters of type Object, any service could match it
                        RuntimeException e = new IllegalStateException(
                                "Cannot depend on an Object as a parameter [method =" + m.toString() + "]");
                        // parent.trySetStartupException(e);
                        throw e;
                    } else {
                        Object oo = null;
                        try {
                            oo = mpc.getComponentInstanceOfType(type);
                        } catch (AmbiguousComponentResolutionException ee) {
                            RuntimeException e = new IllegalStateException("Method " + m + "." + ee.getMessage());
                            // parent.trySetStartupException(e);
                            throw e;
                        }
                        if (oo == null) {
                            final RuntimeException e;
                            if (state == State.INITIALIZED || state == State.STARTING) {
                                if (Container.class.isAssignableFrom(type)) {
                                    e = new IllegalStateException(
                                            "An instance of "
                                                    + type.getSimpleName()
                                                    + " is not available while running methods annotated with @RunAfter(State.STARTING)"
                                                    + ". The @RunAfter(State.RUNNING)"
                                                    + " annotation can be used instead if a " + type.getSimpleName()
                                                    + " is needed." + " [method = " + m + "]");
                                } else {
                                    e = new IllegalStateException("An object of type " + type.getName()
                                            + " is not available for methods with @RunAfter(State.STARTING)"
                                            + " [method = " + m + "]");
                                }
                            } else {
                                e = new IllegalStateException("Instances of " + type.getName()
                                        + " cannot be injected [method = " + m + "]");
                            }
                            // parent.trySetStartupException(e);
                            throw e;
                        }
                        arguments[i] = oo;
                    }
                }
            }
        }

        return null;
    }

    static boolean contains(State[] states, State state) {
        for (State s : states) {
            if (s == state) {
                return true;
            }
        }
        return false;
    }

    static void createString(StringBuilder sb, Method m, int param, String msg) {

    }
}
