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
 * Various tests for {@link LongPredicates}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitivePredicatesLongTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitivePredicatesLongTest {

    /**
     * Tests {@link LongPredicates#and(LongPredicate, LongPredicate)}.
     */
    @Test
    public void and() {
        assertTrue(PrimitivePredicates.and(LONG_TRUE, LONG_TRUE).op(1L));
        assertFalse(PrimitivePredicates.and(LONG_TRUE, LONG_FALSE).op(1L));
        assertFalse(PrimitivePredicates.and(LONG_FALSE, LONG_TRUE).op(1L));
        assertFalse(PrimitivePredicates.and(LONG_FALSE, LONG_FALSE).op(1L));

        AndLongPredicate p = new AndLongPredicate(LONG_FALSE, LONG_TRUE);
        assertSame(p.getLeft(), LONG_FALSE);
        assertSame(p.getRight(), LONG_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.and(LONG_FALSE, TestUtil.dummy(LongPredicate.class)).op(1L);
    }

    /**
     * Tests that {@link LongPredicates#and(LongPredicate, LongPredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE() {
        PrimitivePredicates.and(null, LONG_TRUE);
    }

    /**
     * Tests that {@link LongPredicates#and(LongPredicate, LongPredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE1() {
        PrimitivePredicates.and(LONG_TRUE, null);
    }
    
    /* Test greater then */
    @Test
    public void equalsTo() {
        LongPredicate f = PrimitivePredicates.equalsTo(5L);
        assertEquals(5L, new EqualsToLongPredicate(5L).getEqualsTo());
        assertFalse(f.op(4L));
        assertTrue(f.op(5L));
        assertFalse(f.op(6L));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
    
    /**
     * Tests {@link LongPredicates#FALSE}.
     */
    @Test
    public void falsePredicate() {
        assertFalse(LONG_FALSE.op(2L));
        assertFalse(LONG_FALSE.op(Long.MIN_VALUE));
        LONG_FALSE.toString(); // does not fail
        assertIsSerializable(LONG_FALSE);
        assertSame(LONG_FALSE, TestUtil.serializeAndUnserialize(LONG_FALSE));
    }
   
    /* Test greater then */
    @Test
    public void greaterThen() {
        LongPredicate f = PrimitivePredicates.greaterThen(5L);
        assertEquals(5L, new GreaterThenLongPredicate(5L).getGreaterThen());
        assertFalse(f.op(4L));
        assertFalse(f.op(5L));
        assertTrue(f.op(6L));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    @Test
    public void greaterThenOrEquals() {
        LongPredicate f = PrimitivePredicates.greaterThenOrEquals(5L);
        assertEquals(5L, new GreaterThenOrEqualsLongPredicate(5L).getGreaterThenOrEquals());
        assertFalse(f.op(4L));
        assertTrue(f.op(5L));
        assertTrue(f.op(6L));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThen() {
        LongPredicate f = PrimitivePredicates.lessThen(5L);
        assertEquals(5L, new LessThenLongPredicate(5L).getLessThen());
        assertTrue(f.op(4L));
        assertFalse(f.op(5L));
        assertFalse(f.op(6L));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }

    /* Test greater then */
    @Test
    public void lessThenOrEquals() {
        LongPredicate f = PrimitivePredicates.lessThenOrEquals(5L);
        assertEquals(5L, new LessThenOrEqualsLongPredicate(5L).getLessThenOrEquals());
        assertTrue(f.op(4L));
        assertTrue(f.op(5L));
        assertFalse(f.op(6L));

        f.toString(); // no exceptions

        TestUtil.assertIsSerializable(f);
    }
   
   
     /**
     * Tests that {@link LongPredicates#not(LongPredicate)} throws a
     * {@link NullPointerException} when invoked with a <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void notNPE() {
        PrimitivePredicates.not((LongPredicate) null);
    }

    /**
     * Tests {@link LongPredicates#TRUE}.
     */
    @Test
    public void notPredicate() {
        assertFalse(PrimitivePredicates.not(LONG_TRUE).op(2L));
        assertTrue(PrimitivePredicates.not(LONG_FALSE).op(2L));
        PrimitivePredicates.not(LONG_TRUE).toString(); // does not fail
        assertIsSerializable(PrimitivePredicates.not(LONG_TRUE));
        assertSame(LONG_TRUE, ((NotLongPredicate) PrimitivePredicates.not(LONG_TRUE)).getPredicate());
    }

    /**
     * Tests {@link LongPredicates#or(LongPredicate, LongPredicate)}.
     */
    @Test
    public void or() {
        assertTrue(PrimitivePredicates.or(LONG_TRUE, LONG_TRUE).op(1L));
        assertTrue(PrimitivePredicates.or(LONG_TRUE, LONG_FALSE).op(1L));
        assertTrue(PrimitivePredicates.or(LONG_FALSE, LONG_TRUE).op(1L));
        assertFalse(PrimitivePredicates.or(LONG_FALSE, LONG_FALSE).op(1L));

        OrLongPredicate p = new OrLongPredicate(LONG_FALSE, LONG_TRUE);
        assertSame(p.getLeft(), LONG_FALSE);
        assertSame(p.getRight(), LONG_TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        PrimitivePredicates.or(LONG_TRUE, TestUtil.dummy(LongPredicate.class)).op(1L);
    }

    /**
     * Tests that {@link LongPredicates#or(LongPredicate, LongPredicate)} throws a
     * {@link NullPointerException} when invoked with a left side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE() {
        PrimitivePredicates.or(null, LONG_TRUE);
    }

    /**
     * Tests that {@link LongPredicates#or(LongPredicate, LongPredicate)} throws a
     * {@link NullPointerException} when invoked with a right side <code>null</code>
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE1() {
        PrimitivePredicates.or(LONG_TRUE, null);
    }
   
    
   /**
     * Tests {@link LongPredicates#TRUE}.
     */
    @Test
    public void truePredicate() {
        assertTrue(LONG_TRUE.op(2L));
        assertTrue(LONG_TRUE.op(Long.MIN_VALUE));
        LONG_TRUE.toString(); // does not fail
        assertIsSerializable(LONG_TRUE);
        assertSame(LONG_TRUE, TestUtil.serializeAndUnserialize(LONG_TRUE));
    }
}