package org.codehaus.cake.cache.test.tck.service.loading;

import java.util.Map;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadAllMap extends AbstractCacheTCKTest {
    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test(expected = NullPointerException.class)
    public void withKeysNPE3() {
        withLoading().loadAll((Map) null);
    }
}
