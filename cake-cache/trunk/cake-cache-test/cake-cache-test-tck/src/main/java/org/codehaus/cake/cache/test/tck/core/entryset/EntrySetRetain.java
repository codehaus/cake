/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.entryset;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySetRetain.java 537 2007-12-30 19:21:20Z kasper $
 */
public class EntrySetRetain extends AbstractCacheTCKTest {

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void retainAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().retainAll(Collections.singleton(M1));
        checkLazystart();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retainAll() {
        c = newCache(1);
        c.entrySet().retainAll(Collections.singleton(M1));
        assertSize(1);
        c.entrySet().retainAll(Collections.singleton(M1_NULL));
        assertSize(0);
        c = newCache(1);
        c.entrySet().retainAll(Collections.singleton(M2));
        assertSize(0);
        c = newCache(5);
        c.entrySet().retainAll(Arrays.asList(M1, "F", M3, "G", M5));
        assertSize(3);
        assertTrue(c.entrySet().contains(M1) && c.entrySet().contains(M3)
                && c.entrySet().contains(M5));

    }

    @Test(expected = NullPointerException.class)
    public void retainAllNPE() {
        newCache(5).entrySet().retainAll(null);
    }
}
