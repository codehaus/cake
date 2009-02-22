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

import org.codehaus.cake.ops.PrimitiveOps;
import static org.codehaus.cake.ops.PrimitiveOps.*;
import static org.codehaus.cake.ops.Comparators.*;
import org.codehaus.cake.ops.Comparators;
import static org.codehaus.cake.internal.ops.InternalPrimitiveOps.*;
import static org.codehaus.cake.test.util.TestUtil.*;
import static org.junit.Assert.*;

import org.codehaus.cake.ops.Ops.*;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Test;
/**
 * Various tests for {@link LongOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitiveOpsLongTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitiveOpsLongTest {

    /**
     * Tests {@link LongOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(1L, PrimitiveOps.LONG_ABS_OP.op(-1L));
        assertEquals(1L, PrimitiveOps.LONG_ABS_OP.op(1L));
        PrimitiveOps.LONG_ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(PrimitiveOps.LONG_ABS_OP);
    }
    
   /**
     * Tests {@link IntOps#constant(int)}.
     */
    @Test
    public void constant() {
        assertEquals(1L, longConstant(1).op(0));
        assertEquals(1L, longConstant(1).op(1));
        assertIsSerializable(longConstant(1));
        assertEquals(2L, serializeAndUnserialize(longConstant(2)).op(1));
    }

     /**
     * Tests {@link LongOps#MIN_REDUCER}.
     */
    @Test
    public void min() {
        assertEquals(1L, LONG_MIN_REDUCER.op(1L, 2L));
        assertEquals(1L, LONG_MIN_REDUCER.op(2L, 1L));
        assertEquals(1L, LONG_MIN_REDUCER.op(1L, 1L));
        LONG_MIN_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_MIN_REDUCER);
    }


    /**
     * Tests {@link LongOps#min}.
     */
    @Test
    public void minArg() {
        LongReducer r = longMin(Comparators.LONG_NATURAL_COMPARATOR);
        assertEquals(1L, r.op(1L, 2L));
        assertEquals(1L, r.op(2L, 1L));
        assertEquals(1L, r.op(1L, 1L));
        r.toString(); // does not fail
        assertEquals(1L, serializeAndUnserialize(r).op(1L, 2L));
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void minNPE() {
        longMin(null);
    }
    
        /**
     * Tests {@link Reducers#MAX_REDUCER}.
     */
    @Test
    public void doubleMaxReducer() {
        assertEquals(2L, LONG_MAX_REDUCER.op(2L, 1L));
        assertEquals(2L, LONG_MAX_REDUCER.op(1L, 2L));
        assertEquals(2L, LONG_MAX_REDUCER.op(2L, 2L));
        LONG_MAX_REDUCER.toString(); // does not fail
        assertIsSerializable(LONG_MAX_REDUCER);
        TestUtil.assertSingletonSerializable(LONG_MAX_REDUCER);
    }

    /**
     * Tests
     * {@link Reducers#doubleMaxReducer(org.codehaus.cake.ops.Ops.DoubleComparator)}
     */
    @Test
    public void doubleMaxReducerComparator() {
        LongReducer r = longMax(Comparators.LONG_NATURAL_COMPARATOR);
        assertEquals(2L, r.op(1L, 2L));
        assertEquals(2L, r.op(2L, 1L));
        assertEquals(2L, r.op(2L, 2L));
assertEquals(2L, serializeAndUnserialize(r).op(1L, 2L));
        r.toString(); // does not fail
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void maxNPE() {
        longMax(null);
    }

   /**
     * Tests {@link LongOps#ADD_REDUCER} and {@link LongOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(3L, LONG_ADD_REDUCER.op(1L, 2L));
        assertEquals(3L, LONG_ADD_REDUCER.op(2L, 1L));
        LONG_ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_ADD_REDUCER);
    }

    /**
     * Tests {@link LongOps#add(long)}.
     */
    @Test
    public void addArg() {
        assertEquals(9L, longAdd(5L).op(4L));
        assertEquals(9L, longAdd(4L).op(5L));
        longAdd(9L).toString(); // does not fail
        assertIsSerializable(longAdd(5L));
        assertEquals(-9L, serializeAndUnserialize(longAdd(12L)).op(-21L));
    }
    
   /**
     * Tests {@link LongOps#DIVIDE_REDUCER} and {@link LongOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(4L, LONG_DIVISION_REDUCER.op(16L, 4L));
        assertEquals(-4L, LONG_DIVISION_REDUCER.op(-8L, 2L));
        LONG_DIVISION_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_DIVISION_REDUCER);
    }

    /**
     * Tests {@link LongOps#divide(long)}.
     */
    @Test
    public void divideArg() {
        assertEquals(-2L, longDivide(4L).op(-8L));
        assertEquals(5L, longDivide(5L).op(25L));
        longDivide(9L).toString(); // does not fail
        assertIsSerializable(longDivide(5L));
        assertEquals(-4L, serializeAndUnserialize(longDivide(4L)).op(-16L));
    }
    
       
       /**
     * Tests {@link LongOps#MULTIPLY_REDUCER} and {@link LongOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(16L, LONG_MULTIPLY_REDUCER.op(4L, 4L));
        assertEquals(-8L, LONG_MULTIPLY_REDUCER.op(-4L, 2L));
         LONG_MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link LongOps#multiply(long)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(-8L, longMultiply(4L).op(-2L));
        assertEquals(25L, longMultiply(5L).op(5L));
        longMultiply(9L).toString(); // does not fail
        assertIsSerializable(longMultiply(5L));
        assertEquals(-16L, serializeAndUnserialize(longMultiply(4L)).op(-4L));
    }
    
        /**
     * Tests {@link LongOps#SUBTRACT_REDUCER} and {@link LongOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(-1L, LONG_SUBTRACT_REDUCER.op(1L, 2L));
        assertEquals(1L, LONG_SUBTRACT_REDUCER.op(2L, 1L));
        LONG_SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link LongOps#subtract(long)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(-1L, longSubtract(5L).op(4L));
        assertEquals(1L, longSubtract(4L).op(5L));
        longSubtract(9L).toString(); // does not fail
        assertIsSerializable(longSubtract(5L));
        assertEquals(-33L, serializeAndUnserialize(longSubtract(12L)).op(-21L));
    }    
}