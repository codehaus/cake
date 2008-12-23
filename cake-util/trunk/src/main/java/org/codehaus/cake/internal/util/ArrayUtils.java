/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This class contains various methods for manipulating arrays.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class ArrayUtils {

    // /CLOVER:OFF
    /** Cannot instantiate. */
    private ArrayUtils() {}

    // /CLOVER:ON
    /**
     * Copies the specified array.
     * 
     * @param <T>
     *            the type of the specified array
     * @param original
     *            the array to be copied
     * @return a copy of the original array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original) {
        return (T[]) copyOf(original, original.length, original.getClass());
    }

    /**
     * Copies the specified array, truncating or padding with nulls (if necessary) so the copy has the specified length.
     * 
     * @param <T>
     *            the type of the specified array
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls to obtain the specified length
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    /**
     * Copies the specified array, truncating or padding with nulls (if necessary) so the copy has the specified length.
     * For all indices that are
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @param newType
     *            the class of the copy to be returned
     * @param <U>
     *            the type of the specified array
     * @param <T>
     *            the type of the resulting array
     * @return a copy of the original array, truncated or padded with nulls to obtain the specified length
     */
    @SuppressWarnings("cast")
    public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        T[] copy = (Object) newType == (Object) Object[].class ? (T[]) new Object[newLength] : (T[]) Array.newInstance(
                newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Reverses the specified array.
     * 
     * @param <T>
     *            the
     * @param array
     *            the array to reverse
     * @return the reversed array
     */
    public static <T> T[] reverse(T[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }
    
    public static <T> T[] sort(T[] entries, Comparator<? super T> comparator, int max) {
        if (max >= entries.length) {
            Arrays.sort(entries, comparator);
            return entries;
        }
        T[] queue = (T[]) java.lang.reflect.Array.newInstance(entries.getClass().getComponentType(), max);
        queue[0] = entries[0];
        // siftUp
        for (int i = 1; i < max; i++) {
            T x = entries[i];
            int k = i;
            while (k > 0) {
                int parent = (k - 1) >>> 1;
                T e = queue[parent];
                if (comparator.compare(x, e) <= 0)
                    break;
                queue[k] = e;
                k = parent;
            }
            queue[k] = x;
        }
        // siftDown
        int size = queue.length;
        for (int i = max; i < entries.length; i++) {
            T x = entries[i];
            if (comparator.compare(queue[0], x) > 0) {
                siftDownUsingComparator(size, comparator, queue, x);
            }
        }
        //Sort remaining entrie
        for (int i = size - 1; i >= 0; i--) {
            T x = queue[i];
            queue[i] = queue[0];
            siftDownUsingComparator(i, comparator, queue, x);
        }

        return queue;
    }

    private static <T> void siftDownUsingComparator(int size, Comparator<? super T> comparator, T[] queue, T x) {
        int k = 0;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            T c = queue[child];
            int right = child + 1;
            if (right < size && comparator.compare(c, queue[right]) < 0)
                c = queue[child = right];
            if (comparator.compare(x, c) >= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

}
