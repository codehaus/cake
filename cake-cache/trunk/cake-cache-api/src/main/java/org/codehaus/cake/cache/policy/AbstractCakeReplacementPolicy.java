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
package org.codehaus.cake.cache.policy;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.GetAttributer;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.PolicyAttachmentFactory;
import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.codehaus.cake.service.OnStart;

/**
 * An abstract implementation of a {@link ReplacementPolicy} that is intended for policies that need to attach
 * attributes to cache entries.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AbstractReplacementPolicy.java 229 2008-12-10 19:53:58Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractCakeReplacementPolicy<K, V> implements ReplacementPolicy<K, V> {

    private Set<Attribute<?>> hardDependencies = new HashSet<Attribute<?>>();

    /** Lock object. */
    private final Object lock = new Object();

    private Set<Attribute<?>> softDependencies = new HashSet<Attribute<?>>();

    protected final void dependHard(Attribute<?> attribute) {
        synchronized (lock) {
            if (softDependencies.contains(attribute) || hardDependencies.contains(attribute)) {
                throw new IllegalArgumentException("attribute has already been attached");
            }
            hardDependencies.add(attribute);
        }
    }

    protected final void dependSoft(Attribute<?> attribute) {
        synchronized (lock) {
            if (softDependencies.contains(attribute) || hardDependencies.contains(attribute)) {
                throw new IllegalArgumentException("attribute has already been attached");
            }
            softDependencies.add(attribute);
        }
    }

    /**
     * Registers the necessary hooks into the cache for this replacement policy.
     * 
     * @param service
     *            the CacheAttributeService
     */
    @UseInternals
    @OnStart
    public final void registerAttribute(InternalCacheAttributeService service) {
        synchronized (lock) {
            if (softDependencies == null) {
                throw new IllegalStateException("registerAttribute() has already been called once");
            }
            try {
                register(service);
                for (Attribute a : softDependencies) {
                    service.dependOnSoft(a);
                }
                for (Attribute a : hardDependencies) {
                    service.dependOnHard(a);
                }
            } finally {
                softDependencies = null;
                hardDependencies = null;
            }
        }
    }

    /**
     * Default implementation just selects the new entry.
     */
    public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
        return newEntry;
    }

    /**
     * The default implementation of touch does nothing.
     */
    public void touch(CacheEntry<K, V> entry) {
    }

    protected <T> void register(PolicyAttachmentFactory paf) {
    }
}
