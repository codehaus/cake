package org.codehaus.cake.internal.cache.loading.spi;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public interface InternalCacheLoader<K, V> {
    V load(K key, AttributeMap attributes);
    boolean needsMutableAttributes();
    V load(K key, MutableAttributeMap attributes);

    boolean useAsync();
    
    
}
