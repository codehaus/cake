package org.codehaus.cake.internal.cache.attribute;

import org.codehaus.cake.attribute.Attribute;

public interface InternalCacheAttributeService {
    void attachToPolicy(Attribute<?> attribute);
}
