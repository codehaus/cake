/**
 * 
 */
package org.codehaus.cake.internal.cache.policy;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.cache.policy.PolicyContext;
import org.codehaus.cake.util.attribute.Attribute;

public class FakePolicyContext<T> implements PolicyContext<T> {

    private final Class<T> type;

    private List<Attribute> softAttachments = new LinkedList<Attribute>();

    public FakePolicyContext(Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        this.type = type;
    }

    public BooleanAttachment attachBoolean() {
        return new BA();
    }

    public IntAttachment attachInt() {
        return attachInt(0);
    }

    public IntAttachment attachInt(int defaultValue) {
        return new IA(defaultValue);
    }

    public <U> ObjectAttachment<U> attachObject(Class<U> type) {
        return new OA();
    }

    public ObjectAttachment<T> attachSelfReference() {
        return (ObjectAttachment) attachObject(Object.class);
    }

    public void dependHard(Attribute<?> attribute) {

    }

    public void dependSoft(Attribute<?> attribute) {
        softAttachments.add(attribute);
    }

    public List<Attribute> getSoftDependencies() {
        return softAttachments;
    }

    public Class getElementType() {
        return type;
    }

    public T[] newArray(int size) {
        return (T[]) java.lang.reflect.Array.newInstance(type, size);
    }

    static class BA implements BooleanAttachment {
        final WeakIdentityHashMap<Object, Boolean> map = new WeakIdentityHashMap<Object, Boolean>();

        public boolean get(Object entry) {
            Boolean b = map.get(entry);
            if (b == null) {
                throw new IllegalArgumentException("The value of this attachment has not been set previously");
            }
            return b.booleanValue();
        }

        public void set(Object entry, boolean value) {
            map.put(entry, Boolean.valueOf(value));
        }
    }

    static class IA implements IntAttachment {
        int defaultValue;
        final WeakIdentityHashMap<Object, Integer> map = new WeakIdentityHashMap<Object, Integer>();

        IA(int defaultValue) {
            this.defaultValue = defaultValue;
        }

        public int get(Object entry) {
            Integer i = map.get(entry);
            if (i == null) {
                return defaultValue;
            }
            return i.intValue();
        }

        public void set(Object entry, int value) {
            map.put(entry, Integer.valueOf(value));
        }
    }

    static class OA implements ObjectAttachment {
        final WeakIdentityHashMap<Object, Object> map = new WeakIdentityHashMap<Object, Object>();

        public Object get(Object entry) {
            return map.get(entry);
        }

        public void set(Object entry, Object value) {
            if (value == null) {
                map.remove(entry);
            } else
                map.put(entry, value);
        }
    }

    static class WeakIdentityHashMap<K, V> {
        private final ReferenceQueue<K> queue = new ReferenceQueue<K>();
        private Map<IdentityWeakReference, V> backingStore = new HashMap<IdentityWeakReference, V>();

        V get(Object key) {
            reap();
            return backingStore.get(new IdentityWeakReference(key));
        }

        V put(K key, V value) {
            reap();
            return backingStore.put(new IdentityWeakReference(key), value);
        }

        V remove(Object key) {
            reap();
            return backingStore.remove(new IdentityWeakReference(key));
        }

        private synchronized void reap() {
            Object zombie = queue.poll();
            while (zombie != null) {
                IdentityWeakReference victim = (IdentityWeakReference) zombie;
                backingStore.remove(victim);
                zombie = queue.poll();
            }
        }

        class IdentityWeakReference extends WeakReference<K> {
            int hash;

            @SuppressWarnings("unchecked")
            IdentityWeakReference(Object obj) {
                super((K) obj, queue);
                hash = System.identityHashCode(obj);
            }

            public int hashCode() {
                return hash;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                IdentityWeakReference ref = (IdentityWeakReference) o;
                if (this.get() == ref.get()) {
                    return true;
                }
                return false;
            }
        }
    }
}
