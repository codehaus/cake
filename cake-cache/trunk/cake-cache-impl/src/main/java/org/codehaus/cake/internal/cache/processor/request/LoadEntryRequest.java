package org.codehaus.cake.internal.cache.processor.request;


public interface LoadEntryRequest<K, V> extends AddEntryRequest<K, V> {
    void setValue(V value);
}
