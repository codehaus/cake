/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.cache.service.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.internal.cache.policy.FakePolicyContext;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.CreateAction;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.ModifyAction;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public class MemorySparseAttributeService<K, V> implements InternalAttributeService<K, V> {
    private static final AtomicLong al = new AtomicLong();
    // private Constructor<AttributeMap> constructor;

    private static Class<CacheAttributeMapFactory> noAttributes;

    private final Clock clock;
    private CacheAttributeMapFactory<K, V> generator;
    private final InternalExceptionService<?> ies;
    private final Map<Attribute, CacheAttributeMapConfiguration> map = new HashMap<Attribute, CacheAttributeMapConfiguration>();

    public final ReplacementPolicy<CacheEntry<K, V>> policy;

    public MemorySparseAttributeService(CacheConfiguration configuration, Clock clock, InternalExceptionService<?> ies) {
        this.clock = clock;
        this.ies = ies;
        Class<? extends ReplacementPolicy> pol = configuration.withMemoryStore().getPolicy();
        if (pol != null) {
            FakePolicyContext<?> fpc = new FakePolicyContext(Object.class);
            this.policy = pol == null ? null : Policies.create(pol, fpc);
            for (Attribute a : fpc.getSoftDependencies()) {
                CacheAttributeMapConfiguration sac = CacheAttributeMapConfiguration.getPredefinedConfiguration(a);
                map.put(a, sac);
            }
        } else {
            this.policy = null;
        }
        for (Object o : configuration.getAllEntryAttributes()) {
            Attribute a = (Attribute) o;
            CacheAttributeMapConfiguration sac = CacheAttributeMapConfiguration.getPredefinedConfiguration(a);
            map.put(a, sac);
        }
        initialize();
    }

    public void access(AttributeMap map) {
        generator.access((MutableAttributeMap) map);
    }

    public void attachToPolicy(Attribute<?> attribute) {
        CacheAttributeMapConfiguration sac = new CacheAttributeMapConfiguration(attribute, false, true, false, true);
        sac.setReadMapOnCreate(false);
        sac.setCreateAction(CreateAction.DEFAULT);
        sac.setModifyAction(ModifyAction.KEEP_EXISTING);
        map.put(attribute, sac);
        initialize();
    }

    public MutableAttributeMap create(K key, V value, AttributeMap params) {
        return generator.create(key, value, params, null);
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

    public MutableAttributeMap update(K key, V value, AttributeMap params, AttributeMap previous) {
        return generator.create(key, value, params, previous);
    }
}
