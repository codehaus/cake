package org.codehaus.cake.internal.cache.service.management;

import org.junit.Test;

public class DefaultCacheLoadingMXBeanTest {

    @Test(expected = NullPointerException.class)
    public void constructor_NPE() {
        new DefaultCacheLoadingMXBean(null);
    }
}
