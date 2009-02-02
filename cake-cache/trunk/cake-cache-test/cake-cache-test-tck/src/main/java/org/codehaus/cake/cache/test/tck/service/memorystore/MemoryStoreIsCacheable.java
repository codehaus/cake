/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import java.util.Arrays;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.memorystore.IsCacheablePredicate;
import org.codehaus.cake.cache.service.memorystore.IsCacheablePredicates;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.CollectionOps;
import org.codehaus.cake.ops.LongOps;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Test;

public class MemoryStoreIsCacheable extends AbstractCacheTCKTest {

    static IsCacheablePredicate<Integer, String> ic = IsCacheablePredicates.predicateOnKey(
            Predicates.equalsToAny(1, 2), true);

    static IsCacheablePredicate<Integer, String> ivalue = IsCacheablePredicates.predicateOnValue(Predicates.equalsToAny("A", "B"), false);

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
        withLoading().load(M1.getKey());
        withLoadingForced().load(M2.getKey());
        withLoading().load(M3.getKey());
        withLoadingForced().load(M4.getKey());
        withLoading().load(M5.getKey());
        awaitFinishedThreads();
        assertSize(2);
        assertEquals(5, loader.totalLoads());
    }

    @Test
    public void loadAll() {
        conf.withMemoryStore().setIsCacheableFilter(ic);
        init();
        withLoading().loadAll(Arrays.asList(1, 2, 3, 4, 5));
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
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.addEntryAttributes(SIZE);
        conf.withMemoryStore().setIsCacheableFilter(ivalue);
        init();
        assertGet(M1, M2);
        assertSize(2);
        assertVolume(5);

        loader.setAttribute(SIZE, LongOps.add(2));// size=key+2
        loader.withLoader(M1).setValue("C");
        withLoadingForced().load(M1.getKey());
        awaitFinishedThreads();
        assertSize(1);
        assertVolume(3);

        withLoadingForced().load(M2.getKey());
        awaitFinishedThreads();
        assertSize(1);// ?? replace
        assertVolume(4);
    }

//    @Test
//    public void predicateFail() {
//        conf.withMemoryStore().setIsCacheableFilter(new Predicate() {
//            public boolean op(Object t) {
//                throw RuntimeException1.INSTANCE;
//            }
//        });
//        conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
//        init();
//        put(M1);
//        assertSize(0);
//        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
//    }
//
//    @Test
//    public void predicateFailSome() {
//        conf.withMemoryStore().setIsCacheableFilter(new Predicate<CacheEntry>() {
//            public boolean op(CacheEntry t) {
//                if (t.getKey() == M1.getKey()) {
//                    return true;
//                } else if (t.getKey() == M3.getKey()) {
//                    return false;
//                }
//                throw RuntimeException1.INSTANCE;
//            }
//        });
//        conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
//        init();
//        putAll(M1, M2, M3);
//        assertSize(1);
//        assertGet(M1);
//        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
//    }

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
