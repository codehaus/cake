/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*  This class is automatically generated */ 
package org.codehaus.cake.internal.util.ops;

import static org.codehaus.cake.internal.util.ops.InternalPrimitivePredicates.*;
import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.util.ops.PrimitivePredicates.*;
import static org.junit.Assert.*;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.ops.PrimitivePredicates;
import org.codehaus.cake.util.ops.Ops.*;
import org.junit.Test;
/**
 * Various tests for {@link FloatPredicates}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitivePredicatesFloatTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitivePredicatesFloatTest {

    /**
     * Tests {@link LongPredicates#and(LongPredicate, LongPredicate)}.
     */
    @Test
    public void and() {
        assertTrue(PrimitivePredicates.and(FLOAT_TRUE, FLOAT_TRUE).op(1F));
        assertFalse(PrimitivePredicates.and(FLOAT_TRUE, FLOAT_FALSE).op(1F));
        assertFalse(PrimitivePredicates.and(FLOAT_FALSE, FLOAT_TRUE).op(1F));
        assertFalse(PrimitivePredicates.and(FLOAT_FALSE, FLOAT_FALSE).op(1F));

        AndFloatPredicate p = new AndFloatPredicate(FLOAT_FALSE, FLOAT_TRUE);
        assertSame(p.getLeft(), FLOAT_FALSE);
        assertSame(p.getRight(), FLOAT_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.and(FLOAT_FALSE, TestUtil.dummy(FloatPredicate.class)).op(1F);
    }

    /**
     * Tests that {@link FloatPredicates#and(FloatPredicate, FloatPredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE() {
        PrimitivePredicates.and(null, FLOAT_TRUE);
    }

    /**
     * Tests that {@link FloatPredicates#and(FloatPredicate, FloatPredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE1() {
        PrimitivePredicates.and(FLOAT_TRUE, null);
    }
    
    /* Test greater then */
    @Test
    public void equalsTo() {
        FloatPredicate f = PrimitivePredicates.equalsTo(5F);
        assertEquals(5F, new EqualsToFloatPredicate(5F).getEqualsTo(),0);
        assertFalse(f.op(4F));
        assertTrue(f.op(5F));
        assertFalse(f.op(6F));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
    
    /**
     * Tests {@link FloatPredicates#FALSE}.
     */
    @Test
    public void falsePredicate() {
        assertFalse(FLOAT_FALSE.op(2F));
        assertFalse(FLOAT_FALSE.op(Float.MIN_VALUE));
        FLOAT_FALSE.toString(); // does not fail
        assertIsSerializable(FLOAT_FALSE);
        assertSame(FLOAT_FALSE, TestUtil.serializeAndUnserialize(FLOAT_FALSE));
    }
   
    /* Test greater then */
    @Test
    public void greaterThen() {
        FloatPredicate f = PrimitivePredicates.greaterThen(5F);
        assertEquals(5F, new GreaterThenFloatPredicate(5F).getGreaterThen(),0);
        assertFalse(f.op(4F));
        assertFalse(f.op(5F));
        assertTrue(f.op(6F));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    @Test
    public void greaterThenOrEquals() {
        FloatPredicate f = PrimitivePredicates.greaterThenOrEquals(5F);
        assertEquals(5F, new GreaterThenOrEqualsFloatPredicate(5F).getGreaterThenOrEquals(),0);
        assertFalse(f.op(4F));
        assertTrue(f.op(5F));
        assertTrue(f.op(6F));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThen() {
        FloatPredicate f = PrimitivePredicates.lessThen(5F);
        assertEquals(5F, new LessThenFloatPredicate(5F).getLessThen(),0);
        assertTrue(f.op(4F));
        assertFalse(f.op(5F));
        assertFalse(f.op(6F));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThenOrEquals() {
        FloatPredicate f = PrimitivePredicates.lessThenOrEquals(5F);
        assertEquals(5F, new LessThenOrEqualsFloatPredicate(5F).getLessThenOrEquals(),0);
        assertTrue(f.op(4F));
        assertTrue(f.op(5F));
        assertFalse(f.op(6F));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
   
   
     /**
     * Tests that {@link FloatPredicates#not(FloatPredicate)} throws a
     * {@link NullPointerException} when invoked with a <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void notNPE() {
        PrimitivePredicates.not((FloatPredicate) null);
    }

    /**
     * Tests {@link FloatPredicates#TRUE}.
     */
    @Test
    public void notPredicate() {
        assertFalse(PrimitivePredicates.not(FLOAT_TRUE).op(2F));
        assertTrue(PrimitivePredicates.not(FLOAT_FALSE).op(2F));
        PrimitivePredicates.not(FLOAT_TRUE).toString(); // does not fail
        assertIsSerializable(PrimitivePredicates.not(FLOAT_TRUE));
        assertSame(FLOAT_TRUE, ((NotFloatPredicate) PrimitivePredicates.not(FLOAT_TRUE)).getPredicate());
    }

    /**
     * Tests {@link FloatPredicates#or(FloatPredicate, FloatPredicate)}.
     */
    @Test
    public void or() {
        assertTrue(PrimitivePredicates.or(FLOAT_TRUE, FLOAT_TRUE).op(1F));
        assertTrue(PrimitivePredicates.or(FLOAT_TRUE, FLOAT_FALSE).op(1F));
        assertTrue(PrimitivePredicates.or(FLOAT_FALSE, FLOAT_TRUE).op(1F));
        assertFalse(PrimitivePredicates.or(FLOAT_FALSE, FLOAT_FALSE).op(1F));

        OrFloatPredicate p = new OrFloatPredicate(FLOAT_FALSE, FLOAT_TRUE);
        assertSame(p.getLeft(), FLOAT_FALSE);
        assertSame(p.getRight(), FLOAT_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.or(FLOAT_TRUE, TestUtil.dummy(FloatPredicate.class)).op(1F);
    }

    /**
     * Tests that {@link FloatPredicates#or(FloatPredicate, FloatPredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE() {
        PrimitivePredicates.or(null, FLOAT_TRUE);
    }

    /**
     * Tests that {@link FloatPredicates#or(FloatPredicate, FloatPredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE1() {
        PrimitivePredicates.or(FLOAT_TRUE, null);
    }
   
    
   /**
     * Tests {@link FloatPredicates#TRUE}.
     */
    @Test
    public void truePredicate() {
        assertTrue(FLOAT_TRUE.op(2F));
        assertTrue(FLOAT_TRUE.op(Float.MIN_VALUE));
        FLOAT_TRUE.toString(); // does not fail
        assertIsSerializable(FLOAT_TRUE);
        assertSame(FLOAT_TRUE, TestUtil.serializeAndUnserialize(FLOAT_TRUE));
    }
}