package org.codehaus.cake.internal.cache.service.attribute.factories;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;

public abstract class AbstractAttributeFactory<K, V> {
    private final Attribute att;
    private final InternalExceptionService ies;

    AbstractAttributeFactory(Attribute att, InternalExceptionService ies) {
        this.att = att;
        this.ies = ies;
    }

    public Attribute getAttribute() {
        return att;
    }

    public abstract Object getValue(K key, V value, AttributeMap user, AttributeMap existing);

    protected void illegalAttribute(K key, Object illegalValue, Object resortedTo) {
        ies.warning("ILD " + key);
    }
}
