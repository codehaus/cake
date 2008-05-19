package org.codehaus.cake.cache.test.tck.attributes.cache;

import java.util.Map;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractAttributeTest extends AbstractCacheTCKTest {
    final Attribute atr;

    AbstractAttributeTest(Attribute a) {
        this.atr = a;
    }

    @Before
    public void before() {
        conf.withAttributes().add(atr);
        init();
    }

    @Test
    public void notAvailable() {
        newConfigurationClean();
        init();
        put(M1);
        assertFalse(c.getEntry(M1.getKey()).getAttributes().contains(atr));
        assertEquals(atr.getDefault(), getAttribute(M1, atr));
    }

    @Test
    public void notAvailableLoaded() {
        loader.withLoader(M1).addAttribute(atr, atr.getDefault());
        newConfigurationClean();
        conf.withLoading().setLoader(loader);
        init();
        assertGet(M1);
        assertFalse(getEntry(M1).getAttributes().contains(atr));
        assertEquals(atr.getDefault(), getAttribute(M1, atr));
    }

    @Test
    public void notAvailableLoadedUpdated() {
        loader.withLoader(M1).addAttribute(atr, atr.getDefault());
        newConfiguration();
        conf.withLoading().setLoader(loader);
        init();
        assertGet(M1);
        forceLoad(M1);// value updated
        assertFalse(getEntry(M1).getAttributes().contains(atr));
        assertEquals(atr.getDefault(), getAttribute(M1, atr));
    }

    void assertAttribute(Map.Entry<Integer, String> entry) {
        assertAttribute(entry, atr.getDefault());
    }

    void assertAttribute(Map.Entry<Integer, String> entry, Object value) {
        assertTrue(c.getEntry(entry.getKey()).getAttributes().contains(atr));
        assertEquals(value, getAttribute(entry, atr));

        assertEquals(value, getAttribute(peekEntry(entry),atr));
        assertEquals(value, getAttribute(getEntry(entry), atr));
    }
}
