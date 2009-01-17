package org.codehaus.cake.cache.test.tck.view;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.util.Comparator;

import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class ViewNpe extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderBy() {
        c.view().orderBy(null);
    }

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderByAttributeMax() {
        c.view().orderByAttributeMax(null);
    }

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderByKeys() {
        c.view().orderByKeys(null);
    }

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderByValues() {
        c.view().orderByValues(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cacheViewSetLimit() {
        c.view().setLimit(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void viewSetLimit() {
        c.view().keys().setLimit(-1);
    }

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderByAttributeMin() {
        c.view().orderByAttributeMin(null);
    }

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderByAttribute1() {
        c.view().orderByAttribute(null, dummy(Comparator.class));
    }

    @Test(expected = NullPointerException.class)
    public void cacheViewOrderByAttribute2() {
        c.view().orderByAttribute(new IntAttribute() {}, null);
    }

    @Test(expected = NullPointerException.class)
    public void viewApply() {
        c.view().keys().apply(null);
    }

    @Test(expected = NullPointerException.class)
    public void viewMap() {
        c.view().values().map(null);
    }

    @Test(expected = NullPointerException.class)
    public void viewMax() {
        c.view().values().max(null);
    }

    @Test(expected = NullPointerException.class)
    public void viewMin() {
        c.view().values().min(null);
    }

    @Test(expected = NullPointerException.class)
    public void viewReduce() {
        c.view().values().reduce(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void viewToArray() {
        c.view().values().toArray(null);
    }
}
