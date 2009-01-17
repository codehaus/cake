package org.codehaus.cake.cache.policy.spi;


public interface PolicyAttachmentFactory {
    
    <T> ObjectAttachment<T> attachObject(Class<T> type);
    IntAttachment attachInt();
    BooleanAttachment attachBoolean();
    interface ObjectAttachment<T> {
        T get(Object entry);
        void set(Object entry, T value);
    }
    interface IntAttachment {
        int get(Object entry);
        void set(Object entry, int value);
    }
    interface BooleanAttachment {
        boolean get(Object entry);
        void set(Object entry, boolean value);
    }
}
