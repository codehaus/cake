package org.codehaus.cake.internal.cache.service.attribute.factories;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_DATE_CREATED;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;

public final class CreationTimeAttributeFactory<K, V> extends AbstractAttributeFactory<K, V> {
    private final Clock clock;

    public CreationTimeAttributeFactory(InternalExceptionService ies, Clock clock) {
        super(ENTRY_DATE_CREATED, ies);
        if (clock == null) {
            throw new NullPointerException("clock is null");
        }
        this.clock = clock;
    }

    @Override
    public Object op(K key, V value, AttributeMap user, AttributeMap existing) {
        long creationTime = user.get(ENTRY_DATE_CREATED);
        final long time;
        if (creationTime > 0) {
            time = creationTime;
        } else if (existing != null) {
            time = existing.get(ENTRY_DATE_CREATED);
        } else {
            time = clock.timestamp();
        }
        if (!ENTRY_DATE_CREATED.isValid(creationTime)) {
            illegalAttribute(key, creationTime, time);
        }
        return time;
    }
}
