package org.codehaus.cake.cache.test.tck.query;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.DoubleOps;
import org.codehaus.cake.ops.FloatOps;
import org.codehaus.cake.ops.IntOps;
import org.codehaus.cake.ops.LongOps;
import org.codehaus.cake.ops.ObjectOps;
import org.junit.Test;

public class QueryNpe extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void keyTo() {
        c.query().keyTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void valueTo() {
        c.query().valueTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void putInto1() {
        c.query().putInto(null);
    }

    @Test(expected = NullPointerException.class)
    public void orderBy() {
        c.query().orderBy(null);
    }

//    @Test(expected = NullPointerException.class)
//    public void orderByBoolean() {
//        c.query().orderBy(null, false);
//    }

//    @Test(expected = NullPointerException.class)
//    public void orderByDouble1() {
//        c.query().orderBy(null, DoubleOps.COMPARATOR);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void orderByDouble2() {
//        c.query().orderBy(new DoubleAttribute() {}, null);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void orderByFloat1() {
//        c.query().orderBy(null, FloatOps.COMPARATOR);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void orderByFloat2() {
//        c.query().orderBy(new FloatAttribute() {}, null);
//    }
//    @Test(expected = NullPointerException.class)
//    public void orderByInt1() {
//        c.query().orderBy(null, IntOps.COMPARATOR);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void orderByInt2() {
//        c.query().orderBy(new IntAttribute() {}, null);
//    }
//    @Test(expected = NullPointerException.class)
//    public void orderByLong1() {
//        c.query().orderBy(null, LongOps.COMPARATOR);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void orderByLong2() {
//        c.query().orderBy(new LongAttribute() {}, null);
//    }
    @Test(expected = NullPointerException.class)
    public void orderByValues() {
        c.query().orderByValues(null);
    }

    @Test(expected = NullPointerException.class)
    public void orderByKeys() {
        c.query().orderByKeys(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLimit() {
        c.query().setLimit(0);
    }

}
