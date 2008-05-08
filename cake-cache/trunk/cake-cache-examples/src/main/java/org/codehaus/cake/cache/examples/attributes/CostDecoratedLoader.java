/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.attributes;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.SimpleCacheLoader;

public class CostDecoratedLoader<K, V> implements SimpleCacheLoader<K, V> {
    private final SimpleCacheLoader<K, V> realLoader;

    public CostDecoratedLoader(SimpleCacheLoader<K, V> delegator) {
        if (delegator == null) {
            throw new NullPointerException("delegator is null");
        }
        this.realLoader = delegator;
    }

    public V load(K key, AttributeMap attributes) throws Exception {
        long start = System.nanoTime();
        V v = realLoader.load(key, attributes);
        CacheEntry.COST.set(attributes, System.nanoTime() - start);
        return v;
    }
}
