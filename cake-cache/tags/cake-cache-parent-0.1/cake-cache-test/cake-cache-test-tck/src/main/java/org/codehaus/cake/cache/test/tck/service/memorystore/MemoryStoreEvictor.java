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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.LongOps;
import org.codehaus.cake.ops.Ops.LongOp;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Test;

public class MemoryStoreEvictor extends AbstractCacheTCKTest {

    @Test
    public void evictorSize() {
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {

            public void op(MemoryStoreService<Integer, String> a) {
                assertEquals(10, a.getMaximumSize());
                assertEquals(Long.MAX_VALUE, a.getMaximumVolume());
                assertEquals(11, a.getSize());
                a.trimToSize(5);
            }
        });
        conf.withMemoryStore().setMaximumSize(10);
        init();
        put(11);
        assertSize(5);
    }

    @Test
    public void evictorSizeComparator() {
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {
                assertEquals(10, a.getMaximumSize());
                assertEquals(Long.MAX_VALUE, a.getMaximumVolume());
                assertEquals(11, a.getSize());
                a.trimToSize(5, new Comparator<CacheEntry<Integer, String>>() {
                    public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                        return o2.getKey() - o1.getKey();
                    }
                });
            }
        });
        conf.withMemoryStore().setMaximumSize(10);
        init();
        put(11);
        assertSize(5);
        assertEquals(c.keySet(), new HashSet(Arrays.asList(1, 2, 3, 4, 5)));

        newConfiguration();
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {
                assertEquals(10, a.getMaximumSize());
                assertEquals(Long.MAX_VALUE, a.getMaximumVolume());
                assertEquals(11, a.getSize());
                a.trimToSize(-5, new Comparator<CacheEntry<Integer, String>>() {
                    public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                        return o1.getKey() - o2.getKey();
                    }
                });
            }
        });
        conf.withMemoryStore().setMaximumSize(10);
        init();
        put(11);
        assertSize(6);
        assertEquals(c.keySet(), new HashSet(Arrays.asList(6, 7, 8, 9, 10, 11)));
    }

    @Test
    public void evictorVolume() {
        conf.withAttributes().add(SIZE);
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1

        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {

            public void op(MemoryStoreService<Integer, String> a) {
                assertEquals(Integer.MAX_VALUE, a.getMaximumSize());
                assertEquals(4, a.getSize());
                assertEquals(14, a.getVolume());
                a.trimToVolume(10);
            }
        });
        conf.withMemoryStore().setPolicy(Policies.newLRU());
        conf.withMemoryStore().setMaximumVolume(10);
        init();

        get(M1, M2, M3);
        assertVolume(9);
        get(M4);
        assertSize(2);
        assertContainsKey(M3);
        assertContainsKey(M4);
    }

    @Test
    public void disabled() {
        final AtomicBoolean ab = new AtomicBoolean();
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {
                if (!ab.get()) {
                    assertFalse(a.isDisabled());
                    ab.set(true);
                } else {
                    assertTrue(a.isDisabled());
                }
                a.trimToSize(1);
            }
        });
        conf.withMemoryStore().setMaximumSize(1);
        init();
        put(2);
        withMemoryStore().setDisabled(true);
        put(2);
    }

    @Test
    public void evictorVolumeComparator() {
        loader.add(M1, M2, M3, M4, M5, M6, M7, M8, M9);
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.withAttributes().add(SIZE);
        conf.withMemoryStore().setPolicy(Policies.newLRU());
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {
                assertEquals(Integer.MAX_VALUE, a.getMaximumSize());
                assertEquals(9, a.getSize());
                assertEquals(54, a.getVolume());
                a.trimToVolume(19, new Comparator<CacheEntry<Integer, String>>() {

                    public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                        return o2.getKey() - o1.getKey();
                    }
                });
            }
        });
        conf.withMemoryStore().setMaximumVolume(53);
        init();
        assertGet(M1, M2, M3, M4, M5, M6, M7, M8);
        assertSize(8);
        assertGet(M9);
        assertSize(4);
        assertVolume(14);
        assertEquals(c.keySet(), new HashSet(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void evictorSizeNegTrim() {
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {

            public void op(MemoryStoreService<Integer, String> a) {
                assertEquals(5, a.getMaximumSize());
                assertEquals(6, a.getSize());
                a.trimToSize(-1);
            }
        });
        conf.withMemoryStore().setMaximumSize(5);
        init();
        put(6);
        assertSize(5);
        for (int i = 100; i < 150; i++) {
            c.put(i, "" + i);
        }
        assertSize(5);
    }

    @Test
    public void evictorSizeNoOp() {
        conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {}
        });
        conf.withMemoryStore().setMaximumSize(10);
        init();
        put(11);
        // post a warning, that we manually had to reduce the size of the cache
        exceptionHandler.eat(null, Level.Warn);
        assertSize(10);
    }

    @Test
    public void evictorVolumeAndSize() {
        conf.withAttributes().add(SIZE);
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {
                a.trimToSize(-5);
                a.trimToVolume(-50);
            }
        });
        conf.withMemoryStore().setPolicy(Policies.newLRU());
        conf.withMemoryStore().setMaximumVolume(200);
        conf.withMemoryStore().setMaximumSize(20);
        init();
        final Random r = new Random();
        for (int i = 0; i < 100; i++) {
            loader.add(i, "" + i);
        }
        loader.setAttribute(SIZE, new LongOp() {
            public long op(long a) {
                return r.nextInt(20);
            }
        });
        for (int i = 0; i < 1000; i++) {
            c.get(r.nextInt(120));
            assertValidSizeAndVolume();
        }
        assertValidSizeAndVolume();
    }

    @Test
    public void evictorNoSupportedOperations() {
        final AtomicBoolean hasRun = new AtomicBoolean();

        conf.withAttributes().add(SIZE);
        conf.withMemoryStore().setEvictor(new Procedure<MemoryStoreService<Integer, String>>() {
            public void op(MemoryStoreService<Integer, String> a) {
                try {
                    a.setDisabled(true);
                    throw new AssertionError();
                } catch (UnsupportedOperationException ok) {}
                try {
                    a.setMaximumSize(1);
                    throw new AssertionError();
                } catch (UnsupportedOperationException ok) {}
                try {
                    a.setMaximumVolume(4);
                    throw new AssertionError();
                } catch (UnsupportedOperationException ok) {}
                a.trimToSize(-1);
                hasRun.set(true);
            }
        });
        conf.withMemoryStore().setPolicy(Policies.newLRU());
        conf.withMemoryStore().setMaximumSize(2);
        init();
        withLoadingForced().load(M1.getKey());
        withLoadingForced().load(M2.getKey());
        withLoadingForced().load(M3.getKey());
        awaitFinishedThreads();
        assertSize(2);
        assertTrue(hasRun.get());
    }

}
