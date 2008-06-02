package org.codehaus.cake.internal.cache.service.attribute.factories;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;

public final class SizeAttributeFactory<K, V> extends AbstractAttributeFactory<K, V> {
    public SizeAttributeFactory(InternalExceptionService ies) {
        super(SIZE, ies);
    }

    @Override
    public Object getValue(K key, V value, AttributeMap user, AttributeMap existing) {
        long userSize = user.get(SIZE);
        final long size;
        if (userSize > 0) {
            size = userSize;
        } else {
            size = 1;
        }
        if (!SIZE.isValid(userSize)) {
            illegalAttribute(key, userSize, size);
        }
        return size;
    }
}
