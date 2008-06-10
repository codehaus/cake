package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Load extends AbstractCacheTCKTest {

    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

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

    @Test
    public void load() {
        assertPeek(entry(M1, null));
        load(M1);
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void loadAttributes() {
        assertPeek(entry(M1, null));
        load(M1, asAtrMap(ATR1, "A", ATR2, "B"));
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void loadAttributesIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoading().load(M3.getKey(), asAtrMap(ATR1, "A", ATR2, "B"));
        awaitTermination();
        withLoading().load(M4.getKey(), asAtrMap(ATR1, "A", ATR2, "B"));
        awaitFinishedThreads();
        assertSize(0);
    }
    @Test
    public void loadAttributesTwice() {
        load(M1, asAtrMap(ATR1, "A", ATR2, "B"));
        load(entry(M1, null), asAtrMap(ATR1, "A", ATR2, "B"));// already here
        assertLoads(1);
    }
    @Test
    public void loadIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoading().load(M1.getKey());
        awaitTermination();
        withLoading().load(M2.getKey());
        awaitFinishedThreads();
        assertSize(0);
    }

    @Test
    public void loadMany() {
        load(M1);
        load(M2);
        load(M3);
        load(M4);
        load(M5);
        load(entry(M6, null));// not in loader
        assertLoads(5);
    }

    @Test
    public void loadTwice() {
        load(M1);
        load(entry(M1, null));// already here
        assertLoads(1);
    }

}
