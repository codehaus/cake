package org.codehaus.cake.internal.cache.memorystore.openadressing;

import org.codehaus.cake.util.attribute.AttributeMap;

public interface OpenAdressingEntryFactory<K, V> {
    OpenAdressingEntry<K, V> create(K key, V value, AttributeMap params);

    OpenAdressingEntry<K, V> update(K key, V value, AttributeMap params, OpenAdressingEntry<K, V> existing);

    void initialize();

    void access(OpenAdressingEntry<K, V> entry);
}
