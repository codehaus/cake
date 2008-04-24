package org.codehaus.cake.internal.cache.service.attribute.factories;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_DATE_MODIFIED;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;

public final class ModificationTimeAttributeFactory<K, V> extends AbstractAttributeFactory<K, V> {
    private final Clock clock;

    public ModificationTimeAttributeFactory(InternalExceptionService ies, Clock clock) {
        super(ENTRY_DATE_MODIFIED, ies);
        if (clock == null) {
            throw new NullPointerException("clock is null");
        }
        this.clock = clock;
    }

    @Override
    public Object op(K key, V value, AttributeMap user, AttributeMap existing) {
        long modificationTime = user.get(ENTRY_DATE_MODIFIED);
        final long time;
        if (modificationTime > 0) {
            time = modificationTime;
        } else {
            time = clock.timestamp();
        }
        if (!ENTRY_DATE_MODIFIED.isValid(modificationTime)) {
            illegalAttribute(key, modificationTime, time);
        }
        return time;
    }
}
