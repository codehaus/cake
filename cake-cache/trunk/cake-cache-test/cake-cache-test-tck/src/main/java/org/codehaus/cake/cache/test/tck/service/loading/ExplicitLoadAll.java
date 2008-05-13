/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests loading through one of the {@link Cache#get(Object)} or
 * {@link Cache#getAll(java.util.Collection)} methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ImplicitLoading.java 511 2007-12-13 14:37:02Z kasper $
 */
public class ExplicitLoadAll extends AbstractCacheTCKTest {

    public static final Attribute<String> ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute<String> ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute<String> ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test
    public void withLoadAll() {
        withLoading().withAll().load();
        assertSize(0);
        withLoading().withAll().forceLoad();
        assertSize(0);
        withLoading().withAll(new DefaultAttributeMap()).load();
        assertSize(0);
        withLoading().withAll(new DefaultAttributeMap()).forceLoad();
        assertSize(0);
        assertLoadCount(0);
    }

    @Test
    public void withLoadForce() {
        put(M1);
        put(M2);
        withLoading().withAll().load();
        withLoading().withAll(new DefaultAttributeMap()).load();
        assertLoadCount(0);
        loader.withLoader(M1).setValue("3");
        withLoading().withAll().forceLoad();
        assertLoadCount(2);
        assertPeek(entry(M1, "3"));
        withLoading().withAll().forceLoad();
        assertLoadCount(4);
    }

}
