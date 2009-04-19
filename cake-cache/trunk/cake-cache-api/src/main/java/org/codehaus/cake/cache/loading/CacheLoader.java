package org.codehaus.cake.cache.loading;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A <code>cache loader</code> is used for transparent loading or creation of values by a cache. Instead of the user
 * first checking if a value for a given key exist in the cache and if that is not the case; create the value and add it
 * to the cache. A cache implementation can use a cache loader for lazily creating values for missing entries making
 * this process transparent for the user. A cache loader is also sometimes referred to as a cache backend.
 * <p>
 * Usage example, a cache loader that for a given <tt>path</tt> tries to open the corresponding file and return the
 * content as a byte array:
 * 
 * <pre>
 * public class FileLoader {
 *     &#064;CacheLoader
 *     public byte[] load(String path) throws IOException {
 *         File f = new File(path);
 *         byte[] bytes = new byte[(int) f.length()];
 *         (new RandomAccessFile(f, &quot;r&quot;)).read(bytes);
 *         return bytes;
 *     }
 * }
 * </pre>
 * 
 * When a user requests a value for a particular key that is not present in the cache. The cache will normally
 * automatically call the supplied cache loader to fetch the value. The cache will then automatically insert the
 * key-value pair into the cache and return the value to the user.
 * <p>
 * Another usage of a cache loader is for lazily creating new values. For example, a cache that caches
 * <code>Patterns</code>.
 * 
 * <pre>
 * public class PatternLoader {
 *     &#064;CacheLoader
 *     public Pattern load(String s) throws IOException {
 *         return Pattern.compile(s);
 *     }
 * }
 * </pre>
 * 
 * <p>
 * A method annotated with CacheLoader can also take an attribute map. This map can be used to provide meta-data
 * information to and from the caller of the method. For example, the following cache loader, which retrieves an URL as
 * String. Defines the cost of the element as the number of milliseconds it takes to retrieve the value.
 * 
 * <pre>
 * public static class UrlLoader {
 *     &#064;CacheLoader
 *     public String load(URL url, MutableAttributeMap attributes) throws IOException {
 *         long start = System.currentTimeMillis();
 *         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
 *         StringBuilder sb = new StringBuilder();
 *         int str;
 *         while ((str = in.read()) != -1) {
 *             sb.append((char) str);
 *         }
 *         in.close();
 *         CacheEntry.setCost(attributes, System.currentTimeMillis() - start);
 *         return sb.toString();
 *     }
 * }
 * </pre>
 * 
 * <p>
 * Cache loader instances <tt>MUST</tt> be thread-safe. Allowing multiple threads to simultaneous load new values.
 * <p>
 * Any <tt>cache</tt> implementation that makes use of cache loaders should, but is not required to, make sure that if
 * two threads are simultaneous requesting a value for the same key. Only one of them do the actual loading.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id: BlockingCacheLoader.java 327 2009-04-08 09:34:27Z kasper $
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLoader {
    /** @return a description of the loader */
    String description() default "";
}
