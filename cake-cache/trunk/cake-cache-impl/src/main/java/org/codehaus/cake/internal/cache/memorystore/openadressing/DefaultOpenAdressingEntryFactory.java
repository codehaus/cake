package org.codehaus.cake.internal.cache.memorystore.openadressing;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition;
import org.codehaus.cake.internal.cache.memorystore.attribute.CachePolicyContext;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition.CreateAction;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition.ModifyAction;
import org.codehaus.cake.internal.codegen.attribute.FieldDefinition;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.attribute.ShortAttribute;

public class DefaultOpenAdressingEntryFactory<K, V> implements OpenAdressingEntryFactory<K, V> {

    private CachePolicyContext<K, V> context;

    private final boolean useHits;
    private final boolean useModTime;
    private Clock clock;

    public DefaultOpenAdressingEntryFactory(CachePolicyContext<K, V> context, Clock clock) {
        this.context = context;
        this.clock = clock;
        context.getPolicy();// Initialize
        useHits = context.containsAttribute(CacheEntry.HITS);
        useModTime = context.containsAttribute(CacheEntry.TIME_ACCESSED);
    }

    public void access(OpenAdressingEntry<K, V> entry) {
        if (useHits) {
            long hits = entry.get(CacheEntry.HITS);
            ((DefaultOpenAdressingEntry) entry).attributes.put(CacheEntry.HITS, hits + 1);
        }
        if (useModTime) {
            long timestamp = clock.timeOfDay();
            ((DefaultOpenAdressingEntry) entry).attributes.put(CacheEntry.TIME_ACCESSED, timestamp);
        }
    }

    public OpenAdressingEntry<K, V> create(K key, int hash, V value, AttributeMap params) {
        DefaultAttributeMap attributes = new DefaultAttributeMap();
        long timestamp = Long.MIN_VALUE;
        for (FieldDefinition f : context) {
            if (f instanceof CacheFieldDefinition) {
                CacheFieldDefinition cfc = (CacheFieldDefinition) f;
                if (cfc.isReadMapOnCreate() && params.contains(cfc.getAttribute())) {
                    attributes.put(cfc.getAttribute(), params.get(cfc.getAttribute()));
                } else if (cfc.getCreateAction() == CreateAction.TIMESTAMP) {
                    if (timestamp == Long.MIN_VALUE) {
                        timestamp = clock.timeOfDay();
                    }
                    attributes.put(cfc.getAttribute(), timestamp);
                } else if (cfc.getCreateAction() == CreateAction.DEFAULT) {
                    attributes.put(cfc.getAttribute(), cfc.getAttribute().getDefault());
                } else if (cfc.getCreateAction() == CreateAction.SET_VALUE) {
                    attributes.put(cfc.getAttribute(), cfc.getInitialValue());
                }
            }
        }
        return new DefaultOpenAdressingEntry(key, hash, value, attributes);
    }

    public OpenAdressingEntry<K, V> update(K key, int hash, V value, AttributeMap params,
            OpenAdressingEntry<K, V> existing) {
        DefaultAttributeMap attributes = new DefaultAttributeMap();
        for (FieldDefinition f : context) {
            if (f instanceof CacheFieldDefinition) {
                CacheFieldDefinition cfc = (CacheFieldDefinition) f;
                if (cfc.isReadMapOnModify() && params.contains(cfc.getAttribute())) {
                    attributes.put(cfc.getAttribute(), params.get(cfc.getAttribute()));
                } else if (cfc.getModifyAction() == ModifyAction.TIMESTAMP) {
                    attributes.put(cfc.getAttribute(), clock.timeOfDay());
                } else if (cfc.getModifyAction() == ModifyAction.DEFAULT) {
                    attributes.put(cfc.getAttribute(), cfc.getAttribute().getDefault());
                } else if (cfc.getModifyAction() == ModifyAction.INCREMENT) {
                    long inc = existing.get((LongAttribute) cfc.getAttribute()) + 1;
                    attributes.put(cfc.getAttribute(), inc);
                } else if (cfc.getModifyAction() == ModifyAction.KEEP_EXISTING) {
                    attributes.put(cfc.getAttribute(), existing.get(cfc.getAttribute()));
                }
            }
        }
        return new DefaultOpenAdressingEntry(key, hash, value, attributes);
    }

    public static class DefaultOpenAdressingEntry<K, V> extends OpenAdressingEntry<K, V> {

        final MutableAttributeMap attributes;

        public long ff;
        public long hits;

        public DefaultOpenAdressingEntry(K key, int hash, V value, AttributeMap attributes) {
            this(key, hash, value, new DefaultAttributeMap(attributes));
        }

        public DefaultOpenAdressingEntry(K key, int hash, V value, String goo) {
            this(key, hash, value, new DefaultAttributeMap());
        }

        DefaultOpenAdressingEntry(K key, int hash, V value, MutableAttributeMap attributes) {
            super(key, hash, value);
            this.attributes = attributes;
        }

        public MutableAttributeMap getAttributes() {
            return attributes;
        }

        public <T> T get(Attribute<T> attribute, T defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public <T> T get(Attribute<T> attribute) {
            return attributes.get(attribute);
        }

        public boolean get(BooleanAttribute attribute, boolean defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public boolean get(BooleanAttribute attribute) {
            return attributes.get(attribute);
        }

        public byte get(ByteAttribute attribute, byte defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public byte get(ByteAttribute attribute) {
            return attributes.get(attribute);
        }

        public char get(CharAttribute attribute, char defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public char get(CharAttribute attribute) {
            return attributes.get(attribute);
        }

        public double get(DoubleAttribute attribute, double defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public double get(DoubleAttribute attribute) {
            return attributes.get(attribute);
        }

        public float get(FloatAttribute attribute, float defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public float get(FloatAttribute attribute) {
            return attributes.get(attribute);
        }

        public int get(IntAttribute attribute, int defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public int get(IntAttribute attribute) {
            return attributes.get(attribute);
        }

        public long get(LongAttribute attribute, long defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public long get(LongAttribute attribute) {
            return attributes.get(attribute);
        }

        public short get(ShortAttribute attribute, short defaultValue) {
            return attributes.get(attribute, defaultValue);
        }

        public short get(ShortAttribute attribute) {
            return attributes.get(attribute);
        }

        public boolean contains(Attribute<?> attribute) {
            return attributes.contains(attribute);
        }

        public int size() {
            return attributes.size();
        }

        public Set<Attribute> attributes() {
            return attributes.attributes();
        }

        public Set<Entry<Attribute, Object>> entrySet() {
            return attributes.entrySet();
        }

        public boolean isEmpty() {
            return attributes.isEmpty();
        }

        public Collection<Object> values() {
            return attributes.values();
        }
    }
}
