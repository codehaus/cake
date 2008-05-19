package org.codehaus.cake.cache.test.tck.core.values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class ValuesToString extends AbstractCacheTCKTest {
    /**
     * toString works.
     */
    @Test
    public void toStringValues() {
        assertEquals("[]", newCache().values().toString());
        String str = newCache(5).values().toString();
        assertTrue(str.startsWith("["));
        assertTrue(str.endsWith("]"));
        HashSet<String> hs = new HashSet<String>();
        String[] ss = str.replace("[", "").replace("]", "").replace(" ", "").split(",");
        for (String s : ss) {
            hs.add(s);
        }
        assertEquals(new HashSet(Arrays.asList("A","B","C","D","E")), hs);
    }

    /**
     * toString lazy starts the cache.
     */
    @Test
    public void toStringLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.values().toString();
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
        c.values().toString();

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        String s = c.values().toString();;
        assertEquals("[]", s);// cache should be empty
    }
}
