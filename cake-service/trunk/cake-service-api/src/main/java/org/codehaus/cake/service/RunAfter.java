package org.codehaus.cake.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.codehaus.cake.service.Container.State;

/**
 * Used for annotating methods that should be run <tt>after</tt> a container has
 * reached a specific state, and before it transitions to the next state.
 * The annotated method must be public and on a public class.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RunAfter {
    /** @return the state(s) after which the annotated method should be run */
    State[] value();

    /** @return runs the method asynchronously */
    boolean asynchronous() default false;

    // boolean runBeforeState() default false;
    
    /**
     * Methods annotated with OnTermination on objects that are registered using
     * {@link ContainerConfiguration#addService(Object)}, {@link ContainerConfiguration#addService(Class, Object)} or
     * {@link ContainerConfiguration#addService(Class, ServiceProvider)} will be invoked after the container has been fully
     * shutdown and the container has reached the  the container has terminated. This method is invoked as the last method in the lifecycle of a
     * container. This method is also called if the container failed to initialize or start. But only if the service was
     * successfully initialized was run without failing.
     */

    /**
     * Used for annotating a method that will be run after a container has reached the {@link State#RUNNING} state.
     * <p>
     * The annotated method must public and its owning class must also be public.
     * <p>
     * The method annotated can take a parameter of type {@link Container} (or subtypes hereof). In which case the
     * container that the service has been registered with will be parsed along to the method.
     * 
     * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
     * @version $Id: AfterStart.java 225 2008-11-30 20:53:08Z kasper $
     */

}
