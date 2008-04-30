package org.codehaus.cake.internal.cache.service.attribute;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_DATE_CREATED;
import static org.codehaus.cake.cache.CacheAttributes.ENTRY_DATE_MODIFIED;

import java.util.Map;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.cache.service.attribute.CacheAttributeService;
import org.codehaus.cake.util.Clock;

public class DefaultAttributeService<K, V> implements CacheAttributeService,
        InternalAttributeService<K, V> {
    private final AttributeMap defaults;
    private final Clock clock;

    public DefaultAttributeService(CacheAttributeConfiguration configuration, Clock clock) {
        defaults = new DefaultAttributeMap(configuration.getAllAttributes());
        this.clock = clock;
    }

    public AttributeMap getAttributes() {
        return new DefaultAttributeMap(defaults);
    }

    public <T> T getDefaultValue(Attribute<T> attribute) {
        return defaults.get(attribute);
    }

    public <T> void setDefaultValue(Attribute<T> attribute, T defaultValue) {
        // shouldn't expose map to .set
        attribute.checkValid(defaultValue);
        defaults.put(attribute, defaultValue);
    }

    public AttributeMap create(K key, V value, AttributeMap params) {
        AttributeMap map = new DefaultAttributeMap(defaults);
        if (defaults.contains(ENTRY_DATE_CREATED)) {
            map.put(ENTRY_DATE_CREATED, clock.timestamp());
        }
        if (defaults.contains(ENTRY_DATE_MODIFIED)) {
            map.put(ENTRY_DATE_MODIFIED, clock.timestamp());
        }
        for (Map.Entry<Attribute, Object> entry : params.entrySet()) {
            if (defaults.contains(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    public AttributeMap remove(AttributeMap params) {
        // TODO Auto-generated method stub
        return null;
    }

    public AttributeMap update(K key, V value, AttributeMap params, AttributeMap previous) {
        AttributeMap map = new DefaultAttributeMap(defaults);
        for (Map.Entry<Attribute, Object> entry : params.entrySet()) {
            if (defaults.contains(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        if (defaults.contains(ENTRY_DATE_CREATED)) {
            map.put(ENTRY_DATE_CREATED, previous.get(ENTRY_DATE_CREATED));
        }
        if (defaults.contains(ENTRY_DATE_MODIFIED)) {
            map.put(ENTRY_DATE_MODIFIED, clock.timestamp());
        }
        return map;
    }

    public <T> void registerAttribute(Attribute<T> attribute) {
    // TODO Auto-generated method stub

    }
}
