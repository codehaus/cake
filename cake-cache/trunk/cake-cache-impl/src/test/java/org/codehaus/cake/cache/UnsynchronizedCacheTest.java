package org.codehaus.cake.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.test.tck.CacheTckRunner;
import org.codehaus.cake.service.test.tck.TckImplementationSpecifier;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CacheTckRunner.class)
@TckImplementationSpecifier(UnsynchronizedCache.class)
public class UnsynchronizedCacheTest {

    @Test @Ignore
    public void testStart() throws Exception {
        System.out.println("ok");
        System.in.read();
        System.out.println("ok");
    }

    @Test
    public void prestart() {
        UnsynchronizedCache c = new UnsynchronizedCache();
        assertFalse(c.isStarted());
        c.prestart();
        assertTrue(c.isStarted());
    }
}
