/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.attributes;

import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;

public class CostDecoratedLoader<K, V> implements BlockingCacheLoader<K, V> {
    private final BlockingCacheLoader<K, V> realLoader;

    public CostDecoratedLoader(BlockingCacheLoader<K, V> realLoader) {
        this.realLoader = realLoader;
    }

    public V load(K key, MutableAttributeMap attributes) throws Exception {
        long start = System.nanoTime();
        V v = realLoader.load(key, attributes);
        attributes.put(CacheEntry.COST, System.nanoTime() - start);
        return v;
    }
}
