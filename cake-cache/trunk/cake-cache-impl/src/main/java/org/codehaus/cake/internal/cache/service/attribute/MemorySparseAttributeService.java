package org.codehaus.cake.internal.cache.service.attribute;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.internal.attribute.generator.DefaultAttributeConfiguration;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.CreateAction;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.ModifyAction;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;

public class MemorySparseAttributeService<K, V> implements InternalAttributeService<K, V> {
    private static final AtomicLong al = new AtomicLong();
    private Composer composer;
    private Constructor<AttributeMap> constructor;

    private CacheAttributeMapFactory<K, V> generator;

    private final Map<Attribute, CacheAttributeMapConfiguration> map = new HashMap<Attribute, CacheAttributeMapConfiguration>();
    private final Clock clock;
    private final InternalExceptionService ies;

    public MemorySparseAttributeService(Composer composer, CacheAttributeConfiguration configuration, Clock clock,
            InternalExceptionService ies) {
        this.composer = composer;
        this.clock = clock;
        this.ies = ies;
        for (Object o : configuration.getAllAttributes()) {
            Attribute a = (Attribute) o;
            CacheAttributeMapConfiguration sac = CacheAttributeMapConfiguration.getPredefinedConfiguration(a);
            map.put(a, sac);
        }
        updateAttributes();
    }

    public AttributeMap create(K key, V value, AttributeMap params) {
        return generator.create(key, value, params, null);
    }

    public AttributeMap update(K key, V value, AttributeMap params, AttributeMap previous) {
        return generator.create(key, value, params, previous);
    }

    // private static Map<Class, Set<DefaultAttributeConfiguration>> constructors = new
    // WeakHashMap<Class, DefaultAttributeConfiguration>();

    private void updateAttributes() {
        String name = "CacheEntryGenerator" + al.getAndIncrement();
        try {
            generator = CacheAttributeMapFactoryGenerator.generate(name, new ArrayList(map.values()), clock, ies);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void attachToPolicy(Attribute<?> attribute) {
        CacheAttributeMapConfiguration sac = new CacheAttributeMapConfiguration(attribute, false, true, false, true);
        sac.setReadMapOnCreate(false);
        sac.setCreateAction(CreateAction.DEFAULT);
        sac.setModifyAction(ModifyAction.KEEP_EXISTING);
        map.put(attribute, sac);
        updateAttributes();
    }

    public void dependOnHard(Attribute<?> attribute) {
        if (!map.containsKey(attribute)) {
            throw new IllegalStateException("Attribute " + attribute.getName()
                    + " must be registered to use the specified replacement policy" + "[type=" + attribute.getClass());
        }
    }

    public void dependOnSoft(Attribute<?> attribute) {
        if (!map.containsKey(attribute)) {
            CacheAttributeMapConfiguration sac = CacheAttributeMapConfiguration.getPredefinedConfigurationSoft(attribute);
            map.put(attribute, sac);
        }
        updateAttributes();
    }

    public void access(AttributeMap map) {
       generator.access(map);
    }
}
