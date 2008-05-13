package org.codehaus.cake.internal.cache.service.attribute;

import static org.codehaus.cake.cache.CacheEntry.TIME_CREATED;
import static org.codehaus.cake.cache.CacheEntry.TIME_MODIFIED;

import java.util.Map;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.codehaus.cake.util.Clock;

public class DefaultAttributeService<K, V> implements 
        InternalAttributeService<K, V>, InternalCacheAttributeService {
    private final AttributeMap defaults;
    private final Clock clock;

    public DefaultAttributeService(CacheAttributeConfiguration configuration, Clock clock) {
        defaults = new DefaultAttributeMap();
        for (Object o : configuration.getAllAttributes()) {
            Attribute a = ((Attribute) o);
            defaults.put(a, a.getDefault());

        }
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
        if (defaults.contains(TIME_CREATED)) {
            map.put(TIME_CREATED, clock.timeOfDay());
        }
        if (defaults.contains(TIME_MODIFIED)) {
            map.put(TIME_MODIFIED, clock.timeOfDay());
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
        if (defaults.contains(TIME_CREATED)) {
            map.put(TIME_CREATED, previous.get(TIME_CREATED));
        }
        if (defaults.contains(TIME_MODIFIED)) {
            map.put(TIME_MODIFIED, clock.timeOfDay());
        }
        return map;
    }

    public <T> void add(Attribute<T> attribute) {
    // TODO Auto-generated method stub

    }

    public void attachToPolicy(Attribute<?> attribute) {
        // TODO Auto-generated method stub
        
    }
}
