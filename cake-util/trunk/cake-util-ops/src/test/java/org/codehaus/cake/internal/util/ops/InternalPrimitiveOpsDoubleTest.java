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
import static org.codehaus.cake.util.ops.PrimitiveOps.DOUBLE_ADD_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.DOUBLE_DIVISION_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.DOUBLE_MAX_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.DOUBLE_MIN_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.DOUBLE_MULTIPLY_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.DOUBLE_SUBTRACT_REDUCER;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleAdd;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleConstant;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleDivide;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleMax;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleMin;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleMultiply;
import static org.codehaus.cake.util.ops.PrimitiveOps.doubleSubtract;
import static org.junit.Assert.assertEquals;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.ops.Comparators;
import org.codehaus.cake.util.ops.PrimitiveOps;
import org.codehaus.cake.util.ops.Ops.DoubleReducer;
import org.junit.Test;
/**
 * Various tests for {@link DoubleOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TYPEpredicatesTest.vm 227 2008-11-30 22:13:02Z kasper InternalPrimitiveOpsDoubleTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class InternalPrimitiveOpsDoubleTest {

    /**
     * Tests {@link DoubleOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(1D, PrimitiveOps.DOUBLE_ABS_OP.op(-1D),0);
        assertEquals(1D, PrimitiveOps.DOUBLE_ABS_OP.op(1D),0);
        assertEquals(Double.POSITIVE_INFINITY, PrimitiveOps.DOUBLE_ABS_OP.op(Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, PrimitiveOps.DOUBLE_ABS_OP.op(Double.NaN),0);
        PrimitiveOps.DOUBLE_ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(PrimitiveOps.DOUBLE_ABS_OP);
    }
    
   /**
     * Tests {@link IntOps#constant(int)}.
     */
    @Test
    public void constant() {
        assertEquals(1D, doubleConstant(1).op(0),0);
        assertEquals(1D, doubleConstant(1).op(1),0);
        assertIsSerializable(doubleConstant(1));
        assertEquals(2D, serializeAndUnserialize(doubleConstant(2)).op(1),0);
    }

     /**
     * Tests {@link DoubleOps#MIN_REDUCER}.
     */
    @Test
    public void min() {
        assertEquals(1D, DOUBLE_MIN_REDUCER.op(1D, 2D),0);
        assertEquals(1D, DOUBLE_MIN_REDUCER.op(2D, 1D),0);
        assertEquals(1D, DOUBLE_MIN_REDUCER.op(1D, 1D),0);
        assertEquals(1D, DOUBLE_MIN_REDUCER.op(1D, Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, DOUBLE_MIN_REDUCER.op(1D, Double.NaN),0);
        DOUBLE_MIN_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_MIN_REDUCER);
    }


    /**
     * Tests {@link DoubleOps#min}.
     */
    @Test
    public void minArg() {
        DoubleReducer r = doubleMin(Comparators.DOUBLE_NATURAL_COMPARATOR);
        assertEquals(1D, r.op(1D, 2D),0);
        assertEquals(1D, r.op(2D, 1D),0);
        assertEquals(1D, r.op(1D, 1D),0);
        assertEquals(1D, r.op(1D, Double.POSITIVE_INFINITY),0);
        // System.out.println(Double.compare(1, Double.NaN));
        // System.out.println(Double.compare(Double.NaN,1));
        assertEquals(1D, r.op(Double.NaN, 1D),0);
        r.toString(); // does not fail
        assertEquals(1D, serializeAndUnserialize(r).op(1D, 2D),0);
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void minNPE() {
        doubleMin(null);
    }
    
        /**
     * Tests {@link Reducers#MAX_REDUCER}.
     */
    @Test
    public void doubleMaxReducer() {
        assertEquals(2D, DOUBLE_MAX_REDUCER.op(2D, 1D),0);
        assertEquals(2D, DOUBLE_MAX_REDUCER.op(1D, 2D),0);
        assertEquals(2D, DOUBLE_MAX_REDUCER.op(2D, 2D),0);
        assertEquals(Double.POSITIVE_INFINITY, DOUBLE_MAX_REDUCER.op(1D, Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, DOUBLE_MAX_REDUCER.op(1D, Double.NaN),0);
        DOUBLE_MAX_REDUCER.toString(); // does not fail
        assertIsSerializable(DOUBLE_MAX_REDUCER);
        TestUtil.assertSingletonSerializable(DOUBLE_MAX_REDUCER);
    }

    /**
     * Tests
     * {@link Reducers#doubleMaxReducer(org.codehaus.cake.util.ops.Ops.DoubleComparator)}
     */
    @Test
    public void doubleMaxReducerComparator() {
        DoubleReducer r = doubleMax(Comparators.DOUBLE_NATURAL_COMPARATOR);
        assertEquals(2D, r.op(1D, 2D),0);
        assertEquals(2D, r.op(2D, 1D),0);
        assertEquals(2D, r.op(2D, 2D),0);
        assertEquals(Double.POSITIVE_INFINITY, r.op(1D, Double.POSITIVE_INFINITY), 0);
        assertEquals(Double.NaN, r.op(1D, Double.NaN), 0);
assertEquals(2D, serializeAndUnserialize(r).op(1D, 2D),0);
        r.toString(); // does not fail
        assertIsSerializable(r);
    }

    @Test(expected = NullPointerException.class)
    public void maxNPE() {
        doubleMax(null);
    }

   /**
     * Tests {@link DoubleOps#ADD_REDUCER} and {@link DoubleOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(3D, DOUBLE_ADD_REDUCER.op(1D, 2D),0);
        assertEquals(3D, DOUBLE_ADD_REDUCER.op(2D, 1D),0);
        assertEquals(Double.POSITIVE_INFINITY, DOUBLE_ADD_REDUCER.op(1, Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, DOUBLE_ADD_REDUCER.op(1, Double.NaN),0);
        DOUBLE_ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_ADD_REDUCER);
    }

    /**
     * Tests {@link DoubleOps#add(double)}.
     */
    @Test
    public void addArg() {
        assertEquals(9D, doubleAdd(5D).op(4D),0);
        assertEquals(9D, doubleAdd(4D).op(5D),0);
        assertEquals(Double.POSITIVE_INFINITY, doubleAdd(5).op(Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, doubleAdd(5).op(Double.NaN),0);
        doubleAdd(9D).toString(); // does not fail
        assertIsSerializable(doubleAdd(5D));
        assertEquals(-9D, serializeAndUnserialize(doubleAdd(12D)).op(-21D),0);
    }
    
   /**
     * Tests {@link DoubleOps#DIVIDE_REDUCER} and {@link DoubleOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(4D, DOUBLE_DIVISION_REDUCER.op(16D, 4D),0);
        assertEquals(-4D, DOUBLE_DIVISION_REDUCER.op(-8D, 2D),0);
        assertEquals(Double.POSITIVE_INFINITY, DOUBLE_DIVISION_REDUCER.op(Double.POSITIVE_INFINITY,1),0);
        assertEquals(Double.NaN, DOUBLE_DIVISION_REDUCER.op(1, Double.NaN),0);
        DOUBLE_DIVISION_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_DIVISION_REDUCER);
    }

    /**
     * Tests {@link DoubleOps#divide(double)}.
     */
    @Test
    public void divideArg() {
        assertEquals(-2D, doubleDivide(4D).op(-8D),0);
        assertEquals(5D, doubleDivide(5D).op(25D),0);
        assertEquals(Double.POSITIVE_INFINITY, doubleDivide(5).op(Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, doubleDivide(5).op(Double.NaN),0);
        doubleDivide(9D).toString(); // does not fail
        assertIsSerializable(doubleDivide(5D));
        assertEquals(-4D, serializeAndUnserialize(doubleDivide(4D)).op(-16D),0);
    }
    
       
       /**
     * Tests {@link DoubleOps#MULTIPLY_REDUCER} and {@link DoubleOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(16D, DOUBLE_MULTIPLY_REDUCER.op(4D, 4D),0);
        assertEquals(-8D, DOUBLE_MULTIPLY_REDUCER.op(-4D, 2D),0);
        assertEquals(Double.POSITIVE_INFINITY,DOUBLE_MULTIPLY_REDUCER.op(Double.POSITIVE_INFINITY,1),0);
        assertEquals(Double.NaN, DOUBLE_MULTIPLY_REDUCER.op(1, Double.NaN),0);
         DOUBLE_MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link DoubleOps#multiply(double)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(-8D, doubleMultiply(4D).op(-2D),0);
        assertEquals(25D, doubleMultiply(5D).op(5D),0);
        assertEquals(Double.POSITIVE_INFINITY, doubleMultiply(5).op(Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, doubleMultiply(5).op(Double.NaN),0);
        doubleMultiply(9D).toString(); // does not fail
        assertIsSerializable(doubleMultiply(5D));
        assertEquals(-16D, serializeAndUnserialize(doubleMultiply(4D)).op(-4D),0);
    }
    
        /**
     * Tests {@link DoubleOps#SUBTRACT_REDUCER} and {@link DoubleOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(-1D, DOUBLE_SUBTRACT_REDUCER.op(1D, 2D),0);
        assertEquals(1D, DOUBLE_SUBTRACT_REDUCER.op(2D, 1D),0);
        assertEquals(Double.NEGATIVE_INFINITY, DOUBLE_SUBTRACT_REDUCER.op(1, Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, DOUBLE_SUBTRACT_REDUCER.op(1, Double.NaN),0);
        DOUBLE_SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link DoubleOps#subtract(double)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(-1D, doubleSubtract(5D).op(4D),0);
        assertEquals(1D, doubleSubtract(4D).op(5D),0);
        assertEquals(Double.POSITIVE_INFINITY, doubleSubtract(5).op(Double.POSITIVE_INFINITY),0);
        assertEquals(Double.NaN, doubleSubtract(5).op(Double.NaN),0);
        doubleSubtract(9D).toString(); // does not fail
        assertIsSerializable(doubleSubtract(5D));
        assertEquals(-33D, serializeAndUnserialize(doubleSubtract(12D)).op(-21D),0);
    }    
}