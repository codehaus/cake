package org.codehaus.cake.cache.defaults;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.SynchronizedCache;
import org.codehaus.cake.test.tck.TckImplementationSpecifier;
import org.junit.Ignore;
import org.junit.Test;
//@RunWith(CacheTckRunner.class)
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
