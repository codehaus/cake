package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadingNPE extends AbstractCacheTCKTest {
    @Test(expected = NullPointerException.class)
    public void withKeyNPE() {
        withLoading().load(null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE1() {
        withLoading().load(null, asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE2() {
        withLoading().load(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE1() {
        withLoading().loadAll((Iterable) null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE2() {
        withLoading().loadAll(asList(1, null, 3));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE3() {
        withLoading().loadAll((Iterable) null, asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE4() {
        withLoading().loadAll(asList(1, null, 3), asDummy(AttributeMap.class));
    }
    @Test(expected = NullPointerException.class)
    public void withKeysNPE5() {
        withLoading().loadAll(asList(1,  3), null);
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
