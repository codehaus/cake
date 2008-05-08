package org.codehaus.cake.internal.cache.service.attribute.factories;

import static org.codehaus.cake.cache.CacheEntry.TIME_MODIFIED;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;

public final class ModificationTimeAttributeFactory<K, V> extends AbstractAttributeFactory<K, V> {
    private final Clock clock;

    public ModificationTimeAttributeFactory(InternalExceptionService ies, Clock clock) {
        super(TIME_MODIFIED, ies);
        if (clock == null) {
            throw new NullPointerException("clock is null");
        }
        this.clock = clock;
    }

    @Override
    public Object op(K key, V value, AttributeMap user, AttributeMap existing) {
        long modificationTime = user.get(TIME_MODIFIED);
        final long time;
        if (modificationTime > 0) {
            time = modificationTime;
        } else {
            time = clock.timeOfDay();
        }
        if (!TIME_MODIFIED.isValid(modificationTime)) {
            illegalAttribute(key, modificationTime, time);
        }
        return time;
    }
}
