/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_SIZE;

import java.util.Arrays;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.CollectionOps;
import org.codehaus.cake.ops.LongOps;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Test;

public class MemoryStoreIsCacheable extends AbstractCacheTCKTest {

    static Predicate ic = Predicates.mapAndEvaluate(CollectionOps.MAP_ENTRY_TO_KEY_OP, Predicates
            .anyEquals(1, 2));

    static Predicate ivalue = Predicates.mapAndEvaluate(CollectionOps.MAP_ENTRY_TO_VALUE_OP,
            Predicates.anyEquals("A", "B"));

    @Test
    public void get() {
        // IntegerToStringLoader loader = new IntegerToStringLoader();
        conf.withMemoryStore().setIsCacheableFilter(ic);
        init();
        assertGet(M1, M2, entry(M3, null), entry(M4, null), entry(M5, null));
        assertSize(2);
        assertEquals(5, loader.totalLoads());
    }

    @Test
    public void getAll() {
        conf.withMemoryStore().setIsCacheableFilter(ic);
        init();
        assertGetAll(M1, M2, entry(M3, null), entry(M4, null), entry(M5, null));
        assertSize(2);
        assertEquals(5, loader.totalLoads());
    }

    @Test
    public void load() {
        conf.withMemoryStore().setIsCacheableFilter(ic);
        init();
        withLoading().withKey(M1.getKey()).load();
        withLoading().withKey(M2.getKey()).forceLoad();
        withLoading().withKey(M3.getKey()).load();
        withLoading().withKey(M4.getKey()).forceLoad();
        withLoading().withKey(M5.getKey()).load();
        awaitFinishedThreads();
        assertSize(2);
        assertEquals(5, loader.totalLoads());
    }

    @Test
    public void loadAll() {
        conf.withMemoryStore().setIsCacheableFilter(ic);
        init();
        withLoading().withKeys(Arrays.asList(1, 2, 3, 4, 5)).load();
        awaitFinishedThreads();
        assertSize(2);
        assertEquals(5, loader.totalLoads());
    }

    @Test
    public void loadRemoveOld() {
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        put(M1);
        put(entry(M1, "C"));
        assertSize(0);
    }

    @Test
    public void loadVolume() {
        loader.setAttribute(ENTRY_SIZE, LongOps.add(1));// size=key+1
        conf.withAttributes().add(ENTRY_SIZE);
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        assertGet(M1, M2);
        assertSize(2);
        assertVolume(5);

        loader.setAttribute(ENTRY_SIZE, LongOps.add(2));// size=key+2
        loader.withLoader(M1).setValue("C");
        withLoading().withKey(M1.getKey()).forceLoad();
        assertSize(1);
        assertVolume(3);

        withLoading().withKey(M2.getKey()).forceLoad();
        assertSize(1);// ?? replace
        assertVolume(4);
    }

    @Test
    public void predicateFail() {
        conf.withMemoryStore().setIsCacheableFilter(new Predicate() {
            public boolean op(Object t) {
                throw RuntimeException1.INSTANCE;
            }
        });
        conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
        init();
        put(M1);
        assertSize(0);
        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Fatal);
    }

    @Test
    public void put() {
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        put(M1);
        assertSize(1);
        put(M2);
        assertSize(2);
        put(M3);
        put(M4);
        assertSize(2);
        put(entry(M1, "C"));
        assertSize(1); // ?? replace
    }

    @Test
    public void putAll() {
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        putAll(M1, M3, M4);
        assertSize(1);
        putAll(M1, M2, M3, M4);
        assertSize(2);
        putAll(entry(M1.getKey(), "foo"), M2, M3, M4);
        assertSize(1);// ?? replace
    }

    @Test
    public void putIfAbsent() {
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        putIfAbsent(entry(M1, "C"));
        assertSize(0);
        putIfAbsent(entry(M1, "B"));
        assertSize(1);

    }

    @Test
    public void putUpdate() {
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        put(M1);
        put(M2);
        assertSize(2);
        c.put(M1.getKey(), "C");
        assertSize(1); // ?? replace
        c.put(M2.getKey(), "D");
        assertSize(0);// ?? replace
        put(M1);
        put(M2);
        assertSize(2);
    }

    @Test
    public void replace() {
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        put(M1);
        put(M2);
        assertSize(2);
        replace(M1.getKey(), "C");
        assertSize(1); // ?? replace
        replace(M2.getKey(), "D");
        assertSize(0);// ?? replace
    }
}
