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
}
