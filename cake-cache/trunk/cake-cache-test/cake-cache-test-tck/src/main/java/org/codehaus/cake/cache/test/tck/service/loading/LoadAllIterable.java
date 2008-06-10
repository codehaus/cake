package org.codehaus.cake.cache.test.tck.service.loading;

import java.util.Arrays;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadAllIterable extends AbstractCacheTCKTest {
    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};


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
    @Test
    public void withLoadAll() {
        assertSize(0);
        loadAll(M1, M2, M3);
        assertGet(M1, M2, M3);
        assertLoads(3);
    }

    @Test
    public void withLoadAllAttributes() {
        loadAll(asAtrMap(ATR1, "A", ATR2, "B"), M1, M2, M3);
        assertGet(M1, M2, M3);
        assertLoads(3);
    }

    @Test
    public void loadIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoading().loadAll(Arrays.asList(M1.getKey(),M2.getKey()));
        awaitTermination();
        withLoading().loadAll(Arrays.asList(M3.getKey(),M4.getKey()));
        awaitFinishedThreads();
        assertSize(0);
    }
    @Test
    public void withLoadMany() {
        loadAll(M1, M3, M4);
        loadAll(M2, entry(M4, null));
        loadAll(M5, entry(M6, null), entry(M7, null));
        loadAll(entry(M8, null), entry(M9, null));
        assertLoads(5);
    }

    @Test
    public void withLoadTwice() {
        loadAll(M1, M3, M4);
        loadAll(entry(M1, null), entry(M3, null), entry(M4, null));
        assertLoads(3);
    }
}
