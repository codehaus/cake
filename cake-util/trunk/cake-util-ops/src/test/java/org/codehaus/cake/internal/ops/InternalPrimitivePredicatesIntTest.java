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
package org.codehaus.cake.internal.ops;

import org.codehaus.cake.ops.PrimitivePredicates;
import static org.codehaus.cake.ops.PrimitivePredicates.*;
import static org.codehaus.cake.internal.ops.InternalPrimitivePredicates.*;
import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.junit.Assert.*;

import org.codehaus.cake.ops.Ops.*;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Test;
/**
 * Various tests for {@link IntPredicates}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitivePredicatesIntTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitivePredicatesIntTest {

    /**
     * Tests {@link LongPredicates#and(LongPredicate, LongPredicate)}.
     */
    @Test
    public void and() {
        assertTrue(PrimitivePredicates.and(INT_TRUE, INT_TRUE).op(1));
        assertFalse(PrimitivePredicates.and(INT_TRUE, INT_FALSE).op(1));
        assertFalse(PrimitivePredicates.and(INT_FALSE, INT_TRUE).op(1));
        assertFalse(PrimitivePredicates.and(INT_FALSE, INT_FALSE).op(1));

        AndIntPredicate p = new AndIntPredicate(INT_FALSE, INT_TRUE);
        assertSame(p.getLeft(), INT_FALSE);
        assertSame(p.getRight(), INT_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.and(INT_FALSE, TestUtil.dummy(IntPredicate.class)).op(1);
    }

    /**
     * Tests that {@link IntPredicates#and(IntPredicate, IntPredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE() {
        PrimitivePredicates.and(null, INT_TRUE);
    }

    /**
     * Tests that {@link IntPredicates#and(IntPredicate, IntPredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE1() {
        PrimitivePredicates.and(INT_TRUE, null);
    }
    
    /* Test greater then */
    @Test
    public void equalsTo() {
        IntPredicate f = PrimitivePredicates.equalsTo(5);
        assertEquals(5, new EqualsToIntPredicate(5).getEqualsTo());
        assertFalse(f.op(4));
        assertTrue(f.op(5));
        assertFalse(f.op(6));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
    
    /**
     * Tests {@link IntPredicates#FALSE}.
     */
    @Test
    public void falsePredicate() {
        assertFalse(INT_FALSE.op(2));
        assertFalse(INT_FALSE.op(Integer.MIN_VALUE));
        INT_FALSE.toString(); // does not fail
        assertIsSerializable(INT_FALSE);
        assertSame(INT_FALSE, TestUtil.serializeAndUnserialize(INT_FALSE));
    }
   
    /* Test greater then */
    @Test
    public void greaterThen() {
        IntPredicate f = PrimitivePredicates.greaterThen(5);
        assertEquals(5, new GreaterThenIntPredicate(5).getGreaterThen());
        assertFalse(f.op(4));
        assertFalse(f.op(5));
        assertTrue(f.op(6));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    @Test
    public void greaterThenOrEquals() {
        IntPredicate f = PrimitivePredicates.greaterThenOrEquals(5);
        assertEquals(5, new GreaterThenOrEqualsIntPredicate(5).getGreaterThenOrEquals());
        assertFalse(f.op(4));
        assertTrue(f.op(5));
        assertTrue(f.op(6));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThen() {
        IntPredicate f = PrimitivePredicates.lessThen(5);
        assertEquals(5, new LessThenIntPredicate(5).getLessThen());
        assertTrue(f.op(4));
        assertFalse(f.op(5));
        assertFalse(f.op(6));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThenOrEquals() {
        IntPredicate f = PrimitivePredicates.lessThenOrEquals(5);
        assertEquals(5, new LessThenOrEqualsIntPredicate(5).getLessThenOrEquals());
        assertTrue(f.op(4));
        assertTrue(f.op(5));
        assertFalse(f.op(6));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
   
   
     /**
     * Tests that {@link IntPredicates#not(IntPredicate)} throws a
     * {@link NullPointerException} when invoked with a <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void notNPE() {
        PrimitivePredicates.not((IntPredicate) null);
    }

    /**
     * Tests {@link IntPredicates#TRUE}.
     */
    @Test
    public void notPredicate() {
        assertFalse(PrimitivePredicates.not(INT_TRUE).op(2));
        assertTrue(PrimitivePredicates.not(INT_FALSE).op(2));
        PrimitivePredicates.not(INT_TRUE).toString(); // does not fail
        assertIsSerializable(PrimitivePredicates.not(INT_TRUE));
        assertSame(INT_TRUE, ((NotIntPredicate) PrimitivePredicates.not(INT_TRUE)).getPredicate());
    }

    /**
     * Tests {@link IntPredicates#or(IntPredicate, IntPredicate)}.
     */
    @Test
    public void or() {
        assertTrue(PrimitivePredicates.or(INT_TRUE, INT_TRUE).op(1));
        assertTrue(PrimitivePredicates.or(INT_TRUE, INT_FALSE).op(1));
        assertTrue(PrimitivePredicates.or(INT_FALSE, INT_TRUE).op(1));
        assertFalse(PrimitivePredicates.or(INT_FALSE, INT_FALSE).op(1));

        OrIntPredicate p = new OrIntPredicate(INT_FALSE, INT_TRUE);
        assertSame(p.getLeft(), INT_FALSE);
        assertSame(p.getRight(), INT_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.or(INT_TRUE, TestUtil.dummy(IntPredicate.class)).op(1);
    }

    /**
     * Tests that {@link IntPredicates#or(IntPredicate, IntPredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE() {
        PrimitivePredicates.or(null, INT_TRUE);
    }

    /**
     * Tests that {@link IntPredicates#or(IntPredicate, IntPredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE1() {
        PrimitivePredicates.or(INT_TRUE, null);
    }
   
    
   /**
     * Tests {@link IntPredicates#TRUE}.
     */
    @Test
    public void truePredicate() {
        assertTrue(INT_TRUE.op(2));
        assertTrue(INT_TRUE.op(Integer.MIN_VALUE));
        INT_TRUE.toString(); // does not fail
        assertIsSerializable(INT_TRUE);
        assertSame(INT_TRUE, TestUtil.serializeAndUnserialize(INT_TRUE));
    }
}