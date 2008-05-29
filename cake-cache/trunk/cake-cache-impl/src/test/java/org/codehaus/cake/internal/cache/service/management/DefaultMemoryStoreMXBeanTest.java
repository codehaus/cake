package org.codehaus.cake.internal.cache.service.management;

import org.junit.Test;

public class DefaultMemoryStoreMXBeanTest {

    @Test(expected = NullPointerException.class)
    public void constructor_NPE() {
        new DefaultMemoryStoreMXBean(null);
    }
}
