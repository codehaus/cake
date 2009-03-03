package org.codehaus.cake.internal.cache;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class InternalCacheAttributes {
    public static final ObjectAttribute<Predicate> CACHE_FILTER = new ObjectAttribute(Predicate.class) {};

    public static final ObjectAttribute<Cache> CONTAINER = new ObjectAttribute(Cache.class) {};
}
