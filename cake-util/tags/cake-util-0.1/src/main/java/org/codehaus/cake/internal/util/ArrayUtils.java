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

/**
 * This class contains various methods for manipulating arrays.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CollectionUtils.java 546 2008-01-07 20:47:32Z kasper $
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
}
