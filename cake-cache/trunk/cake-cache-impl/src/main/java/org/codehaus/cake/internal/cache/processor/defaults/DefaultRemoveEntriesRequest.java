package org.codehaus.cake.internal.cache.processor.defaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultRemoveEntriesRequest<K, V> implements RemoveEntriesRequest<K, V> {

    final Collection<RemoveEntryRequest<K, V>> removes;


    public DefaultRemoveEntriesRequest(Collection<? extends K> keys) {
        removes = new ArrayList<RemoveEntryRequest<K, V>>();
        for (K key : keys) {
            removes.add(new DefaultRemoveRequest<K, V>(key, (Predicate) null));
        }
    }

    public Collection<RemoveEntryRequest<K, V>> removes() {
        return removes;
    }

}
