/**
 * 
 */
package org.codehaus.cake.internal.cache.policy;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.cake.cache.policy.PolicyContext;
import org.codehaus.cake.util.attribute.Attribute;

public class FakePolicyContext<T> implements PolicyContext<T> {

    private final Class<T> type;

    private List<Attribute> hardAttachments = new LinkedList<Attribute>();
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
        hardAttachments.add(attribute);
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
        final IdentityHashMap<Object, Boolean> map = new IdentityHashMap<Object, Boolean>();

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
        final IdentityHashMap<Object, Integer> map = new IdentityHashMap<Object, Integer>();

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
        final IdentityHashMap<Object, Object> map = new IdentityHashMap<Object, Object>();

        public Object get(Object entry) {
            return map.get(entry);
        }

        public void set(Object entry, Object value) {
            map.put(entry, value);
        }
    }
}
