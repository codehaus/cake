package org.codehaus.cake.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.test.tck.CacheTckRunner;
import org.codehaus.cake.service.test.tck.TckImplementationSpecifier;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(CacheTckRunner.class)
@TckImplementationSpecifier(SynchronizedCache.class)
public class SynchronizedCacheTest {
    
    @Test @Ignore
    public void prestart() {
        SynchronizedCache c = new SynchronizedCache();
        assertFalse(c.isStarted());
        c.prestart();
        assertTrue(c.isStarted());
    }
}
