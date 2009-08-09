package org.codehaus.cake.cache.policy;

import org.codehaus.cake.util.attribute.Attribute;

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

    /**
     * Creates a new boolean attachement. That can be used to attach a boolean to the data maintained by the cache.
     * 
     * @return the boolean attachment
     */
    BooleanAttachment attachBoolean();

    /**
     * Creates a new int attachement.
     * 
     * @return the int attachment
     */
    IntAttachment attachInt();

    IntAttachment attachInt(int defaultValue);

    <U> ObjectAttachment<U> attachObject(Class<U> type);

    ObjectAttachment<T> attachSelfReference();

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link org.codehaus.cake.cache.CacheAttributeConfiguration#add(Attribute...)} the cache will fail to startup. For example, it does not
     * make sense to use {@link ReplaceCostliestPolicy} if the user does not supply entries a {@link org.codehaus.cake.cache.CacheEntry#COST}
     * attached.
     * 
     * @param attribute
     *            the attribute to attach
     */
    void dependHard(Attribute<?> attribute);

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

    /** @return the type of data maintained by cache */
    Class<T> getElementType();

    /**
     * Creates a new array of the type of data maintained by the cache.
     * 
     * @param size
     *            the size of the array
     * @return the new array of the specified size
     */
    T[] newArray(int size);

    /** A boolean attachment. */
    interface BooleanAttachment {
        boolean get(Object element);

        void set(Object element, boolean value);
    }

    /** An int attachment. */
    interface IntAttachment {

        /**
         * Returns the integer attachment for the specified element, or the specified default value if no information
         * has been attached.
         * 
         * @param entry
         * @return
         */
        int get(Object element);

        void set(Object element, int value);
    }

    /** An Object attachement */
    interface ObjectAttachment<T> {
        T get(Object element);

        void set(Object element, T value);
    }
}
