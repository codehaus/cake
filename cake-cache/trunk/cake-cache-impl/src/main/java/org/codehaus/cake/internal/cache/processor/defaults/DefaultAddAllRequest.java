package org.codehaus.cake.internal.cache.processor.defaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;

public class DefaultAddAllRequest<K, V> implements AddEntriesRequest<K, V> {

    final Collection<AddEntryRequest<K, V>> adds;

    public DefaultAddAllRequest(Map<? extends K, ? extends V> map, AttributeMap attributes) {
        if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        adds = new ArrayList<AddEntryRequest<K, V>>();
        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
            adds.add(new DefaultCreateUpdateRequest<K, V>(e.getKey(), new DefaultAttributeMap(), e.getValue(), null,
                    null, null));
        }
    }

    public Collection<AddEntryRequest<K, V>> adds() {
        return adds;
    }

    public boolean doTrim() {
        // TODO Auto-generated method stub
        return false;
    }

    private List<CacheEntry<K, V>> trimmed;

    public List<CacheEntry<K, V>> getTrimmed() {
        return trimmed;
    }

    public void setTrimmed(List<CacheEntry<K, V>> trimmed) {
        this.trimmed = trimmed;
    }

}
