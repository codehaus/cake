package org.codehaus.cake.cache.test.tck.attributes;

import static org.codehaus.cake.cache.test.util.AtrStubs.I_2;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class PrimitiveAttributes  extends AbstractCacheTCKTest {
    @Test
    public void intAttribute() {
        conf.addEntryAttributes(I_2);
        newCache();
        System.out.println(c.getEntry(1).get(I_2));

    }
}
