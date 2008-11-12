package org.codehaus.cake.cache.test.tck.selection;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.BinaryPredicate;
import org.codehaus.cake.ops.Ops.BytePredicate;
import org.codehaus.cake.ops.Ops.CharPredicate;
import org.codehaus.cake.ops.Ops.DoublePredicate;
import org.codehaus.cake.ops.Ops.FloatPredicate;
import org.codehaus.cake.ops.Ops.IntPredicate;
import org.codehaus.cake.ops.Ops.LongPredicate;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.ShortPredicate;
import org.junit.Test;

public class IllegalSelection extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void selectOnBinaryNPE() {
        c.select().on((BinaryPredicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnPredicateNPE() {
        c.select().on((Predicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnAFilterNPE() {
        c.select().on(null, Predicates.TRUE);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnObjectFilterNPE() {
        c.select().on(new ObjectAttribute(String.class) {}, (Predicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnBoolNPE() {
        c.select().on(null, false);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnByteNPE() {
        c.select().on(null, dummy(BytePredicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void selectOnByteFilterNPE() {
        c.select().on(new ByteAttribute() {}, (BytePredicate) null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnCharNPE() {
        c.select().on(null, dummy(CharPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnCharFilterNPE() {
        c.select().on(new CharAttribute() {}, (CharPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnDoubleNPE() {
        c.select().on(null, dummy(DoublePredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnDoubleFilterNPE() {
        c.select().on(new DoubleAttribute() {}, (DoublePredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnFloatNPE() {
        c.select().on(null, dummy(FloatPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnFloatFilterNPE() {
        c.select().on(new FloatAttribute() {}, (FloatPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnIntNPE() {
        c.select().on(null, dummy(IntPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnIntFilterNPE() {
        c.select().on(new IntAttribute() {}, (IntPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnLongNPE() {
        c.select().on(null, dummy(LongPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnLongFilterNPE() {
        c.select().on(new LongAttribute() {}, (LongPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnShortNPE() {
        c.select().on(null, dummy(ShortPredicate.class));
    }
    @Test(expected = NullPointerException.class)
    public void selectOnShortFilterNPE() {
        c.select().on(new ShortAttribute() {}, (ShortPredicate) null);
    }
    @Test(expected = NullPointerException.class)
    public void selectOnKeyNPE() {
        c.select().onKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnKeyTypeNPE() {
        c.select().onKeyType(null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnValueNPE() {
        c.select().onValue(null);
    }

    @Test(expected = NullPointerException.class)
    public void selectOnValueTypeNPE() {
        c.select().onValueType(null);
    }
}
