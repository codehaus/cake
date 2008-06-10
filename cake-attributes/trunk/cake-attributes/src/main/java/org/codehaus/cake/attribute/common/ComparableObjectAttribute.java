package org.codehaus.cake.attribute.common;

import java.util.Comparator;

import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.attribute.WithAttributes;

/**
 * The attributes ordered according to their {@linkplain Comparable natural ordering}, or by a {@link Comparator}
 * provided at attribute construction time, depending on which constructor is used.
 * 
 * An attribute relying on natural ordering does not permit comparison of non-comparable objects (doing so may result in
 * {@code ClassCastException}).
 * 
 * @param <T>
 */
public class ComparableObjectAttribute<T> extends ObjectAttribute<T> implements Comparator<WithAttributes> {

    /**
     * The comparator, or null if attribute uses elements' natural ordering.
     */
    private final Comparator<? super T> comparator;
//
//    public ComparableObjectAttribute(Class<T> clazz) {
//        super(clazz);
//        comparator = null;
//    }
//
//    public ComparableObjectAttribute(Class<T> clazz, Comparator<? super T> comparator) {
//        super(clazz);
//        if (comparator == null) {
//            throw new NullPointerException("comparator is null");
//        }
//        this.comparator = comparator;
//    }
//
//    public ComparableObjectAttribute(Class<T> clazz, Comparator<? super T> comparator, T defaultValue) {
//        super(clazz, defaultValue);
//        if (comparator == null) {
//            throw new NullPointerException("comparator is null");
//        }
//        this.comparator = comparator;
//    }
//
//    public ComparableObjectAttribute(Class<T> clazz, T defaultValue) {
//        super(clazz, defaultValue);
//        comparator = null;
//    }

    public ComparableObjectAttribute(String name, Class<T> clazz) {
        super(name, clazz);
        comparator = null;
    }

    public ComparableObjectAttribute(String name, Class<T> clazz, Comparator<? super T> comparator) {
        super(name, clazz);
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        this.comparator = comparator;
    }

    public ComparableObjectAttribute(String name, Class<T> clazz, Comparator<? super T> comparator, T defaultValue) {
        super(name, clazz, defaultValue);
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        this.comparator = comparator;
    }

    public ComparableObjectAttribute(String name, Class<T> clazz, T defaultValue) {
        super(name, clazz, defaultValue);
        comparator = null;
    }

    public int compare(WithAttributes o1, WithAttributes o2) {
        if (comparator == null) {
            Comparable<? super T> thisVal = (Comparable<? super T>) get(o1);
            T anotherVal = (T) get(o2);
            return thisVal.compareTo(anotherVal);
        } else {
            T thisVal = get(o1);
            T anotherVal = get(o2);
            return comparator.compare(thisVal, anotherVal);
        }
    }
}
