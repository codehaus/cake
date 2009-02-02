package org.codehaus.cake.cache.policy.spi;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.costsize.ReplaceCostliestPolicy;

/**
 * A PolicyContext can be used by replacement policies to attach data to the elements of the cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AbstractArrayReplacementPolicy.java 225 2008-11-30 20:53:08Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface PolicyContext<T> {

    /** @return the type of data maintained by cache */
    Class getElementType();

    /**
     * Creates a new array of the specified
     * 
     * @param size
     * @return
     */
    T[] newArray(int size);

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link CacheAttributeConfiguration#add(Attribute...)} the attribute will be silently registered.
     * 
     * For example, LFU will depend on CacheEntry.HITS. This is a non-hard dependency because if the user has not
     * already defined it, it will just be added as a secret.
     * 
     * 
     * @param attribute
     *            the attribute to attach
     */
    void dependSoft(Attribute<?> attribute);

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link CacheAttributeConfiguration#add(Attribute...)} the cache will fail to startup. For example, it does not
     * make sense to use {@link ReplaceCostliestPolicy} if the user does not supply entries a {@link CacheEntry#COST}
     * attached.
     * 
     * @param attribute
     *            the attribute to attach
     */
    void dependHard(Attribute<?> attribute);

    ObjectAttachment<T> attachSelfReference();

    <U> ObjectAttachment<U> attachObject(Class<U> type);

    /**
     * Creates a new int attachement.
     * 
     * @return the int attachment
     */
    IntAttachment attachInt();

    /**
     * Creates a new boolean attachement.
     * 
     * @return the boolean attachment
     */
    BooleanAttachment attachBoolean();

    /** An Object attachement */
    interface ObjectAttachment<T> {
        T get(Object entry);

        void set(Object entry, T value);
    }

    /** An int attachment. */
    interface IntAttachment {
        int get(Object entry);

        int get(Object entry, int defaultValue);

        void set(Object entry, int value);
    }
    /** A boolean attachment. */
    interface BooleanAttachment {
        boolean get(Object entry);

        void set(Object entry, boolean value);
    }
}
