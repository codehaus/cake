package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadForced extends AbstractCacheTCKTest {

    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test(expected = NullPointerException.class)
    public void withKeyNPE() {
        withLoadingForced().load(null);
    }
    @Test(expected = NullPointerException.class)
    public void withKeyNPE1() {
        withLoadingForced().load(null, asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE2() {
        withLoadingForced().load(1, null);
    }
    @Test
    public void loadForce() {
        assertPeek(entry(M1, null));
        forceLoad(M1);
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void loadForceIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoadingForced().load(M1.getKey());
        awaitTermination();
        withLoadingForced().load(M2.getKey());
    }

    @Test
    public void loadForceTwice() {
        forceLoad(M1);
        loader.withLoader(M1).setValue(M2.getValue());
        forceLoad(entry(M1, M2.getValue()));// already here, but force load
        assertLoads(2);
    }

    @Test
    public void loadForceAttributesTwice() {
        forceLoad(M1, asAtrMap(ATR1, "A", ATR2, "B"));
        loader.withLoader(M1).setValue(M3.getValue());
        // already here, but force load
        forceLoad(entry(M1, M3.getValue()), asAtrMap(ATR1, "A", ATR3, "C"));
        assertLoads(2);
    }

    @Test
    public void loadForceAttributesIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoadingForced().load(M1.getKey(), asAtrMap(ATR1, "A", ATR2, "B"));
        awaitTermination();
        withLoadingForced().load(M2.getKey(), asAtrMap(ATR1, "A", ATR2, "B"));
    }
}
