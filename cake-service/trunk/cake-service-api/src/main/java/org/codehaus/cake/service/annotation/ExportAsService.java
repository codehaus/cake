package org.codehaus.cake.service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;

/**
 * This annotation can be used for registering for easily attaching objects/services to a {@link Container} so that they
 * can later be easily retrieved.
 * 
 * For example, if you wish to make this object easily available from a container
 * 
 * <pre>
 * &#064;ExportAsService(Runnable.class)
 * public class HelloRunnable implements Runnable {
 *     public void run() {
 *         System.out.println(&quot;Hello&quot;);
 *     }
 * }
 * </pre>
 * 
 * You can register the object with the configuration of the container before initializing it, using
 * 
 * <pre>
 * ContainerConfiguration&lt;?&gt; configuration = null;
 * configuration.addToLifecycle(new ExportRunnable());
 * SomeContainer container = new SomeContainer(configuration);
 * </pre>
 * 
 * Finally the object can be retrieved using {@link #value()} as the key.
 * 
 * <pre>
 * Runnable r = container.getService(Runnable.class);
 * r.run(); //Prints hello
 * </pre>
 * 
 * <p>
 * If the service is exported with a key that conflicts with the key-type of any of the build-in services. The
 * registered service will replace the build-in service when trying to retrive using
 * {@link ServiceManager#getService(Class)} or
 * {@link ServiceManager#getService(Class, org.codehaus.cake.attribute.AttributeMap)}. Together with ServiceFactory
 * this can be used to introduce some powerfull behaviour
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportAsService {
    /**
     * The key that is used for looking up the exported service using {@link ServiceManager#getService(Class)} or
     * {@link ServiceManager#getService(Class, org.codehaus.cake.attribute.AttributeMap)}
     * 
     * @return the key of the service
     */
    Class<?> value();
}
