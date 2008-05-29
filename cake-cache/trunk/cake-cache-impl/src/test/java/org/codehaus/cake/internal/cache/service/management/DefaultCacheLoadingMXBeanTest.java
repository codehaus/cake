package org.codehaus.cake.internal.cache.service.management;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.junit.Test;

public class DefaultCacheLoadingMXBeanTest {

    @Test(expected = NullPointerException.class)
    public void constructor_NPE() {
        new DefaultCacheLoadingMXBean(null, dummy(CacheLoadingService.class));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_NPE1() {
        new DefaultCacheLoadingMXBean(dummy(CacheLoadingService.class), null);
    }
}
