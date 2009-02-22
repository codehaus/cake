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
 * Various tests for {@link IntOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitiveOpsIntTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitiveOpsIntTest {

    /**
     * Tests {@link IntOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(1, PrimitiveOps.INT_ABS_OP.op(-1));
        assertEquals(1, PrimitiveOps.INT_ABS_OP.op(1));
        PrimitiveOps.INT_ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(PrimitiveOps.INT_ABS_OP);
    }
    
   /**
     * Tests {@link IntOps#constant(int)}.
     */
    @Test
    public void constant() {
        assertEquals(1, intConstant(1).op(0));
        assertEquals(1, intConstant(1).op(1));
        assertIsSerializable(intConstant(1));
        assertEquals(2, serializeAndUnserialize(intConstant(2)).op(1));
    }

     /**
     * Tests {@link IntOps#MIN_REDUCER}.
     */
    @Test
    public void min() {
        assertEquals(1, INT_MIN_REDUCER.op(1, 2));
        assertEquals(1, INT_MIN_REDUCER.op(2, 1));
        assertEquals(1, INT_MIN_REDUCER.op(1, 1));
        INT_MIN_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_MIN_REDUCER);
    }


    /**
     * Tests {@link IntOps#min}.
     */
    @Test
    public void minArg() {
        IntReducer r = intMin(Comparators.INT_NATURAL_COMPARATOR);
        assertEquals(1, r.op(1, 2));
        assertEquals(1, r.op(2, 1));
        assertEquals(1, r.op(1, 1));
        r.toString(); // does not fail
        assertEquals(1, serializeAndUnserialize(r).op(1, 2));
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void minNPE() {
        intMin(null);
    }
    
        /**
     * Tests {@link Reducers#MAX_REDUCER}.
     */
    @Test
    public void doubleMaxReducer() {
        assertEquals(2, INT_MAX_REDUCER.op(2, 1));
        assertEquals(2, INT_MAX_REDUCER.op(1, 2));
        assertEquals(2, INT_MAX_REDUCER.op(2, 2));
        INT_MAX_REDUCER.toString(); // does not fail
        assertIsSerializable(INT_MAX_REDUCER);
        TestUtil.assertSingletonSerializable(INT_MAX_REDUCER);
    }

    /**
     * Tests
     * {@link Reducers#doubleMaxReducer(org.codehaus.cake.ops.Ops.DoubleComparator)}
     */
    @Test
    public void doubleMaxReducerComparator() {
        IntReducer r = intMax(Comparators.INT_NATURAL_COMPARATOR);
        assertEquals(2, r.op(1, 2));
        assertEquals(2, r.op(2, 1));
        assertEquals(2, r.op(2, 2));
assertEquals(2, serializeAndUnserialize(r).op(1, 2));
        r.toString(); // does not fail
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void maxNPE() {
        intMax(null);
    }

   /**
     * Tests {@link IntOps#ADD_REDUCER} and {@link IntOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(3, INT_ADD_REDUCER.op(1, 2));
        assertEquals(3, INT_ADD_REDUCER.op(2, 1));
        INT_ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_ADD_REDUCER);
    }

    /**
     * Tests {@link IntOps#add(int)}.
     */
    @Test
    public void addArg() {
        assertEquals(9, intAdd(5).op(4));
        assertEquals(9, intAdd(4).op(5));
        intAdd(9).toString(); // does not fail
        assertIsSerializable(intAdd(5));
        assertEquals(-9, serializeAndUnserialize(intAdd(12)).op(-21));
    }
    
   /**
     * Tests {@link IntOps#DIVIDE_REDUCER} and {@link IntOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(4, INT_DIVISION_REDUCER.op(16, 4));
        assertEquals(-4, INT_DIVISION_REDUCER.op(-8, 2));
        INT_DIVISION_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_DIVISION_REDUCER);
    }

    /**
     * Tests {@link IntOps#divide(int)}.
     */
    @Test
    public void divideArg() {
        assertEquals(-2, intDivide(4).op(-8));
        assertEquals(5, intDivide(5).op(25));
        intDivide(9).toString(); // does not fail
        assertIsSerializable(intDivide(5));
        assertEquals(-4, serializeAndUnserialize(intDivide(4)).op(-16));
    }
    
       
       /**
     * Tests {@link IntOps#MULTIPLY_REDUCER} and {@link IntOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(16, INT_MULTIPLY_REDUCER.op(4, 4));
        assertEquals(-8, INT_MULTIPLY_REDUCER.op(-4, 2));
         INT_MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link IntOps#multiply(int)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(-8, intMultiply(4).op(-2));
        assertEquals(25, intMultiply(5).op(5));
        intMultiply(9).toString(); // does not fail
        assertIsSerializable(intMultiply(5));
        assertEquals(-16, serializeAndUnserialize(intMultiply(4)).op(-4));
    }
    
        /**
     * Tests {@link IntOps#SUBTRACT_REDUCER} and {@link IntOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(-1, INT_SUBTRACT_REDUCER.op(1, 2));
        assertEquals(1, INT_SUBTRACT_REDUCER.op(2, 1));
        INT_SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link IntOps#subtract(int)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(-1, intSubtract(5).op(4));
        assertEquals(1, intSubtract(4).op(5));
        intSubtract(9).toString(); // does not fail
        assertIsSerializable(intSubtract(5));
        assertEquals(-33, serializeAndUnserialize(intSubtract(12)).op(-21));
    }    
}