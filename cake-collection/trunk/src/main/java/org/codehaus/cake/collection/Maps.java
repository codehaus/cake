package org.codehaus.cake.collection;

import java.util.HashMap;
import java.util.Map;

public class Maps {

    public static <K, V> Map<K, V> from(K key, V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }
    
    public static <K, V> Map<K, V> from(K key1, V value1, K key2, V value2) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }
}
