package org.codehaus.cake.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method invoked when the container has terminated. This method is invoked as the last method in
 * this lifecycle interface and is called when the container and all of it services has been
 * succesfully shutdown. This method is also called if the container failed to initialize or start.
 * But only if the service was succesfully initialized was run without failing).
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLifecycle.java 511 2007-12-13 14:37:02Z kasper $
 */
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Disposable {

}
