package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_SIZE;

import java.util.Random;

import org.codehaus.cake.cache.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.policy.Policies;
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
    public void evictorVolume() {
        conf.withAttributes().add(ENTRY_SIZE);
        loader.setAttribute(ENTRY_SIZE, LongOps.add(1));// size=key+1

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
        conf.withAttributes().add(ENTRY_SIZE);
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
        loader.setAttribute(ENTRY_SIZE, new LongOp() {
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
}
