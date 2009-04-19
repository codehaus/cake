package org.codehaus.cake.internal.cache.memorystore.openadressing;

import org.codehaus.cake.util.attribute.AttributeMap;

public interface OpenAdressingEntryFactory<K, V> {
    OpenAdressingEntry<K, V> create(K key, int hash, V value, AttributeMap params);

    OpenAdressingEntry<K, V> update(K key, int hash, V value, AttributeMap params, OpenAdressingEntry<K, V> existing);

    void access(OpenAdressingEntry<K, V> entry);
}
