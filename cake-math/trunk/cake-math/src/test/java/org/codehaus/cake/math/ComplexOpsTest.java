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
package org.codehaus.cake.math;

import static org.codehaus.cake.ops.Ops.*;
import static org.junit.Assert.*;

import static org.codehaus.cake.test.util.TestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.codehaus.cake.ops.Ops.DoubleReducer;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Test;
import org.codehaus.cake.ops.Ops.*;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Test;
import java.math.*;
/**
 * Various tests for {@link ComplexOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ComplexOpsTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class ComplexOpsTest {

    /**
     * Tests {@link ComplexOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(Math.sqrt(2), ComplexOps.ABS_OP.op(new Complex(-1, -1)), 0);
        assertEquals(Math.sqrt(2), ComplexOps.ABS_OP.op(new Complex(1, 1)), 0);
        assertSame(ComplexOps.ABS_OP, ComplexOps.abs());
        ComplexOps.ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(ComplexOps.ABS_OP);
    }   /**
     * Tests {@link ComplexOps#ADD_REDUCER} and {@link ComplexOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(new Complex(3,3), ComplexOps.ADD_REDUCER.op(new Complex(1,1), new Complex(2,2)));
        assertEquals(new Complex(3,3), ComplexOps.ADD_REDUCER.op(new Complex(2,2), new Complex(1,1)));
        assertSame(ComplexOps.ADD_REDUCER, ComplexOps.add());
        ComplexOps.ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(ComplexOps.ADD_REDUCER);
    }

    /**
     * Tests {@link ComplexOps#add(Complex)}.
     */
    @Test
    public void addArg() {
        assertEquals(new Complex(9,9), ComplexOps.add(new Complex(5,5)).op(new Complex(4,4)));
        assertEquals(new Complex(9,9), ComplexOps.add(new Complex(4,4)).op(new Complex(5,5)));
        ComplexOps.add(new Complex(9,9)).toString(); // does not fail
        assertIsSerializable(ComplexOps.add(new Complex(5,5)));
        assertEquals(new Complex(-9,-9), serializeAndUnserialize(ComplexOps.add(new Complex(12,12))).op(new Complex(-21,-21)));
    }
    
   /**
     * Tests {@link ComplexOps#DIVIDE_REDUCER} and {@link ComplexOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(new Complex(4,4), ComplexOps.DIVIDE_REDUCER.op(new Complex(16,16), new Complex(4,4)));
        assertEquals(new Complex(-4,-4), ComplexOps.DIVIDE_REDUCER.op(new Complex(-8,-8), new Complex(2,2)));
        assertSame(ComplexOps.DIVIDE_REDUCER, ComplexOps.divide());
        ComplexOps.DIVIDE_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(ComplexOps.DIVIDE_REDUCER);
    }

    /**
     * Tests {@link ComplexOps#divide(Complex)}.
     */
    @Test
    public void divideArg() {
        assertEquals(new Complex(-2,-2), ComplexOps.divide(new Complex(4,4)).op(new Complex(-8,-8)));
        assertEquals(new Complex(5,5), ComplexOps.divide(new Complex(5,5)).op(new Complex(25,25)));
        ComplexOps.divide(new Complex(9,9)).toString(); // does not fail
        assertIsSerializable(ComplexOps.divide(new Complex(5,5)));
        assertEquals(new Complex(-4,-4), serializeAndUnserialize(ComplexOps.divide(new Complex(4,4))).op(new Complex(-16,-16)));
    }
    
       
       /**
     * Tests {@link ComplexOps#MULTIPLY_REDUCER} and {@link ComplexOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(new Complex(16,16), ComplexOps.MULTIPLY_REDUCER.op(new Complex(4,4), new Complex(4,4)));
        assertEquals(new Complex(-8,-8), ComplexOps.MULTIPLY_REDUCER.op(new Complex(-4,-4), new Complex(2,2)));
        assertSame(ComplexOps.MULTIPLY_REDUCER, ComplexOps.multiply());
        ComplexOps.MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(ComplexOps.MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link ComplexOps#multiply(Complex)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(new Complex(-8,-8), ComplexOps.multiply(new Complex(4,4)).op(new Complex(-2,-2)));
        assertEquals(new Complex(25,25), ComplexOps.multiply(new Complex(5,5)).op(new Complex(5,5)));
        ComplexOps.multiply(new Complex(9,9)).toString(); // does not fail
        assertIsSerializable(ComplexOps.multiply(new Complex(5,5)));
        assertEquals(new Complex(-16,-16), serializeAndUnserialize(ComplexOps.multiply(new Complex(4,4))).op(new Complex(-4,-4)));
    }
    
        /**
     * Tests {@link ComplexOps#SUBTRACT_REDUCER} and {@link ComplexOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(new Complex(-1,-1), ComplexOps.SUBTRACT_REDUCER.op(new Complex(1,1), new Complex(2,2)));
        assertEquals(new Complex(1,1), ComplexOps.SUBTRACT_REDUCER.op(new Complex(2,2), new Complex(1,1)));
        assertSame(ComplexOps.SUBTRACT_REDUCER, ComplexOps.subtract());
        ComplexOps.SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(ComplexOps.SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link ComplexOps#subtract(Complex)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(new Complex(-1,-1), ComplexOps.subtract(new Complex(5,5)).op(new Complex(4,4)));
        assertEquals(new Complex(1,1), ComplexOps.subtract(new Complex(4,4)).op(new Complex(5,5)));
        ComplexOps.subtract(new Complex(9,9)).toString(); // does not fail
        assertIsSerializable(ComplexOps.subtract(new Complex(5,5)));
        assertEquals(new Complex(-33,-33), serializeAndUnserialize(ComplexOps.subtract(new Complex(12,12))).op(new Complex(-21,-21)));
    }
}