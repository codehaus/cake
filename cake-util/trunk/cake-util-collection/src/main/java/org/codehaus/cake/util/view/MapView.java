package org.codehaus.cake.util.view;

import java.util.List;
import java.util.Map;

import org.codehaus.cake.util.ops.Ops.BinaryProcedure;

/**
 * A view of key value mappings.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 239 2008-12-23 20:29:21Z kasper $
 * @param <T>
 *            the type of elements
 */
public interface MapView<K, V> {

    /**
     * Applies the specified procedure to the keys and values in this view.
     * 
     * @param procedure
     *            the procedure to apply
     */
    void apply(BinaryProcedure<? super K, ? super V> procedure);

    /**
     * Returns a view a of all entries in this view as a {@link Map.Entry}.
     * <p>
     * The following example extract all entries in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;Map.Entry&lt;K, V&gt;&gt; list = mapview.entries().toList();
     * </pre>
     * 
     * @return a view of the entries in this view.
     */
    View<Map.Entry<K, V>> entries();

    /**
     * Creates a new view with all the keys in this view {@link Map.Entry#getKey()}. The following example extract all
     * keys in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;K&gt; list = mapview.keys().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<K> keys();

    /**
     * Returns the number of elements in this view. If this view contains more than <tt>Long.MAX_VALUE</tt> elements,
     * returns <tt>Long.MAX_VALUE</tt>.
     * 
     * @return the number of elements in this view
     */
    long size();

    /**
     * Creates a new {@link Map} with all the entries in this view.
     * <p>
     * The returned map will be "safe" in that no references to it or any of its elements are maintained by this view.
     * (In other words, this method must allocate a new map even if this view is backed by a map).
     * 
     * @return the new map
     */
    Map<K, V> toMap();

    /**
     * Creates a new view with all the values in this view {@link Map.Entry#getValue()}. The following example extract all
     * values in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;V&gt; list = mapview.values().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<V> values();
}
