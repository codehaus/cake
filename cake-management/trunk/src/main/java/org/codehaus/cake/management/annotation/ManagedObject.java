package org.codehaus.cake.management.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to mark objects that should be exposed via JMX.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ManagedOperation.java 432 2007-11-11 17:03:04Z kasper $
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagedObject {
    /**
     * The name of the object.
     */
    String defaultValue() default "";

    /**
     * The description of the object.
     */
    String description() default "";
}
