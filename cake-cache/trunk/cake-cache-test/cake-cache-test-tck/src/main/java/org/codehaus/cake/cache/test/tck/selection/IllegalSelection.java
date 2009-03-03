package org.codehaus.cake.cache.test.tck.selection;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.codehaus.cake.util.attribute.ShortAttribute;
import org.codehaus.cake.util.ops.Predicates;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.BytePredicate;
import org.codehaus.cake.util.ops.Ops.CharPredicate;
import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.ShortPredicate;
import org.junit.Test;

public class IllegalSelection extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void selectOnBinaryNPE() {
        c.filter().on((BinaryPredicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnPredicateNPE() {
        c.filter().on((Predicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnAFilterNPE() {
        c.filter().on(null, Predicates.TRUE);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnObjectFilterNPE() {
        c.filter().on(new ObjectAttribute(String.class) {}, (Predicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnBoolNPE() {
        c.filter().on(null, false);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnByteNPE() {
        c.filter().on(null, dummy(BytePredicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void selectOnByteFilterNPE() {
        c.filter().on(new ByteAttribute() {}, (BytePredicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnCharNPE() {
        c.filter().on(null, dummy(CharPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnCharFilterNPE() {
        c.filter().on(new CharAttribute() {}, (CharPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnDoubleNPE() {
        c.filter().on(null, dummy(DoublePredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnDoubleFilterNPE() {
        c.filter().on(new DoubleAttribute() {}, (DoublePredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnFloatNPE() {
        c.filter().on(null, dummy(FloatPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnFloatFilterNPE() {
        c.filter().on(new FloatAttribute() {}, (FloatPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnIntNPE() {
        c.filter().on(null, dummy(IntPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnIntFilterNPE() {
        c.filter().on(new IntAttribute() {}, (IntPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnLongNPE() {
        c.filter().on(null, dummy(LongPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnLongFilterNPE() {
        c.filter().on(new LongAttribute() {}, (LongPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnShortNPE() {
        c.filter().on(null, dummy(ShortPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnShortFilterNPE() {
        c.filter().on(new ShortAttribute() {}, (ShortPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnKeyNPE() {
        c.filter().onKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnKeyTypeNPE() {
        c.filter().onKeyType(null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnValueNPE() {
        c.filter().onValue(null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnValueTypeNPE() {
        c.filter().onValueType(null);
    }
}
