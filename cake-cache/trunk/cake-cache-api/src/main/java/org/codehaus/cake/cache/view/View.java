package org.codehaus.cake.cache.view;

import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.ops.Ops.Reducer;

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
     * Creates a new CacheQuery which limits the number of results returned by this query.
     * 
     * @param limit
     *            the maximum number of results to return
     * @return a new query
     */
    View<T> setLimit(long limit);

    /**
     * Creates a new view where all elements are ordered accordingly to the specified comparator.
     * 
     * @param comparator
     *            the comparator used for ordering
     * @throws NullPointerException
     *             if the specified comparator is null
     * @return the new view
     */
    View<T> orderBy(Comparator<? super T> comparator);

    /**
     * Assuming all elements are Comparable, creates a new View where all values are ordered accordingly to their
     * natural order. This ordering does not guarantee that elements with equal keys maintain their relative position. *
     * 
     * @return the new view
     */
    View<T> orderByMax();

    /**
     * Assuming all elements are Comparable, creates a new View where all values are ordered accordingly to their
     * natural order. This ordering does not guarantee that elements with equal keys maintain their relative position.
     * 
     * @return the new view
     */
    View<T> orderByMin();

    /**
     * Returns a new view where all elements of this view is mapped using the specified mapper.
     * <p>
     * Suppose <tt>v</tt> is a <tt>View</tt> known to contain only strings. The following code creates a new virtual
     * view that contains the upper case version for each element.
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
     * Returns any element matching filter constraints, or <code>null</code> if none.
     * 
     * @return an element, or null if none
     */
    T any();

    /**
     * Applies the specified procedure to the elements in this view.
     * 
     * @param procedure
     *            the procedure to apply
     */
    void apply(Procedure<? super T> procedure);

    /**
     * Returns the maximum element, or null if empty, assuming that all elements are Comparables
     * 
     * @return maximum element, or null if empty
     * @throws ClassCastException
     *             if any element is not Comparable
     */
    T max();

    /**
     * Returns the maximum element, or null if empty
     * 
     * @param comparator
     *            the comparator
     * @return maximum element, or null if empty
     * @throws NullPointerException
     *             if the specified comparator is <tt>null</tt>
     */
    T max(Comparator<? super T> comparator);

    /**
     * Returns the minimum element, or null if empty, assuming that all elements are Comparables
     * 
     * @return minimum element, or null if empty
     * @throws ClassCastException
     *             if any element is not Comparable
     */
    T min();

    /**
     * Returns the minimum element, or null if empty
     * 
     * @param comparator
     *            the comparator
     * @return minimum element, or null if empty
     * @throws NullPointerException
     *             if the specified comparator is <tt>null</tt>
     */
    T min(Comparator<? super T> comparator);

    /**
     * Returns reduction of elements.
     * 
     * @param reducer
     *            the reducer
     * @param base
     *            the result for an empty array
     * @return reduction
     */
    T reduce(Reducer<T> reducer, T base);

    /** @return the number of elements in this view */
    long size();

    /**
     * Returns an array containing all of the elements in this view. If the view makes any guarantees as to what order
     * its elements are returned by its iterator, this method must return the elements in the same order.
     * <p>
     * The returned array will be "safe" in that no references to it are maintained by this view. (In other words, this
     * method must allocate a new array even if this view is backed by an array). The caller is thus free to modify the
     * returned array.
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
     * the element in the array immediately following the end of the collection is set to <tt>null</tt>. This is
     * useful in determining the length of this view <i>only</i> if the caller knows that this view does not contain
     * any <tt>null</tt> elements.)
     * <p>
     * If this view makes any guarantees as to what order its elements are returned by its iterator, this method must
     * return the elements in the same order.
     * <p>
     * Suppose <tt>v</tt> is a <tt>View</tt> known to contain only strings. The following code can be used to dump
     * the view into a newly allocated array of <tt>String</tt>:
     * 
     * <pre>
     * String[] x = (String[]) v.toArray(new String[0]);
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
     *             this collection
     * @throws NullPointerException
     *             if the specified array is <tt>null</tt>
     */
    <E> E[] toArray(E[] a);

    /**
     * Creates a new {@link List} with all the elements in this view applying any mapping, filtering or ordering.
     * <p>
     * The returned list will be "safe" in that no references to it or any of its elements are maintained by this view.
     * (In other words, this method must allocate a new list even if this view is backed by an list).
     * 
     * @return the new list
     */
    List<T> toList();
}
