package org.codehaus.cake.internal.cache.service.management;

import org.junit.Test;

public class DefaultCacheMXBeanTest {

    @Test(expected = NullPointerException.class)
    public void constructor_NPE() {
        new DefaultCacheMXBean(null);
    }
}
