package org.codehaus.cake.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Starts the service. The specified service registrant can be used to retrieve other services or register services.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLifecycle.java 511 2007-12-13 14:37:02Z kasper $
 */
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Startable {}
