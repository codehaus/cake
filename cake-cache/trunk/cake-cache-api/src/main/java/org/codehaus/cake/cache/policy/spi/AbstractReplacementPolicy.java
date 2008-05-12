package org.codehaus.cake.cache.policy.spi;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.codehaus.cake.service.Startable;

public abstract class AbstractReplacementPolicy<K, V> implements ReplacementPolicy<K, V> {

    /** The attributes that have been registered. */
    private Set<Attribute<?>> attributes = new HashSet<Attribute<?>>();

    /** Lock object. */
    private final Object lock = new Object();

    /**
     * The default implementation of touch does nothing.
     */
    public void touch(CacheEntry<K, V> entry) {}

    /**
     * This method can be used to attach special attributes to each cache entry. registered should
     * only be read inside methods provided by abstract...
     * 
     * @param attribute
     */
    protected final void attachToEntry(Attribute<?> attribute) {
        synchronized (lock) {
            if (attributes.contains(attribute)) {
                throw new IllegalArgumentException("attribute has already been attached");
            }
            attributes.add(attribute);
        }
    }

    /**
     * Registers the necessary hooks into the cache for this replacement policy.
     * 
     * @param service
     *            the CacheAttributeService
     */
    @UseInternals
    @Startable
    public final void registerAttribute(InternalCacheAttributeService service) {
        synchronized (lock) {
            if (attributes == null) {
                throw new IllegalStateException("registerAttribute() has already been called once");
            }
            try {
                for (Attribute a : attributes) {
                    service.attachToPolicy(a);
                }

            } finally {
                attributes = null;
            }

        }
    }
}
