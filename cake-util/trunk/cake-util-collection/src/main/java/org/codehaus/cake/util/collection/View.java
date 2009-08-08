/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.util.collection;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Procedure;
import org.codehaus.cake.util.ops.Ops.Reducer;

/**
 * A view of objects.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 239 2008-12-23 20:29:21Z kasper $
 * @param <T>
 *            the type of elements
 */
public interface View<T> {

    /**
     * Returns any element from this view, or the specified value if this view contains no elements.
     * <p>
     * The element returned by this method is not guaranteed to be a random selection among all the elements in this
     * view. Most likely it will be the first element in this view.
     * 
     * @param base
     *            the value to return for an empty view
     * @return an element from this view, or the specified value if this view contains no elements.
     */
    T any(T base);

    /**
     * Applies the specified procedure to all the elements in this view.
     * <p>
     * The specified procedure must be safe for concurrent usage by multiple threads.
     * 
     * @param procedure
     *            the procedure to apply
     * @throws NullPointerException
     *             if the specified procedure is <tt>null</tt>
     */
    void apply(Procedure<? super T> procedure);

    /** @return <tt>true</tt> if this view contains no elements, otherwise <tt>false</tt> */
    boolean isEmpty();// OK

    /**
     * Returns a new view where all elements of this view is mapped using the specified mapper.
     * <p>
     * Suppose <tt>v</tt> is a <tt>View</tt> known to contain only strings. The following code creates a new (virtual)
     * view that contains the upper cased version for each element.
     * 
     * <pre>
     * View&lt;String&gt; x = v.map(new Op&lt;String, String&gt;() {
     *     public String op(String a) {
     *         return a.toUpperCase();
     *     }
     * });
     * </pre>
     * 
     * @param mapper
     *            the mapper
     * @return the new view
     * @throws NullPointerException
     *             if the specified mapper is <tt>null</tt>
     */
    <E> View<E> map(Op<? super T, ? extends E> mapper);

    /**
     * Returns the maximum element, or null if empty, assuming that all elements are Comparable instances.
     * 
     * @return maximum element, or null if empty
     * @throws ClassCastException
     *             if any element is not Comparable
     */
    T max();

    /**
     * Returns the minimum element, or null if empty, assuming that all elements are Comparables
     * 
     * @return minimum element, or null if empty
     * @throws ClassCastException
     *             if any element is not Comparable
     */
    T min();

    /** @return a new view retaining all <tt>non-null</tt> values from this view */
    View<T> notNull();

    /**
     * Assuming this view only contains a single element, return that element. Otherwise throw an
     * {@link IllegalStateException}.
     * <p>
     * This is equivalent to;
     * 
     * <pre>
     * if (view.size() != 1) {
     *     throw new IllegalArgumentException(&quot;view does not contain exactly 1 element&quot;);
     * }
     * return view.toList().get(0);
     * </pre>
     * 
     * only atomically.
     * 
     * @throws IllegalStateException
     *             if the view does not contain exactly one element
     * @return the only element in this view
     */
    T one();

    /**
     * Assuming this view contains one or zero elements, return the single element or the specified value if the view is
     * empty. Otherwise throw an {@link IllegalStateException}
     * <p>
     * This is equivalent to
     * 
     * <pre>
     * if (view.size() &gt; 1) {
     *     throw new IllegalArgumentException(&quot;size is greater then 1&quot;);
     * }
     * return view.any(defaultValue);
     * </pre>
     * 
     * only atomically
     * 
     * @param base
     *            the result for a view containing no elements
     * @return the only element in this view, or the specified base if this view contains no elements
     * @throws IllegalStateException
     *             if the view contains more then one element
     */
    T one(T base);

    /**
     * Creates a new view where all elements are ordered accordingly to the specified comparator.
     * 
     * @param comparator
     *            the comparator used for ordering elements
     * @throws NullPointerException
     *             if the specified comparator is null
     * @return the new view
     */
    View<T> orderBy(Comparator<? super T> comparator);

    /**
     * Assuming all elements are Comparable, creates a new view where all values are ordered accordingly to their
     * natural order. This ordering does not guarantee that elements with equal keys maintain their relative position.
     * 
     * @return the new view
     */
    View<T> orderByMax();

    /**
     * Sorts the specified array of objects into ascending order, according to the <i>natural ordering</i> of its
     * elements. All elements in the array must implement the <tt>Comparable</tt> interface. Furthermore, all elements
     * in the array must be <i>mutually comparable</i> (that is, <tt>e1.compareTo(e2)</tt> must not throw a
     * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt> in the array).
     * <p>
     * 
     * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result of the sort.
     * <p>
     * 
     * The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low
     * sublist is less than the lowest element in the high sublist). This algorithm offers guaranteed n*log(n)
     * performance.
     * 
     * @param a
     *            the array to be sorted.
     * @throws ClassCastException
     *             if the array contains elements that are not <i>mutually comparable</i> (for example, strings and
     *             integers).
     * @see Comparable
     */

    /**
     * Assuming all elements are Comparable, creates a new View where all values are ordered accordingly to their
     * reverse natural order. Furthermore, all elements in the view must be <i>mutually comparable</i> (that is,
     * <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt> for any elements <tt>e1</tt> and
     * <tt>e2</tt> in the view).This ordering does not guarantee that elements with equal keys maintain their relative
     * position.
     * <p>
     * This method does not check that all elements are Comparable. However, subsekvent invocations on the returned view
     * may fail with {@link ClassCastException} if not all elements are Comparable.
     * <p>
     * 
     * @return the new view
     */
    View<T> orderByMin();

    /**
     * Returns reduction of elements.
     * <p>
     * Usage: Considering a view of {@link BigDecimal}, the following example computes the sum of all numbers in the
     * view, or returns <tt>BigDecimal.ZERO</tt> if the view is empty :
     * 
     * <pre>
     * View&lt;BigDecimal&gt; numbers = ...;
     * BigDecimal sum = numbers.reduce(new Reducer&lt;BigDecimal&gt;() {
     *     public BigDecimal op(BigDecimal a, BigDecimal b) {
     *         return a.add(b);
     *     }
     * }, BigDecimal.ZERO);
     * </pre>
     * 
     * @param reducer
     *            the reducer
     * @param base
     *            the result for an empty view
     * @return reduction of elements in this view
     * @throws NullPointerException
     *             if the specified reducer is <tt>null</tt>
     */
    T reduce(Reducer<T> reducer, T base);

    /**
     * Creates a new view retaining no more then the specified amount of elements.
     * <p>
     * If this view has been ordered, the elements retained will be the top xx number of elements, and for the new view
     * will maintain this ordering.
     * <p>
     * If this view has not been ordered the new view will most likely contain the first <tt>n</tt> elements in this
     * view (matching any filter constraints). However no such guarantee can be made.
     * <p>
     * Usage: Returning a list of the 10 highest numbers in a view containing only integers:
     * 
     * <pre>
     * View&lt;Integer&gt; v=...
     * List&lt;Integer&gt; list = v.orderByMax().setLimit(10).toList();
     * </pre>
     * 
     * @param limit
     *            the maximum number of elements in the new view
     * @throws IllegalArgumentException
     *             if the limit is a non positive number (<=0)
     * @return the new view
     */
    View<T> setLimit(long limit);

    /**
     * Returns the number of elements in this view. If this view contains more than <tt>Long.MAX_VALUE</tt> elements,
     * returns <tt>Long.MAX_VALUE</tt>.
     * 
     * @return the number of elements in this view
     */
    long size();

    /**
     * Returns an array containing all of the elements in this view. If this view is ordered, the returned array will
     * maintain this ordering. Returning {@link #max()} as element with <tt>index 0</tt> in the array and {@link #min()}
     * as element with <tt>index size-1</tt> in the array.
     * <p>
     * The returned array will be "safe" in that no references to it are maintained by this view. (In other words, this
     * method must allocate a new array even if this view is somehow backed by an array). The caller is thus free to
     * modify the returned array.
     * 
     * @return an array containing all of the elements in this view
     */
    Object[] toArray();

    /**
     * Returns an array containing all of the elements in this view; the runtime type of the returned array is that of
     * the specified array. If the view fits in the specified array, it is returned therein. Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of this view.
     * <p>
     * If this view fits in the specified array with room to spare (i.e., the array has more elements than this view),
     * the element in the array immediately following the end of the collection is set to <tt>null</tt>. This is useful
     * in determining the length of this view <i>only</i> if the caller knows that this view does not contain any
     * <tt>null</tt> elements.)
     * <p>
     * If this view is ordered, the returned array will maintain this ordering. Returning {@link #max()} as element with
     * <tt>index 0</tt> in the array and {@link #min()} as element with <tt>index size-1</tt> in the array.
     * <p>
     * <b>Sample usage.</b> Suppose <tt>v</tt> is a <tt>View</tt> known to contain only strings. The following code can
     * be used to dump the view into a newly allocated array of Strings:
     * 
     * <pre>
     * View&lt;String&gt; v = ...
     * String[] x = v.toArray(new String[0]);
     * </pre>
     * 
     * <p>
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to <tt>toArray()</tt>.
     * 
     * @param a
     *            the array into which the elements of this view are to be stored, if it is big enough; otherwise, a new
     *            array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of this view
     * @throws ArrayStoreException
     *             the runtime type of the specified array is not a supertype of the runtime type of every element in
     *             this view
     * @throws NullPointerException
     *             if the specified array is <tt>null</tt>
     */
    <E> E[] toArray(E[] a); // OK

    /**
     * Creates a new {@link List} with all the elements in this view. If this view is ordered, the returned list will
     * maintain this ordering. Returning {@link #max()} as element with <tt>index 0</tt> in the list and {@link #min()}
     * as element with <tt>index size-1</tt> in the list.
     * <p>
     * The returned list will be "safe" in that no references to it or any of its elements are maintained by this view.
     * (In other words, this method must allocate a new list even if this view is somehow backed by a list).
     * 
     * @return a list containing all the elements in this view
     */
    List<T> toList();
}
