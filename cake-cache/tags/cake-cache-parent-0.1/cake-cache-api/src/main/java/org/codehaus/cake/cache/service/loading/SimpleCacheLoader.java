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
package org.codehaus.cake.cache.service.loading;

import org.codehaus.cake.attribute.AttributeMap;

/**
 * A <code>cache loader</code> is used for transparent loading or creation of values by a cache. Instead of the user
 * first checking if a value for a given key exist in the cache and if that is not the case; create the value and add it
 * to the cache. A cache implementation can use a cache loader for lazily creating values for missing entries making
 * this process transparent for the user. A cache loader is also sometimes referred to as a cache backend.
 * <p>
 * Usage example, a loader that for each key (String) loads the file for that given path.
 * 
 * <pre>
 * class FileLoader implements SimpleCacheLoader&lt;String, byte[]&gt; {
 *     public byte[] load(String s, AttributeMap attributes) throws IOException {
 *         File f = new File(s);
 *         byte[] bytes = new byte[(int) f.length()];
 *         (new RandomAccessFile(f, &quot;r&quot;)).read(bytes);
 *         return bytes;
 *     }
 * }
 * </pre>
 * 
 * When a user requests a value for a particular key that is not present in the cache. The cache will automatically call
 * the supplied cache loader to fetch the value. The cache will then insert the key-value pair into the cache and return
 * the value to the user. The cache loader might also be used to fetch an updated value when an entry is no longer valid
 * according to some policy.
 * <p>
 * Another usage of a cache loader is for lazily creating new values. For example, a cache that caches
 * <code>Patterns</code>.
 * 
 * <pre>
 * class PatternLoader implements SimpleCacheLoader&lt;String, Pattern&gt; {
 *     public Pattern load(String s, AttributeMap attributes) throws IOException {
 *         return Pattern.compile(s);
 *     }
 * }
 * </pre>
 * 
 * <p>
 * The load method also provides an attribute map. This map can be used to provide meta-data information to the caller
 * of the {@link #load(Object, AttributeMap)} method. For example, the following cache loader, which retrieves an URL as
 * String. Defines the cost of the element as the number of milliseconds it takes to retrieve the value.
 * 
 * <pre>
 * public static class UrlLoader implements SimpleCacheLoader&lt;URL, String&gt; {
 *     public String load(URL url, AttributeMap attributes) throws Exception {
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
 * TODO write something about attribute map.
 * <p>
 * Cache loader instances <tt>MUST</tt> be thread-safe. Allowing multiple threads to simultaneous create or load new
 * values.
 * <p>
 * Any <tt>cache</tt> implementation that makes use of cache loaders should, but is not required to, make sure that if
 * two threads are simultaneous requesting a value for the same key. Only one of them do the actual loading.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id: CacheLoader.java 529 2007-12-27 17:09:26Z kasper $
 * @param <K>
 *            the type of keys used for loading values
 * @param <V>
 *            the type of values that are loaded
 */
public interface SimpleCacheLoader<K, V> {

    /**
     * Loads a single value.
     * 
     * @param key
     *            the key whose associated value is to be returned.
     * @param attributes
     *            a map of attributes that can the loader can inspect for attributes needed while loading or it can be
     *            updated by the cache loader with additional attributes
     * @return the value to which the specified key is mapped, or null if no such mapping exist.
     * @throws Exception
     *             An exception occured while loading or creating the value
     */
    V load(K key, AttributeMap attributes) throws Exception;
}
