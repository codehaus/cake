package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadingNPE extends AbstractCacheTCKTest {
    @Test(expected = NullPointerException.class)
    public void withKeyNPE() {
        withLoading().withKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE1() {
        withLoading().withKey(null, asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE2() {
        withLoading().withKey(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE1() {
        withLoading().withKeys((Iterable) null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE2() {
        withLoading().withKeys(asList(1, null, 3));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE3() {
        withLoading().withKeys((Iterable) null, asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE4() {
        withLoading().withKeys(asList(1, null, 3), asDummy(AttributeMap.class));
    }
    @Test(expected = NullPointerException.class)
    public void withKeysNPE5() {
        withLoading().withKeys(asList(1,  3), null);
    }
    //
    // @Test(expected = NullPointerException.class)
    // public void withKeysNPE3() {
    // withLoading().withKeys((Map) null);
    // }
    //
    // @Test(expected = NullPointerException.class)
    // public void withKeysNPE4() {
    // withLoading().withKeys(
    // asMap(1, asDummy(AttributeMap.class), null, asDummy(AttributeMap.class)));
    // }
    //
    // @Test(expected = NullPointerException.class)
    // public void withKeysNPE5() {
    // withLoading().withKeys(asMap(1, asDummy(AttributeMap.class), 1, null));
    // }
}
