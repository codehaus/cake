package org.codehaus.cake.internal.cache.service.attribute.factories;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_COST;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;

public final class CostAttributeFactory<K, V> extends AbstractAttributeFactory<K, V> {
    public CostAttributeFactory(InternalExceptionService ies) {
        super(ENTRY_COST, ies);
    }

    @Override
    public Object op(K key, V value, AttributeMap user, AttributeMap existing) {
        double userCost = user.get(ENTRY_COST);
        final double cost;
        if (userCost > 0) {
            cost = userCost;
        } else {
            cost = 1;
        }
        if (!ENTRY_COST.isValid(userCost)) {
            illegalAttribute(key, userCost, cost);
        }
        return cost;
    }
}
