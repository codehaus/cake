/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests loading through one of the {@link Cache#get(Object)} or
 * {@link Cache#getAll(java.util.Collection)} methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ImplicitLoading.java 511 2007-12-13 14:37:02Z kasper $
 */
public class ExplicitLoadKey extends AbstractCacheTCKTest {

    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test
    public void withLoad() {
        assertPeek(entry(M1, null));
        load(M1);
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void withLoadForce() {
        assertPeek(entry(M1, null));
        forceLoad(M1);
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void withLoadAttributes() {
        assertPeek(entry(M1, null));
        load(M1, asAtrMap(ATR1, "A", ATR2, "B"));
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void withLoadAttributesTwice() {
        load(M1, asAtrMap(ATR1, "A", ATR2, "B"));
        load(entry(M1, null), asAtrMap(ATR1, "A", ATR2, "B"));// already here
        assertLoads(1);
    }

    @Test
    public void withLoadMany() {
        load(M1);
        load(M2);
        load(M3);
        load(M4);
        load(M5);
        load(entry(M6, null));// not in loader
        assertLoads(5);
    }

    @Test
    public void withLoadTwice() {
        load(M1);
        load(entry(M1, null));// already here
        assertLoads(1);
    }

    @Test
    public void withLoadForceTwice() {
        forceLoad(M1);
        loader.withLoader(M1).setValue(M2.getValue());
        forceLoad(entry(M1, M2.getValue()));// already here, but force load
        assertLoads(2);
    }

    @Test
    public void withLoadForceAttributesTwice() {
        forceLoad(M1, asAtrMap(ATR1, "A", ATR2, "B"));
        loader.withLoader(M1).setValue(M3.getValue());
        // already here, but force load
        forceLoad(entry(M1, M3.getValue()), asAtrMap(ATR1, "A", ATR3, "C"));
        assertLoads(2);
    }
}
