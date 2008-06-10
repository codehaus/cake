package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadAll extends AbstractCacheTCKTest {

    public static final Attribute<String> ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute<String> ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute<String> ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test
    public void withLoadAttributesTwice() {
        loadAll(asAtrMap(ATR1, "A", ATR2, "B"), M1, M2, M3);
        // already here
        loadAll(asAtrMap(ATR1, "A", ATR2, "B"), entry(M1, null), entry(M2, null), entry(M3, null));
        assertLoads(3);
    }

    @Test
    public void withLoadAttributesTwiceDiff() {
        loadAll(asAtrMap(ATR1, "A", ATR2, "B"), M1, M3, M4);
        // already here
        loadAll(asAtrMap(ATR1, "A", ATR2, "B"), entry(M1, null), M2, entry(M3, null), M5);
        assertLoads(5);
    }

    @Test
    public void withLoadAll() {
        withLoading().loadAll();
        assertSize(0);
        withLoadingForced().loadAll();
        assertSize(0);
        withLoading().loadAll(new DefaultAttributeMap());
        assertSize(0);
        withLoadingForced().loadAll(new DefaultAttributeMap());
        assertSize(0);
        assertLoadCount(0);
    }

    @Test
    public void withLoadForce() {
        put(M1);
        put(M2);
        withLoading().loadAll();
        withLoading().loadAll(new DefaultAttributeMap());
        assertLoadCount(0);
        loader.withLoader(M1).setValue("3");
        withLoadingForced().loadAll();
        awaitFinishedThreads();
        assertLoadCount(2);
        assertPeek(entry(M1, "3"));
        withLoadingForced().loadAll();
        awaitFinishedThreads();
        assertLoadCount(4);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE1() {
        withLoading().loadAll((AttributeMap) null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE2() {
        withLoadingForced().loadAll((AttributeMap) null);
    }
}
