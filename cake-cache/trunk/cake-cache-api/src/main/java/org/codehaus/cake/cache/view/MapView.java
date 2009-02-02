package org.codehaus.cake.cache.view;

import java.util.List;
import java.util.Map;

import org.codehaus.cake.ops.Ops.BinaryProcedure;

public interface MapView<K, V> {

    /**
     * Applies the specified procedure to the keys and values in this view.
     * 
     * @param procedure
     *            the procedure to apply
     */
    void apply(BinaryProcedure<? super K, ? super V> procedure);

    /**
     * Creates a new view with all entries in this view. Is primarily used for extracting data from the view. The
     * following example extract all entries in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;Map.Entry&lt;K, V&gt;&gt; list = mapview.entries().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<Map.Entry<K, V>> entries();

    /**
     * Creates a new view with the key for each entry {@link Map.Entry#getKey()}. The following example extract all
     * keys in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;K&gt; list = mapview.keys().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<K> keys();

    /** @return the number of mappings in this view */
    long size();

    /**
     * Creates a new {@link Map} with all the mappings in this view applying any mapping, filtering and ordering.
     * <p>
     * The returned map will be "safe" in that no references to it or any of its elements are maintained by this view.
     * (In other words, this method must allocate a new map even if this view is backed by a map).
     * 
     * @return the new map
     */
    Map<K, V> toMap();

    /**
     * Creates a new view with the value for each entry {@link Map.Entry#getValue()}. The following example extract all
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
