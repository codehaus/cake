/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.cache.service.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.GetAttributer;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.policy.AbstractCakeReplacementPolicy;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.CreateAction;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.ModifyAction;
import org.codehaus.cake.internal.cache.service.memorystore.CacheMap.HashEntry;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;

public class MemorySparseAttributeService<K, V> implements InternalAttributeService<K, V> {
    private static final AtomicLong al = new AtomicLong();
    // private Constructor<AttributeMap> constructor;

    private CacheAttributeMapFactory<K, V> generator;

    private final Map<Attribute, CacheAttributeMapConfiguration> map = new HashMap<Attribute, CacheAttributeMapConfiguration>();
    private final Clock clock;
    private final InternalExceptionService<?> ies;
    private AbstractCakeReplacementPolicy<K, V> policy;

    public MemorySparseAttributeService(CacheConfiguration configuration, Clock clock, InternalExceptionService<?> ies) {
        this.clock = clock;
        this.ies = ies;
        ReplacementPolicy<K, V> p = configuration.withMemoryStore().getPolicy();
        if (p instanceof AbstractCakeReplacementPolicy) {
            policy = (AbstractCakeReplacementPolicy<K, V>) p;
        }
        for (Object o : configuration.getAllEntryAttributes()) {
            Attribute a = (Attribute) o;
            CacheAttributeMapConfiguration sac = CacheAttributeMapConfiguration.getPredefinedConfiguration(a);
            map.put(a, sac);
        }
        initialize();
    }

    public AttributeMap create(K key, V value, GetAttributer params) {
        return generator.create(key, value, params, null);
    }

    public AttributeMap update(K key, V value, GetAttributer params, GetAttributer previous) {
        return generator.create(key, value, params, previous);
    }

    private static Class<CacheAttributeMapFactory> noAttributes;

    public void initialize() {
        if (map.size() == 0) {
            generator = CacheAttributeMapFactoryGenerator.NO_ATTRIBUTES_FACTORY;
        } else {
            String name = "CacheEntryGenerator" + al.getAndIncrement();
            try {
                generator = CacheAttributeMapFactoryGenerator.generate(name, new ArrayList(map.values()), clock, ies);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void attachToPolicy(Attribute<?> attribute) {
        CacheAttributeMapConfiguration sac = new CacheAttributeMapConfiguration(attribute, false, true, false, true);
        sac.setReadMapOnCreate(false);
        sac.setCreateAction(CreateAction.DEFAULT);
        sac.setModifyAction(ModifyAction.KEEP_EXISTING);
        map.put(attribute, sac);
        initialize();
    }

    public void dependOnHard(Attribute<?> attribute) {
        if (!map.containsKey(attribute)) {
            throw new IllegalStateException("Attribute " + attribute.getName()
                    + " must be registered to use the specified replacement policy" + "[type=" + attribute.getClass());
        }
    }

    public void dependOnSoft(Attribute<?> attribute) {
        if (!map.containsKey(attribute)) {
            CacheAttributeMapConfiguration sac = CacheAttributeMapConfiguration
                    .getPredefinedConfigurationSoft(attribute);
            map.put(attribute, sac);
        }
        initialize();
    }

    public void access(GetAttributer map) {
        generator.access((AttributeMap) map);
    }

    public BooleanAttachment attachBoolean() {
        final BooleanAttribute a = new BooleanAttribute() {};
        
        return new BooleanAttachment() {
            public boolean get(Object entry) {
                return ((GetAttributer) entry).get(a);
            }

            public void set(Object entry, boolean value) {
                ((HashEntry) entry).getAttributes().put(a, value);
            }
        };
    }

    public IntAttachment attachInt() {
        final IntAttribute a = new IntAttribute() {};
        attachToPolicy(a);
        return new IntAttachment() {
            public int get(Object entry) {
              return  ((GetAttributer) entry).get(a);
            }

            public void set(Object entry, int value) {
                ((HashEntry) entry).getAttributes().put(a, value);
            }
        };
    }

    public <T> ObjectAttachment<T> attachObject(Class<T> type) {
        final ObjectAttribute<T> a = new ObjectAttribute<T>(type) {};
        attachToPolicy(a);
        return new ObjectAttachment<T>() {
            public T get(Object entry) {
                return ((GetAttributer) entry).get(a);
            }

            public void set(Object entry, T value) {
                ((HashEntry) entry).getAttributes().put(a, value);
            }
        };

    }

  
    //
    // public <T> Reg<T> newObject(Class<T> type) {
    // if (type.equals(Boolean.TYPE)) {
    // ObjectAttribute<T> oa = new ObjectAttribute<T>(type) {};
    // attachToPolicy(oa);
    // if (true) {
    // throw new UnsupportedOperationException();
    // }
    // return new ObjectRef<T>(oa);
    // } else {
    // ObjectAttribute<T> oa = new ObjectAttribute<T>(type) {};
    // attachToPolicy(oa);
    // return new ObjectRef<T>(oa);
    // }
    // }



}
