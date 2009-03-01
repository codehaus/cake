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
 * Various tests for {@link DoublePredicates}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitivePredicatesDoubleTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitivePredicatesDoubleTest {

    /**
     * Tests {@link LongPredicates#and(LongPredicate, LongPredicate)}.
     */
    @Test
    public void and() {
        assertTrue(PrimitivePredicates.and(DOUBLE_TRUE, DOUBLE_TRUE).op(1D));
        assertFalse(PrimitivePredicates.and(DOUBLE_TRUE, DOUBLE_FALSE).op(1D));
        assertFalse(PrimitivePredicates.and(DOUBLE_FALSE, DOUBLE_TRUE).op(1D));
        assertFalse(PrimitivePredicates.and(DOUBLE_FALSE, DOUBLE_FALSE).op(1D));

        AndDoublePredicate p = new AndDoublePredicate(DOUBLE_FALSE, DOUBLE_TRUE);
        assertSame(p.getLeft(), DOUBLE_FALSE);
        assertSame(p.getRight(), DOUBLE_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.and(DOUBLE_FALSE, TestUtil.dummy(DoublePredicate.class)).op(1D);
    }

    /**
     * Tests that {@link DoublePredicates#and(DoublePredicate, DoublePredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE() {
        PrimitivePredicates.and(null, DOUBLE_TRUE);
    }

    /**
     * Tests that {@link DoublePredicates#and(DoublePredicate, DoublePredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE1() {
        PrimitivePredicates.and(DOUBLE_TRUE, null);
    }
    
    /* Test greater then */
    @Test
    public void equalsTo() {
        DoublePredicate f = PrimitivePredicates.equalsTo(5D);
        assertEquals(5D, new EqualsToDoublePredicate(5D).getEqualsTo(),0);
        assertFalse(f.op(4D));
        assertTrue(f.op(5D));
        assertFalse(f.op(6D));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
    
    /**
     * Tests {@link DoublePredicates#FALSE}.
     */
    @Test
    public void falsePredicate() {
        assertFalse(DOUBLE_FALSE.op(2D));
        assertFalse(DOUBLE_FALSE.op(Double.MIN_VALUE));
        DOUBLE_FALSE.toString(); // does not fail
        assertIsSerializable(DOUBLE_FALSE);
        assertSame(DOUBLE_FALSE, TestUtil.serializeAndUnserialize(DOUBLE_FALSE));
    }
   
    /* Test greater then */
    @Test
    public void greaterThen() {
        DoublePredicate f = PrimitivePredicates.greaterThen(5D);
        assertEquals(5D, new GreaterThenDoublePredicate(5D).getGreaterThen(),0);
        assertFalse(f.op(4D));
        assertFalse(f.op(5D));
        assertTrue(f.op(6D));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    @Test
    public void greaterThenOrEquals() {
        DoublePredicate f = PrimitivePredicates.greaterThenOrEquals(5D);
        assertEquals(5D, new GreaterThenOrEqualsDoublePredicate(5D).getGreaterThenOrEquals(),0);
        assertFalse(f.op(4D));
        assertTrue(f.op(5D));
        assertTrue(f.op(6D));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThen() {
        DoublePredicate f = PrimitivePredicates.lessThen(5D);
        assertEquals(5D, new LessThenDoublePredicate(5D).getLessThen(),0);
        assertTrue(f.op(4D));
        assertFalse(f.op(5D));
        assertFalse(f.op(6D));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThenOrEquals() {
        DoublePredicate f = PrimitivePredicates.lessThenOrEquals(5D);
        assertEquals(5D, new LessThenOrEqualsDoublePredicate(5D).getLessThenOrEquals(),0);
        assertTrue(f.op(4D));
        assertTrue(f.op(5D));
        assertFalse(f.op(6D));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
   
   
     /**
     * Tests that {@link DoublePredicates#not(DoublePredicate)} throws a
     * {@link NullPointerException} when invoked with a <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void notNPE() {
        PrimitivePredicates.not((DoublePredicate) null);
    }

    /**
     * Tests {@link DoublePredicates#TRUE}.
     */
    @Test
    public void notPredicate() {
        assertFalse(PrimitivePredicates.not(DOUBLE_TRUE).op(2D));
        assertTrue(PrimitivePredicates.not(DOUBLE_FALSE).op(2D));
        PrimitivePredicates.not(DOUBLE_TRUE).toString(); // does not fail
        assertIsSerializable(PrimitivePredicates.not(DOUBLE_TRUE));
        assertSame(DOUBLE_TRUE, ((NotDoublePredicate) PrimitivePredicates.not(DOUBLE_TRUE)).getPredicate());
    }

    /**
     * Tests {@link DoublePredicates#or(DoublePredicate, DoublePredicate)}.
     */
    @Test
    public void or() {
        assertTrue(PrimitivePredicates.or(DOUBLE_TRUE, DOUBLE_TRUE).op(1D));
        assertTrue(PrimitivePredicates.or(DOUBLE_TRUE, DOUBLE_FALSE).op(1D));
        assertTrue(PrimitivePredicates.or(DOUBLE_FALSE, DOUBLE_TRUE).op(1D));
        assertFalse(PrimitivePredicates.or(DOUBLE_FALSE, DOUBLE_FALSE).op(1D));

        OrDoublePredicate p = new OrDoublePredicate(DOUBLE_FALSE, DOUBLE_TRUE);
        assertSame(p.getLeft(), DOUBLE_FALSE);
        assertSame(p.getRight(), DOUBLE_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.or(DOUBLE_TRUE, TestUtil.dummy(DoublePredicate.class)).op(1D);
    }

    /**
     * Tests that {@link DoublePredicates#or(DoublePredicate, DoublePredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE() {
        PrimitivePredicates.or(null, DOUBLE_TRUE);
    }

    /**
     * Tests that {@link DoublePredicates#or(DoublePredicate, DoublePredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE1() {
        PrimitivePredicates.or(DOUBLE_TRUE, null);
    }
   
    
   /**
     * Tests {@link DoublePredicates#TRUE}.
     */
    @Test
    public void truePredicate() {
        assertTrue(DOUBLE_TRUE.op(2D));
        assertTrue(DOUBLE_TRUE.op(Double.MIN_VALUE));
        DOUBLE_TRUE.toString(); // does not fail
        assertIsSerializable(DOUBLE_TRUE);
        assertSame(DOUBLE_TRUE, TestUtil.serializeAndUnserialize(DOUBLE_TRUE));
    }
}