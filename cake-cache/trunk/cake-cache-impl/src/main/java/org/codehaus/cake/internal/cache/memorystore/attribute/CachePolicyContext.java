package org.codehaus.cake.internal.cache.memorystore.attribute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.PolicyContext;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.internal.cache.policy.FakePolicyContext;
import org.codehaus.cake.internal.codegen.attribute.FieldDefinition;
import org.codehaus.cake.util.attribute.Attribute;

public class CachePolicyContext<K, V> implements PolicyContext<CacheEntry<K, V>>, Iterable<FieldDefinition> {

    final List<FieldDefinition> fields = new ArrayList<FieldDefinition>();
    private final LinkedHashSet<Attribute> attributes = new LinkedHashSet<Attribute>();

    private ReplacementPolicy<CacheEntry<K, V>> policy;
    private Class<? extends ReplacementPolicy> pol;

    boolean isInitialized;

    public LinkedHashSet<Attribute> getAllAttributes() {
        getPolicy(); //lazy initialize
        return attributes;
    }

    public boolean containsAttribute(Attribute a) {
        getPolicy(); //lazy initialize
        return attributes.contains(a);
    }

    public CachePolicyContext(CacheConfiguration<K, V> configuration) {
        // First add attributes that has already been configured by the user
        for (Attribute<?> a : configuration.getAllEntryAttributes()) {
            CacheFieldDefinition<?> conf = CacheFieldDefinition.getPredefinedConfiguration(a);
            fields.add(conf);
            attributes.add(a);
        }
        pol = configuration.withMemoryStore().getPolicy();
    }

    public synchronized ReplacementPolicy<CacheEntry<K, V>> getPolicy() {
        if (policy == null && pol != null) {
            this.policy = pol == null ? null : Policies.create(pol, this);
            pol = null;
        }
        isInitialized = true;
        return policy;
    }

    void checkNotInitialized() {
        if (isInitialized) {
            throw new IllegalStateException();
        }
    }

    public synchronized void dependHard(Attribute<?> attribute) {
        checkNotInitialized();
        if (!attributes.contains(attribute)) {
            throw new IllegalArgumentException(attribute
                    + " has not been added to the cache through CacheConfiguration.addAttribute()");
        }
    }

    public synchronized BooleanAttachment attachBoolean() {
        checkNotInitialized();
        return new FakePolicyContext<Void>(Void.class).attachBoolean();
    }

    public synchronized IntAttachment attachInt() {
        return attachInt(0);
    }

    public synchronized IntAttachment attachInt(int defaultValue) {
        checkNotInitialized();
        return new FakePolicyContext<Void>(Void.class).attachInt(defaultValue);
    }

    public synchronized <U> ObjectAttachment<U> attachObject(Class<U> type) {
        checkNotInitialized();
        return new FakePolicyContext<Void>(Void.class).attachObject(type);
    }

    public synchronized ObjectAttachment<CacheEntry<K, V>> attachSelfReference() {
        return (ObjectAttachment) attachObject(CacheEntry.class);
    }

    public synchronized void dependSoft(Attribute<?> attribute) {
        checkNotInitialized();
        if (!attributes.contains(attribute)) {
            CacheFieldDefinition<?> conf = CacheFieldDefinition.getPredefinedConfigurationSoft(attribute);
            fields.add(conf);
            attributes.add(attribute);
        }
        // softDependencies.add(attribute);
    }

    public Class<CacheEntry<K, V>> getElementType() {
        return (Class) CacheEntry.class;
    }

    public CacheEntry<K, V>[] newArray(int size) {
        return new CacheEntry[size];
    }

    public Iterator<FieldDefinition> iterator() {
        getPolicy(); //lazy initialize
        return fields.iterator();
    }

    public List<FieldDefinition> getFields() {
        getPolicy(); //lazy initialize
        return fields;
    }
}
