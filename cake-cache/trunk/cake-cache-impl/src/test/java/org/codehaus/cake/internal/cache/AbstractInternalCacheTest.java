package org.codehaus.cake.internal.cache;

import org.junit.Test;

public class AbstractInternalCacheTest {

    @Test(expected = NullPointerException.class)
    public void checkPutNPE1() {
        AbstractInternalCache.checkPut(null, "", "");
    }

    @Test(expected = NullPointerException.class)
    public void checkPutNPE2() {
        AbstractInternalCache.checkPut("", null, "");
    }

    @Test(expected = NullPointerException.class)
    public void checkPutNPE3() {
        AbstractInternalCache.checkPut("", "", null);
    }
}
