package org.codehaus.cake.cache.test.tck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.cache.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.service.exceptionhandling.CacheTestExceptionHandler;
import org.codehaus.cake.cache.test.service.loading.TestLoader;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;

public class AbstractBaseCacheTCKTest extends
        AbstractTCKTest<Cache<Integer, String>, CacheConfiguration<Integer, String>> {
    public static final Map.Entry<Integer, String> M1 = CollectionTestUtil.M1;

    public static final Map.Entry<Integer, String> M2 = CollectionTestUtil.M2;

    public static final Map.Entry<Integer, String> M3 = CollectionTestUtil.M3;

    public static final Map.Entry<Integer, String> M4 = CollectionTestUtil.M4;

    public static final Map.Entry<Integer, String> M5 = CollectionTestUtil.M5;

    public static final Map.Entry<Integer, String> M6 = CollectionTestUtil.M6;

    public static final Map.Entry<Integer, String> M7 = CollectionTestUtil.M7;
    public static final Map.Entry<Integer, String> M8 = CollectionTestUtil.M8;
    public static final Map.Entry<Integer, String> M9 = CollectionTestUtil.M9;
    protected TestLoader loader;
    protected CacheTestExceptionHandler<Integer, String> exceptionHandler;

    // @After
    public void noExceptions() {
        if (exceptionHandler != null) {
            exceptionHandler.assertCleared();
        }
    }

    protected <T> T getAttribute(Map.Entry<Integer, String> entry, Attribute<T> atr) {
        return c.peekEntry(entry.getKey()).get(atr);
    }

    protected <T> T getAttribute(CacheEntry<Integer, String> entry, Attribute<T> atr) {
        return entry.get(atr);
    }

    public void assertLoadCount(int count) {
        assertEquals(count, loader.totalLoads());
    }

    @Override
    protected CacheConfiguration<Integer, String> newConfiguration() {
        super.newConfiguration();
        loader = new TestLoader();
        exceptionHandler = new CacheTestExceptionHandler<Integer, String>();
        loader.add(M1, M2, M3, M4, M5);
        conf.withLoading().setLoader(loader);
        conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
        return conf;
    }

    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }

    public static <K, V> Map<K, V> asAnyMap(K k1, V v1) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> asAnyMap(K k1, V v1, K k2, V v2) {
        Map<K, V> map = asAnyMap(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> asAnyMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = asAnyMap(k1, v1, k2, v2);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> asAnyMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = asAnyMap(k1, v1, k2, v2, k3, v3);
        map.put(k2, v2);
        return map;
    }

    public <T> T asDummy(Class<T> dummyClass) {
        return TestUtil.dummy(dummyClass);
    }

    public AbstractBaseCacheTCKTest assertNotStarted() {
        assertFalse(c.isStarted());
        return this;
    }

    protected void assertPeek(Map.Entry<Integer, String>... entries) {
        for (Map.Entry<Integer, String> entry : entries) {
            assertEquals(entry.getValue(), c.peek(entry.getKey()));
        }
    }

    public AbstractBaseCacheTCKTest assertSize(int size) {
        assertEquals(size, c.size());
        assertEquals(size, withMemoryStore().getSize());
        if (size == 0) {
            assertTrue(c.isEmpty());
            assertEquals(0, withMemoryStore().getVolume());
        } else {
            assertFalse(c.isEmpty());
        }
        return this;
    }

    public AbstractBaseCacheTCKTest assertVolume(long volume) {
        assertEquals(volume, withMemoryStore().getVolume());
        return this;
    }

    protected final MemoryStoreService<Integer, String> withMemoryStore() {
        return c.getService(MemoryStoreService.class);
    }

    public AbstractBaseCacheTCKTest assertValidSizeAndVolume() {
        assertTrue("[volume =" + withMemoryStore().getVolume() + ", maximum volume="
                + withMemoryStore().getMaximumVolume() + "]", withMemoryStore().getVolume() <= withMemoryStore()
                .getMaximumVolume());
        assertTrue(withMemoryStore().getSize() <= withMemoryStore().getMaximumSize());
        assertTrue(c.size() <= withMemoryStore().getMaximumSize());
        return this;
    }

    public AbstractBaseCacheTCKTest assertStarted() {
        assertTrue(c.isStarted());
        return this;
    }

    @Override
    public AbstractBaseCacheTCKTest awaitTermination() {
        super.awaitTermination();
        return this;
    }

    protected Map.Entry<Integer, String> entry(Integer i, String s) {
        Map<Integer, String> m = new HashMap<Integer, String>();
        m.put(i, s);
        return m.entrySet().iterator().next();
    }

    protected Map.Entry<Integer, String> entry(Map.Entry<Integer, String> i, String s) {
        return entry(i.getKey(), s);
    }

    protected AbstractBaseCacheTCKTest init() {
        newContainer();
        return this;
    }

    protected AbstractBaseCacheTCKTest init(int entries) {
        c = newCache(entries);
        if (entries > 0) {
            assertStarted();
        } else {
            assertNotStarted();
        }
        return this;
    }

    public Cache<Integer, String> newCache() {
        return newContainer();
    }

    public Cache<Integer, String> newCache(int entries) {
        Cache<Integer, String> c = newContainer();
        if (entries > 0) {
            c.putAll(createMap(entries));
        }
        return c;
    }

    protected String peek(Integer e) {
        return c.peek(e);
    }

    protected String peek(Map.Entry<Integer, String> e) {
        return c.peek(e.getKey());
    }

    public AbstractBaseCacheTCKTest shutdown() {
        c.shutdown();
        return this;
    }

    public static Map<Integer, String> createMap(int entries) {
        if (entries < 0 || entries > 26) {
            throw new IllegalArgumentException();
        }
        Map<Integer, String> map = new HashMap<Integer, String>(entries);
        for (int i = 1; i <= entries; i++) {
            map.put(i, "" + (char) (i + 64));
        }

        return map;
    }

    public void assertLoads(int count) {
        assertEquals(count, loader.totalLoads());
    }
}
