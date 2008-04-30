/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.entryset;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySetIterator.java 554 2008-01-08 23:32:04Z kasper $
 */
public class EntrySetIterator extends AbstractCacheTCKTest {

    @Test
    @SuppressWarnings("unused")
    public void iterator() {
        int count = 0;
        init();
        for (Integer entry : c.keySet()) {
            count++;
        }
        assertEquals(0, count);
        c = newCache(5);
        Iterator<Map.Entry<Integer, String>> iter = c.entrySet().iterator();
        while (iter.hasNext()) {
            assertTrue(M1_TO_M5_SET.contains(iter.next()));
            count++;
        }
        assertEquals(5, count);
    }

    /**
     * {@link Set#iterator()} lazy starts the cache.
     */
    @Test
    public void iteratorLazyStart() {
        c = newCache(0);
        c.entrySet().iterator();
        checkLazystart();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextNSE() {
        c = newCache(1);
        Iterator<Map.Entry<Integer, String>> iter = c.entrySet().iterator();
        iter.next();
        iter.next();
    }

    @Test
    @SuppressWarnings("unused")
    public void iteratorRemove() {
        c = newCache(5);
        Iterator<Map.Entry<Integer, String>> iter = c.entrySet().iterator();
        while (iter.hasNext()) {
            int next = iter.next().getKey();
            if (next % 2 == 0) {
                iter.remove();
            }
        }
        assertSize(3);
        assertTrue(c.entrySet().contains(M1));
        assertFalse(c.entrySet().contains(M2));
        assertTrue(c.entrySet().contains(M3));
        assertFalse(c.entrySet().contains(M4));
        assertTrue(c.entrySet().contains(M5));
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE() {
        newCache().entrySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE1() {
        newCache(1).entrySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE2() {
        c = newCache(1);
        Iterator<Map.Entry<Integer, String>> iter = c.entrySet().iterator();
        iter.next();
        iter.remove();
        iter.remove();// should throw
    }

    /**
     * {@link Set#iterator()} fails when the cache is shutdown.
     */
    @Test
    public void iteratorShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();
        c.entrySet().iterator();
    }
}
