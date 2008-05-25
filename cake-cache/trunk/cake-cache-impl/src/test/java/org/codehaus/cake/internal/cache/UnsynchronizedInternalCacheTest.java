package org.codehaus.cake.internal.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.test.tck.CacheTckRunner;
import org.codehaus.cake.service.test.tck.TckImplementationSpecifier;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CacheTckRunner.class)
@TckImplementationSpecifier(UnsynchronizedInternalCache.class)
public class UnsynchronizedInternalCacheTest {

    @Test
    public void prestart() {
        UnsynchronizedInternalCache c = new UnsynchronizedInternalCache();
        assertFalse(c.isStarted());
        c.prestart();
        assertTrue(c.isStarted());
    }
   

}
