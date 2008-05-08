package org.codehaus.cake.internal.cache.service.attribute.factories;

import static org.codehaus.cake.cache.CacheEntry.COST;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;

public final class CostAttributeFactory<K, V> extends AbstractAttributeFactory<K, V> {
    public CostAttributeFactory(InternalExceptionService ies) {
        super(COST, ies);
    }

    @Override
    public Object op(K key, V value, AttributeMap user, AttributeMap existing) {
        double userCost = user.get(COST);
        final double cost;
        if (userCost > 0) {
            cost = userCost;
        } else {
            cost = 1;
        }
        if (!COST.isValid(userCost)) {
            illegalAttribute(key, userCost, cost);
        }
        return cost;
    }
}
