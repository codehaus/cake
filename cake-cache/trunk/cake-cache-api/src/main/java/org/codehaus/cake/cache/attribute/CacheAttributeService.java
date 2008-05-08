package org.codehaus.cake.cache.attribute;

import org.codehaus.cake.attribute.Attribute;

public interface CacheAttributeService {
    <T> void add(Attribute<T> attribute);
}
