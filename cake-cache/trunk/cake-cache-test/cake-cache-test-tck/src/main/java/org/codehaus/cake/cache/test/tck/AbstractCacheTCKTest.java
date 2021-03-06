/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
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
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;

public class AbstractCacheTCKTest extends AbstractTCKTest<Cache<Integer, String>, CacheConfiguration<Integer, String>> {
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

    protected final CacheLoadingService<Integer, String> withLoading() {
        return c.getService(CacheLoadingService.class);
    }

    protected final CacheLoadingService<Integer, String> withLoadingForced() {
        return c.getService(CacheLoadingService.class, CacheLoadingService.IS_FORCED.singleton(true));
    }

    protected final MemoryStoreService<Integer, String> withMemoryStore() {
        return c.getService(MemoryStoreService.class);
    }

    protected String remove(Map.Entry<Integer, String> e) {
        return c.remove(e.getKey());
    }

    protected String replace(Integer key, String value) {
        return c.replace(key, value);
    }

    protected String putIfAbsent(Integer key, String value) {
        return c.putIfAbsent(key, value);
    }

    protected String putIfAbsent(Map.Entry<Integer, String> e) {
        return c.putIfAbsent(e.getKey(), e.getValue());
    }

    protected boolean replace(Integer key, String oldValue, String newValue) {
        return c.replace(key, oldValue, newValue);
    }

    protected String replace(Map.Entry<Integer, String> e) {
        return c.replace(e.getKey(), e.getValue());
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

    protected void assertState(State state) {
        assertEquals(state, c.getState());
    }
    protected Collection<Map.Entry<Integer, String>> put(int to) {
        return put(1, to);
    }

    protected Collection<Map.Entry<Integer, String>> putAll(int from, int to) {
        Map<Integer, String> m = new HashMap<Integer, String>();
        for (int i = from; i <= to; i++) {
            m.put(i, "" + (char) (i + 64));
        }
        c.putAll(m);
        return new ArrayList<Map.Entry<Integer, String>>(c.asMap().entrySet());
    }

    protected Collection<Map.Entry<Integer, String>> put(int from, int to) {
        for (int i = from; i <= to; i++) {
            c.put(i, "" + (char) (i + 64));
        }
        return new ArrayList<Map.Entry<Integer, String>>(c.asMap().entrySet());
    }
    protected void assertIsEmpty() {
        assertTrue(isEmpty());
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

    public AbstractCacheTCKTest assertContainsKey(Map.Entry key) {
        assertTrue(c.containsKey(key.getKey()));
        return this;
    }

    public AbstractCacheTCKTest assertContainsValue(Map.Entry value) {
        assertTrue(c.containsValue(value.getValue()));
        return this;
    }

    protected AbstractCacheTCKTest assertGet(Map.Entry<Integer, String>... entries) {
        for (Map.Entry<Integer, String> entry : entries) {
            assertEquals(entry.getValue(), c.get(entry.getKey()));
        }
        return this;
    }

    protected void assertGetAll(Map.Entry<Integer, String>... entries) {
        Collection<Integer> keys = new ArrayList<Integer>();
        for (Map.Entry<Integer, String> entry : entries) {
            keys.add(entry.getKey());
        }
        Map<Integer, String> map = c.getAllOld(keys);
        assertEquals(keys.size(), map.size());
        for (Map.Entry<Integer, String> entry : entries) {
            assertEquals(entry.getValue(), map.get(entry.getKey()));
        }
    }

    public AbstractCacheTCKTest assertNotContainsKey(Map.Entry key) {
        assertFalse(c.containsKey(key.getKey()));
        return this;
    }

    public AbstractCacheTCKTest assertNotContainsValue(Map.Entry value) {
        assertFalse(c.containsValue(value.getValue()));
        return this;
    }

    public AbstractCacheTCKTest assertNotStarted() {
        assertState(State.INITIALIZED);
        return this;
    }

    protected void assertPeek(Map.Entry<Integer, String>... entries) {
        for (Map.Entry<Integer, String> entry : entries) {
            assertEquals(entry.getValue(), c.peek(entry.getKey()));
        }
    }

    public AbstractCacheTCKTest assertSize(int size) {
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

    public AbstractCacheTCKTest assertVolume(long volume) {
        assertEquals(volume, withMemoryStore().getVolume());
        return this;
    }

    public AbstractCacheTCKTest assertValidSizeAndVolume() {
        assertTrue("[volume =" + withMemoryStore().getVolume() + ", maximum volume="
                + withMemoryStore().getMaximumVolume() + "]", withMemoryStore().getVolume() <= withMemoryStore()
                .getMaximumVolume());
        assertTrue(withMemoryStore().getSize() <= withMemoryStore().getMaximumSize());
        assertTrue(c.size() <= withMemoryStore().getMaximumSize());
        return this;
    }

    public AbstractCacheTCKTest assertStarted() {
        assertTrue(c.getState().isStarted());
        return this;
    }

    @Override
    public AbstractCacheTCKTest awaitTermination() {
        super.awaitTermination();
        return this;
    }

    public AbstractCacheTCKTest clear() {
        c.clear();
        return this;
    }

    public boolean containsKey(Object key) {
        return c.containsKey(key);
    }

    protected Map.Entry<Integer, String> entry(Integer i, String s) {
        Map<Integer, String> m = new HashMap<Integer, String>();
        m.put(i, s);
        return m.entrySet().iterator().next();
    }

    protected Map.Entry<Integer, String> entry(Map.Entry<Integer, String> i, String s) {
        return entry(i.getKey(), s);
    }

    protected AbstractCacheTCKTest init() {
        newContainer();
        return this;
    }

    protected AbstractCacheTCKTest init(int entries) {
        c = newCache(entries);
        return this;
    }

    public boolean isEmpty() {
        return c.isEmpty();
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

    protected String get(Map.Entry<Integer, String> e) {
        return c.get(e.getKey());
    }

    protected Map<Integer, String> get(Map.Entry<Integer, String>... entries) {
        HashMap<Integer, String> result = new HashMap<Integer, String>();
        for (Map.Entry<Integer, String> e : entries) {
            result.put(e.getKey(), c.get(e.getKey()));
        }
        return result;
    }

    protected Map<Integer, String> getAll(Map.Entry<Integer, String>... entries) {
        Collection<Integer> keys = new ArrayList<Integer>();
        for (Map.Entry<Integer, String> e : entries) {
            keys.add(e.getKey());
        }
        return c.getAllOld(keys);
    }

    protected CacheEntry<Integer, String> peekEntry(Map.Entry<Integer, String> e) {
        return c.peekEntry(e.getKey());
    }

    protected CacheEntry<Integer, String> getEntry(Map.Entry<Integer, String> e) {
        return c.getEntry(e.getKey());
    }

    protected void put(Map.Entry<Integer, String> entry) {
        c.put(entry.getKey(), entry.getValue());
    }

    public <T, S> void put(Map.Entry<Integer, String> e, Attribute<T> a1, T t) {
        c.withCrud().write().put(e.getKey(), e.getValue(), a1.singleton(t));
    }

    public <T, S> void put(Map.Entry<Integer, String> e, Attribute<T> a1, T t, Attribute<S> a2, S s) {
        AttributeMap ga = Attributes.from(a1, t, a2, s);
        c.withCrud().write().put(e.getKey(), e.getValue(), ga);

    }

    protected void put(Map.Entry<Integer, String>... entries) {
        for (Map.Entry<Integer, String> entry : entries) {
            c.put(entry.getKey(), entry.getValue());
        }
    }

    protected void putAll(Map.Entry<Integer, String>... entries) {
        c.putAll(CollectionTestUtil.asMap_(entries));
    }

    public AbstractCacheTCKTest shutdown() {
        c.shutdown();
        return this;
    }

    public long size() {
        return c.size();
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

    protected void forceLoadAll() {
        withLoadingForced().loadAll();
    }

    protected void forceLoad(Map.Entry<Integer, String> entry) {
        forceLoad(entry, null);
    }

    protected void forceLoad(Map.Entry<Integer, String> entry, AttributeMap map) {
        load(entry, map, true);
    }

    public AbstractCacheTCKTest load(Map.Entry<Integer, String> entry) {
        load(entry, null);
        return this;
    }

    protected void load(Map.Entry<Integer, String> entry, AttributeMap map) {
        load(entry, map, false);
    }

    protected void loadAll(Map.Entry<Integer, String>... entries) {
        loadAll(null, false, entries);
    }

    protected void loadAll(AttributeMap map, Map.Entry<Integer, String>... entries) {
        loadAll(map, false, entries);
    }

    protected void forceLoadAll(Map.Entry<Integer, String>... entries) {
        loadAll(null, true, entries);
    }

    protected void forceLoadAll(AttributeMap map, Map.Entry<Integer, String>... entries) {
        loadAll(map, true, entries);
    }

    private void loadAll(AttributeMap map, boolean force, Map.Entry<Integer, String>... iter) {
        Map<Integer, String> keys = new LinkedHashMap<Integer, String>();
        Map<Integer, String> prev = new LinkedHashMap<Integer, String>();
        int count = 0;
        for (Map.Entry<Integer, String> i : iter) {
            keys.put(i.getKey(), i.getValue());
            prev.put(i.getKey(), peek(i));
            count += i.getValue() == null ? 0 : 1;
        }
        long pre = loader.totalLoads();
        if (map == null) {
            if (force) {
                withLoadingForced().loadAll(keys.keySet());
            } else {
                withLoading().loadAll(keys.keySet());
            }
        } else {
            if (force) {
                withLoadingForced().loadAll(keys.keySet(), map);
            } else {
                withLoading().loadAll(keys.keySet(), map);
            }
        }
        awaitFinishedThreads();
        for (Map.Entry<Integer, String> i : iter) {
            if (force || i.getValue() != null) {
                assertPeek(entry(i.getKey(), keys.get(i.getKey())));
            } else {
                assertPeek(entry(i.getKey(), prev.get(i.getKey())));
            }
        }
        assertEquals(count + pre, loader.totalLoads());
    }

    private void load(Map.Entry<Integer, String> entry, AttributeMap map, boolean force) {
        long pre = loader.totalLoads();
        String prevValue = c.peek(entry.getKey());
        if (map == null) {
            if (force) {
                withLoadingForced().load(entry.getKey());
            } else {
                withLoading().load(entry.getKey());
            }
        } else {
            if (force) {
                withLoadingForced().load(entry.getKey(), map);
            } else {
                withLoading().load(entry.getKey(), map);
            }
        }
        awaitFinishedThreads();
        if (force || entry.getValue() != null) {
            assertEquals(entry.getKey(), loader.getLatestKey());
            if (map == null) {
                // assertEquals(Attributes.EMPTY_ATTRIBUTE_MAP,
                // loader.getLatestAttributeMap());
            } else {
                assertEquals(map, loader.getLatestAttributeMap());
            }
            assertEquals(pre + 1, loader.totalLoads());
            assertPeek(entry);
        } else {
            assertEquals(pre, loader.totalLoads());
            assertPeek(entry(entry, prevValue));
        }
    }
}
