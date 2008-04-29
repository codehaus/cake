package org.codehaus.cake.cache.policy.spi;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;

public abstract class AbstractReplacementPolicy<K, V> implements ReplacementPolicy<K, V> {

    @Override
    public void touch(CacheEntry<K, V> entry) {}
}
