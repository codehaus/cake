package org.codehaus.cake.cache.test.tck.service.loading;

import java.util.Map;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadingNPE extends AbstractCacheTCKTest {

    
     @Test(expected = NullPointerException.class)
     public void withKeysNPE3() {
         withLoading().loadAll((Map) null);
     }
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
