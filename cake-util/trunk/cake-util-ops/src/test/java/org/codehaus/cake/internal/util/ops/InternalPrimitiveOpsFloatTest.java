/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
/*  This class is automatically generated */ 
package org.codehaus.cake.internal.util.ops;

import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.serializeAndUnserialize;
import static org.codehaus.cake.util.ops.PrimitiveOps.FLOAT_ADD_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.FLOAT_DIVISION_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.FLOAT_MAX_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.FLOAT_MIN_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.FLOAT_MULTIPLY_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.FLOAT_SUBTRACT_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatAdd;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatConstant;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatDivide;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatMax;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatMin;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatMultiply;
import static org.codehaus.cake.util.ops.PrimitiveOps.floatSubtract;
import static org.junit.Assert.assertEquals;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.ops.Comparators;
import org.codehaus.cake.util.ops.PrimitiveOps;
import org.codehaus.cake.util.ops.Ops.FloatReducer;
import org.junit.Test;
/**
 * Various tests for {@link FloatOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitiveOpsFloatTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitiveOpsFloatTest {

    /**
     * Tests {@link FloatOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(1F, PrimitiveOps.FLOAT_ABS_OP.op(-1F),0);
        assertEquals(1F, PrimitiveOps.FLOAT_ABS_OP.op(1F),0);
        assertEquals(Float.POSITIVE_INFINITY, PrimitiveOps.FLOAT_ABS_OP.op(Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, PrimitiveOps.FLOAT_ABS_OP.op(Float.NaN),0);
        PrimitiveOps.FLOAT_ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(PrimitiveOps.FLOAT_ABS_OP);
    }
    
   /**
     * Tests {@link IntOps#constant(int)}.
     */
    @Test
    public void constant() {
        assertEquals(1F, floatConstant(1).op(0),0);
        assertEquals(1F, floatConstant(1).op(1),0);
        assertIsSerializable(floatConstant(1));
        assertEquals(2F, serializeAndUnserialize(floatConstant(2)).op(1),0);
    }

     /**
     * Tests {@link FloatOps#MIN_REDUCER}.
     */
    @Test
    public void min() {
        assertEquals(1F, FLOAT_MIN_REDUCER.op(1F, 2F),0);
        assertEquals(1F, FLOAT_MIN_REDUCER.op(2F, 1F),0);
        assertEquals(1F, FLOAT_MIN_REDUCER.op(1F, 1F),0);
        assertEquals(1F, FLOAT_MIN_REDUCER.op(1F, Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, FLOAT_MIN_REDUCER.op(1F, Float.NaN),0);
        FLOAT_MIN_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_MIN_REDUCER);
    }


    /**
     * Tests {@link FloatOps#min}.
     */
    @Test
    public void minArg() {
        FloatReducer r = floatMin(Comparators.FLOAT_NATURAL_COMPARATOR);
        assertEquals(1F, r.op(1F, 2F),0);
        assertEquals(1F, r.op(2F, 1F),0);
        assertEquals(1F, r.op(1F, 1F),0);
        assertEquals(1F, r.op(1F, Float.POSITIVE_INFINITY),0);
        // System.out.println(Double.compare(1, Double.NaN));
        // System.out.println(Double.compare(Double.NaN,1));
        assertEquals(1F, r.op(Float.NaN, 1F),0);
        r.toString(); // does not fail
        assertEquals(1F, serializeAndUnserialize(r).op(1F, 2F),0);
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void minNPE() {
        floatMin(null);
    }
    
        /**
     * Tests {@link Reducers#MAX_REDUCER}.
     */
    @Test
    public void doubleMaxReducer() {
        assertEquals(2F, FLOAT_MAX_REDUCER.op(2F, 1F),0);
        assertEquals(2F, FLOAT_MAX_REDUCER.op(1F, 2F),0);
        assertEquals(2F, FLOAT_MAX_REDUCER.op(2F, 2F),0);
        assertEquals(Float.POSITIVE_INFINITY, FLOAT_MAX_REDUCER.op(1F, Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, FLOAT_MAX_REDUCER.op(1F, Float.NaN),0);
        FLOAT_MAX_REDUCER.toString(); // does not fail
        assertIsSerializable(FLOAT_MAX_REDUCER);
        TestUtil.assertSingletonSerializable(FLOAT_MAX_REDUCER);
    }

    /**
     * Tests
     * {@link Reducers#doubleMaxReducer(org.codehaus.cake.util.ops.Ops.DoubleComparator)}
     */
    @Test
    public void doubleMaxReducerComparator() {
        FloatReducer r = floatMax(Comparators.FLOAT_NATURAL_COMPARATOR);
        assertEquals(2F, r.op(1F, 2F),0);
        assertEquals(2F, r.op(2F, 1F),0);
        assertEquals(2F, r.op(2F, 2F),0);
        assertEquals(Float.POSITIVE_INFINITY, r.op(1F, Float.POSITIVE_INFINITY), 0);
        assertEquals(Float.NaN, r.op(1F, Float.NaN), 0);
assertEquals(2F, serializeAndUnserialize(r).op(1F, 2F),0);
        r.toString(); // does not fail
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void maxNPE() {
        floatMax(null);
    }

   /**
     * Tests {@link FloatOps#ADD_REDUCER} and {@link FloatOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(3F, FLOAT_ADD_REDUCER.op(1F, 2F),0);
        assertEquals(3F, FLOAT_ADD_REDUCER.op(2F, 1F),0);
        assertEquals(Float.POSITIVE_INFINITY, FLOAT_ADD_REDUCER.op(1, Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, FLOAT_ADD_REDUCER.op(1, Float.NaN),0);
        FLOAT_ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_ADD_REDUCER);
    }

    /**
     * Tests {@link FloatOps#add(float)}.
     */
    @Test
    public void addArg() {
        assertEquals(9F, floatAdd(5F).op(4F),0);
        assertEquals(9F, floatAdd(4F).op(5F),0);
        assertEquals(Float.POSITIVE_INFINITY, floatAdd(5).op(Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, floatAdd(5).op(Float.NaN),0);
        floatAdd(9F).toString(); // does not fail
        assertIsSerializable(floatAdd(5F));
        assertEquals(-9F, serializeAndUnserialize(floatAdd(12F)).op(-21F),0);
    }
    
   /**
     * Tests {@link FloatOps#DIVIDE_REDUCER} and {@link FloatOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(4F, FLOAT_DIVISION_REDUCER.op(16F, 4F),0);
        assertEquals(-4F, FLOAT_DIVISION_REDUCER.op(-8F, 2F),0);
        assertEquals(Float.POSITIVE_INFINITY, FLOAT_DIVISION_REDUCER.op(Float.POSITIVE_INFINITY,1),0);
        assertEquals(Float.NaN, FLOAT_DIVISION_REDUCER.op(1, Float.NaN),0);
        FLOAT_DIVISION_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_DIVISION_REDUCER);
    }

    /**
     * Tests {@link FloatOps#divide(float)}.
     */
    @Test
    public void divideArg() {
        assertEquals(-2F, floatDivide(4F).op(-8F),0);
        assertEquals(5F, floatDivide(5F).op(25F),0);
        assertEquals(Float.POSITIVE_INFINITY, floatDivide(5).op(Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, floatDivide(5).op(Float.NaN),0);
        floatDivide(9F).toString(); // does not fail
        assertIsSerializable(floatDivide(5F));
        assertEquals(-4F, serializeAndUnserialize(floatDivide(4F)).op(-16F),0);
    }
    
       
       /**
     * Tests {@link FloatOps#MULTIPLY_REDUCER} and {@link FloatOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(16F, FLOAT_MULTIPLY_REDUCER.op(4F, 4F),0);
        assertEquals(-8F, FLOAT_MULTIPLY_REDUCER.op(-4F, 2F),0);
        assertEquals(Float.POSITIVE_INFINITY,FLOAT_MULTIPLY_REDUCER.op(Float.POSITIVE_INFINITY,1),0);
        assertEquals(Float.NaN, FLOAT_MULTIPLY_REDUCER.op(1, Float.NaN),0);
         FLOAT_MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link FloatOps#multiply(float)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(-8F, floatMultiply(4F).op(-2F),0);
        assertEquals(25F, floatMultiply(5F).op(5F),0);
        assertEquals(Float.POSITIVE_INFINITY, floatMultiply(5).op(Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, floatMultiply(5).op(Float.NaN),0);
        floatMultiply(9F).toString(); // does not fail
        assertIsSerializable(floatMultiply(5F));
        assertEquals(-16F, serializeAndUnserialize(floatMultiply(4F)).op(-4F),0);
    }
    
        /**
     * Tests {@link FloatOps#SUBTRACT_REDUCER} and {@link FloatOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(-1F, FLOAT_SUBTRACT_REDUCER.op(1F, 2F),0);
        assertEquals(1F, FLOAT_SUBTRACT_REDUCER.op(2F, 1F),0);
        assertEquals(Float.NEGATIVE_INFINITY, FLOAT_SUBTRACT_REDUCER.op(1, Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, FLOAT_SUBTRACT_REDUCER.op(1, Float.NaN),0);
        FLOAT_SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link FloatOps#subtract(float)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(-1F, floatSubtract(5F).op(4F),0);
        assertEquals(1F, floatSubtract(4F).op(5F),0);
        assertEquals(Float.POSITIVE_INFINITY, floatSubtract(5).op(Float.POSITIVE_INFINITY),0);
        assertEquals(Float.NaN, floatSubtract(5).op(Float.NaN),0);
        floatSubtract(9F).toString(); // does not fail
        assertIsSerializable(floatSubtract(5F));
        assertEquals(-33F, serializeAndUnserialize(floatSubtract(12F)).op(-21F),0);
    }    
}