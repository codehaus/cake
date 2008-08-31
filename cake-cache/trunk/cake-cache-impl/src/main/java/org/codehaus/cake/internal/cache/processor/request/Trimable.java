package org.codehaus.cake.internal.cache.processor.request;

import java.util.List;

import org.codehaus.cake.cache.CacheEntry;

public interface Trimable<K, V> {
    void setTrimmed(List<CacheEntry<K, V>> iter);

    List<CacheEntry<K, V>> getTrimmed();
    
    
    boolean doTrim();
}
