package org.codehaus.cake.cache.test.tck.core.keyset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class KeySetToString extends AbstractCacheTCKTest {
    /**
     * toString works.
     */
    @Test
    public void toStringKeySet() {
        assertEquals("[]", newCache().keySet().toString());
        String str = newCache(5).keySet().toString();
        assertTrue(str.startsWith("["));
        assertTrue(str.endsWith("]"));
        HashSet<Integer> hs = new HashSet<Integer>();
        String[] ss = str.replace("[", "").replace("]", "").replace(" ", "").split(",");
        for (String s : ss) {
            hs.add(Integer.parseInt(s));
        }
        assertEquals(new HashSet(Arrays.asList(1, 2, 3, 4, 5)), hs);
    }

    /**
     * toString lazy starts the cache.
     */
    @Test
    public void toStringLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.keySet().toString();
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void toStringShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.keySet().toString();

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        String s = c.keySet().toString();;
        assertEquals("[]", s);// cache should be empty
    }
}
