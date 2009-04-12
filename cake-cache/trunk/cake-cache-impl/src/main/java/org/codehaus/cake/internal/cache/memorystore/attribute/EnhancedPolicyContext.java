package org.codehaus.cake.internal.cache.memorystore.attribute;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.internal.codegen.attribute.FieldDefinition;

public class EnhancedPolicyContext<K, V> extends CachePolicyContext<K, V> {

    Map<Object, FieldDefinition> attachments = new HashMap<Object, FieldDefinition>();
    private int count;

    public EnhancedPolicyContext(CacheConfiguration<K, V> configuration) {
        super(configuration);
    }

    public EnhancedPolicyContext<K, V> updateAttribute(BooleanAttachment delegator, BooleanAttachment target) {
        ((MutableBooleanAttachment) delegator).delegateTo = target;
        return this;
    }

    public EnhancedPolicyContext<K, V> updateAttribute(IntAttachment delegator, IntAttachment target) {
        ((MutableIntAttachment) delegator).delegateTo = target;
        return this;
    }

    public EnhancedPolicyContext<K, V> updateAttribute(ObjectAttachment<?> delegator, ObjectAttachment<?> target) {
        ((MutableObjectAttachment) delegator).delegateTo = target;
        return this;
    }

    @Override
    public synchronized BooleanAttachment attachBoolean() {
        checkNotInitialized();
        BooleanAttachment a = new MutableBooleanAttachment();
        FieldDefinition fd = new FieldDefinition(Boolean.TYPE, "booleanAttachment" + count++);
        fields.add(fd);
        attachments.put(a, fd);
        return a;
    }

    @Override
    public synchronized IntAttachment attachInt(int initialValue) {
        checkNotInitialized();
        IntAttachment a = new MutableIntAttachment();
        FieldDefinition fd = new FieldDefinition(Boolean.TYPE, "booleanAttachment" + count++).setInitialValue(initialValue);
        fields.add(fd);
        attachments.put(a, fd);
        return a;
    }

    @Override
    public synchronized <U> ObjectAttachment<U> attachObject(Class<U> type) {
        checkNotInitialized();
        MutableObjectAttachment<U> a = new MutableObjectAttachment<U>();
        FieldDefinition fd = new FieldDefinition(type, "attachment" + count++);
        fields.add(fd);
        attachments.put(a, fd);
        return a;
    }

    public Map<Object, FieldDefinition> getAttachments() {
        return attachments;
    }

    static class MutableIntAttachment implements IntAttachment {
        IntAttachment delegateTo;

        public int get(Object element) {
            return delegateTo.get(element);
        }

        public void set(Object element, int value) {
            delegateTo.set(element, value);
        }
    }

    static class MutableBooleanAttachment implements BooleanAttachment {
        BooleanAttachment delegateTo;

        public boolean get(Object element) {
            return delegateTo.get(element);
        }

        public void set(Object element, boolean value) {
            delegateTo.set(element, value);
        }
    }

    static class MutableObjectAttachment<T> implements ObjectAttachment<T> {
        ObjectAttachment<T> delegateTo;

        public T get(Object element) {
            return delegateTo.get(element);
        }

        public void set(Object element, T value) {
            delegateTo.set(element, value);
        }
    }

}
