package org.codehaus.cake.internal.cache.service.attribute;

import static org.codehaus.cake.cache.CacheEntry.COST;
import static org.codehaus.cake.cache.CacheEntry.SIZE;
import static org.codehaus.cake.cache.CacheEntry.TIME_CREATED;
import static org.codehaus.cake.cache.CacheEntry.TIME_MODIFIED;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.internal.attribute.generator.DefaultAttributeConfiguration;
import org.codehaus.cake.internal.attribute.generator.DefaultMapGenerator;
import org.codehaus.cake.internal.cache.service.attribute.factories.AbstractAttributeFactory;
import org.codehaus.cake.internal.cache.service.attribute.factories.CostAttributeFactory;
import org.codehaus.cake.internal.cache.service.attribute.factories.CreationTimeAttributeFactory;
import org.codehaus.cake.internal.cache.service.attribute.factories.ModificationTimeAttributeFactory;
import org.codehaus.cake.internal.cache.service.attribute.factories.SizeAttributeFactory;
import org.codehaus.cake.internal.service.Composer;

public class MemorySparseAttributeService<K, V> implements InternalAttributeService<K, V> {
    private static final AtomicLong al = new AtomicLong();
    private Composer composer;
    private Constructor<AttributeMap> constructor;

    private AbstractAttributeFactory<K, V>[] factories;

    private final Map<Attribute, Info> map = new HashMap<Attribute, Info>();

    public MemorySparseAttributeService(Composer composer, CacheAttributeConfiguration configuration) {
        this.composer = composer;
        for (Object o : configuration.getAllAttributes()) {
            Attribute a = (Attribute) o;
            map.put(a, new Info(a, false, false, a.getDefault()));
        }
        updateAttributes();
    }

    public AttributeMap create(K key, V value, AttributeMap params) {
        return update(key, value, params, null);
    }

    public AttributeMap getAttributes() {
        AttributeMap am = new DefaultAttributeMap();
        for (Info i : map.values()) {
            am.put(i.getAttribute(), i.defaultValue);
        }
        return am;
    }

    public <T> T getDefaultValue(Attribute<T> attribute) {
        Info i = map.get(attribute);
        return i == null ? attribute.getDefault() : (T) i.defaultValue;
    }

    public <T> void add(Attribute<T> attribute) {
        map.put(attribute, new Info(attribute, true, true, attribute.getDefault()));
        updateAttributes();
    }

    public AttributeMap remove(AttributeMap params) {
        throw new UnsupportedOperationException();
    }

    public <T> void setDefaultValue(Attribute<T> attribute, T defaultValue) {
        map.put(attribute, new Info(attribute, true, true, defaultValue));
    }

    public AttributeMap update(K key, V value, AttributeMap params, AttributeMap previous) {
        Object[] objects = new Object[factories.length];
        for (int i = 0; i < factories.length; i++) {
            objects[i] = factories[i].getValue(key, value, params, previous);
        }
        try {
            return constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // resort to DefaultAttributeMap?????
        return null;
    }

    // private static Map<Class, Set<DefaultAttributeConfiguration>> constructors = new
    // WeakHashMap<Class, DefaultAttributeConfiguration>();

    private void updateAttributes() {
        factories = new AbstractAttributeFactory[map.size()];
        int count = 0;
        composer.registerImplementation(TIME_CREATED, CreationTimeAttributeFactory.class);
        composer.registerImplementation(TIME_MODIFIED, ModificationTimeAttributeFactory.class);
        composer.registerImplementation(SIZE, SizeAttributeFactory.class);
        composer.registerImplementation(COST, CostAttributeFactory.class);
        for (Info i : map.values()) {
            AbstractAttributeFactory f = (AbstractAttributeFactory) composer
                    .getFromKeyIfAvailable(i.getAttribute());
            factories[count++] = f;
        }
        long start = System.nanoTime();
        try {
            Class factory = DefaultMapGenerator.generate("CacheEntry" + al.getAndIncrement(),
                    new ArrayList(map.values()));
         //   System.out.println(System.nanoTime() - start);
            start = System.nanoTime();
            constructor = (Constructor) factory.getConstructors()[0];
        } catch (Exception e1) {
            e1.printStackTrace();
        }
       // System.out.println(System.nanoTime() - start);
//        System.out.println("----");
    }

    static class Info extends DefaultAttributeConfiguration {

        public Info(Attribute a, boolean isMutable, boolean isHidden, Object defaultValue) {
            super(a, isMutable, isHidden);
            this.defaultValue = defaultValue;
        }

        Object defaultValue;
    }

    public void attachToPolicy(Attribute<?> attribute) {
        // TODO Auto-generated method stub
        
    }

    public void dependOnHard(Attribute<?> attribute) {
        // TODO Auto-generated method stub
        
    }

    public void dependOnSoft(Attribute<?> attribute) {
        // TODO Auto-generated method stub
        
    }
}
