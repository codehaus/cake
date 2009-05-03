package org.codehaus.cake.cache.test.tck.asmap;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.cache.test.tck.AbstractBaseCacheTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;

public class AbstractAsMapTCKTest extends AbstractBaseCacheTCKTest {

    protected ConcurrentMap<Integer, String> asMap() {
        return c.asMap();
    }

    protected void clear() {
        asMap().clear();
    }

    public boolean isEmpty() {
        return asMap().isEmpty();
    }

    @Override
    public AbstractBaseCacheTCKTest assertSize(int size) {
        assertEquals(size, asMap().size());
        return super.assertSize(size);
    }

    public Set<Entry<Integer, String>> entrySet() {
        return asMap().entrySet();
    }

    protected void assertIsEmpty() {
        assertTrue(isEmpty());
    }

    protected void assertIsNotEmpty() {
        assertFalse(isEmpty());
    }

    protected void putAll(Map.Entry... entries) {
        c.putAll(CollectionTestUtil.asMap_(entries));
    }

    protected String put(Integer key, String value) {
        return asMap().put(key, value);
    }

    protected String remove(Integer i) {
        return asMap().remove(i);
    }

    protected boolean remove(Integer key, String value) {
        return asMap().remove(key, value);
    }

    protected String remove(Map.Entry<Integer, String> e) {
        return asMap().remove(e.getKey());
    }

    protected String get(Map.Entry<Integer, String> e) {
        return asMap().get(e.getKey());
    }

    public void assertGet(Map.Entry<Integer, String> e) {
        assertEquals(e.getValue(), get(e));
    }

    public void assertGet(Map.Entry<Integer, String> e, String value) {
        assertEquals(value, get(e));
    }

    public boolean containsKey(Object value) {
        return asMap().containsKey(value);
    }

    public String replace(Integer key, String value) {
        return asMap().replace(key, value);
    }

    public String putIfAbsent(Integer key, String value) {
        return asMap().putIfAbsent(key, value);
    }
    
    public boolean replace(Integer key, String oldValue, String newValue) {
        return asMap().replace(key, oldValue, newValue);
    }

    public boolean containsValue(Object value) {
        return asMap().containsValue(value);
    }

    public void assertContainsKey(Map.Entry<Integer, String> key) {
        assertTrue(asMap().containsKey(key.getKey()));
    }

    public void assertContainsValue(Map.Entry<Integer, String> value) {
        assertTrue(asMap().containsValue(value.getValue()));
    }

    public void assertNotContainsKey(Map.Entry<Integer, String> key) {
        assertFalse(c.containsKey(key.getKey()));
    }

    public void assertNotContainsValue(Map.Entry<Integer, String> value) {
        assertFalse(c.containsValue(value.getValue()));
    }
}
