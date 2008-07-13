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
 * Various tests for {@link BigDecimalOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: BigDecimalOpsTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class BigDecimalOpsTest {

    /**
     * Tests {@link BigDecimalOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(new BigDecimal(1), BigDecimalOps.ABS_OP.op(new BigDecimal(-1)));
        assertEquals(new BigDecimal(1), BigDecimalOps.ABS_OP.op(new BigDecimal(1)));
        assertSame(BigDecimalOps.ABS_OP, BigDecimalOps.abs());
        BigDecimalOps.ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigDecimalOps.ABS_OP);
    }   /**
     * Tests {@link BigDecimalOps#ADD_REDUCER} and {@link BigDecimalOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(new BigDecimal(3), BigDecimalOps.ADD_REDUCER.op(new BigDecimal(1), new BigDecimal(2)));
        assertEquals(new BigDecimal(3), BigDecimalOps.ADD_REDUCER.op(new BigDecimal(2), new BigDecimal(1)));
        assertSame(BigDecimalOps.ADD_REDUCER, BigDecimalOps.add());
        BigDecimalOps.ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigDecimalOps.ADD_REDUCER);
    }

    /**
     * Tests {@link BigDecimalOps#add(BigDecimal)}.
     */
    @Test
    public void addArg() {
        assertEquals(new BigDecimal(9), BigDecimalOps.add(new BigDecimal(5)).op(new BigDecimal(4)));
        assertEquals(new BigDecimal(9), BigDecimalOps.add(new BigDecimal(4)).op(new BigDecimal(5)));
        BigDecimalOps.add(new BigDecimal(9)).toString(); // does not fail
        assertIsSerializable(BigDecimalOps.add(new BigDecimal(5)));
        assertEquals(new BigDecimal(-9), serializeAndUnserialize(BigDecimalOps.add(new BigDecimal(12))).op(new BigDecimal(-21)));
    }
    
   /**
     * Tests {@link BigDecimalOps#DIVIDE_REDUCER} and {@link BigDecimalOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(new BigDecimal(4), BigDecimalOps.DIVIDE_REDUCER.op(new BigDecimal(16), new BigDecimal(4)));
        assertEquals(new BigDecimal(-4), BigDecimalOps.DIVIDE_REDUCER.op(new BigDecimal(-8), new BigDecimal(2)));
        assertSame(BigDecimalOps.DIVIDE_REDUCER, BigDecimalOps.divide());
        BigDecimalOps.DIVIDE_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigDecimalOps.DIVIDE_REDUCER);
    }

    /**
     * Tests {@link BigDecimalOps#divide(BigDecimal)}.
     */
    @Test
    public void divideArg() {
        assertEquals(new BigDecimal(-2), BigDecimalOps.divide(new BigDecimal(4)).op(new BigDecimal(-8)));
        assertEquals(new BigDecimal(5), BigDecimalOps.divide(new BigDecimal(5)).op(new BigDecimal(25)));
        BigDecimalOps.divide(new BigDecimal(9)).toString(); // does not fail
        assertIsSerializable(BigDecimalOps.divide(new BigDecimal(5)));
        assertEquals(new BigDecimal(-4), serializeAndUnserialize(BigDecimalOps.divide(new BigDecimal(4))).op(new BigDecimal(-16)));
    }
    
       
       /**
     * Tests {@link BigDecimalOps#MULTIPLY_REDUCER} and {@link BigDecimalOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(new BigDecimal(16), BigDecimalOps.MULTIPLY_REDUCER.op(new BigDecimal(4), new BigDecimal(4)));
        assertEquals(new BigDecimal(-8), BigDecimalOps.MULTIPLY_REDUCER.op(new BigDecimal(-4), new BigDecimal(2)));
        assertSame(BigDecimalOps.MULTIPLY_REDUCER, BigDecimalOps.multiply());
        BigDecimalOps.MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigDecimalOps.MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link BigDecimalOps#multiply(BigDecimal)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(new BigDecimal(-8), BigDecimalOps.multiply(new BigDecimal(4)).op(new BigDecimal(-2)));
        assertEquals(new BigDecimal(25), BigDecimalOps.multiply(new BigDecimal(5)).op(new BigDecimal(5)));
        BigDecimalOps.multiply(new BigDecimal(9)).toString(); // does not fail
        assertIsSerializable(BigDecimalOps.multiply(new BigDecimal(5)));
        assertEquals(new BigDecimal(-16), serializeAndUnserialize(BigDecimalOps.multiply(new BigDecimal(4))).op(new BigDecimal(-4)));
    }
    
        /**
     * Tests {@link BigDecimalOps#SUBTRACT_REDUCER} and {@link BigDecimalOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(new BigDecimal(-1), BigDecimalOps.SUBTRACT_REDUCER.op(new BigDecimal(1), new BigDecimal(2)));
        assertEquals(new BigDecimal(1), BigDecimalOps.SUBTRACT_REDUCER.op(new BigDecimal(2), new BigDecimal(1)));
        assertSame(BigDecimalOps.SUBTRACT_REDUCER, BigDecimalOps.subtract());
        BigDecimalOps.SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigDecimalOps.SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link BigDecimalOps#subtract(BigDecimal)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(new BigDecimal(-1), BigDecimalOps.subtract(new BigDecimal(5)).op(new BigDecimal(4)));
        assertEquals(new BigDecimal(1), BigDecimalOps.subtract(new BigDecimal(4)).op(new BigDecimal(5)));
        BigDecimalOps.subtract(new BigDecimal(9)).toString(); // does not fail
        assertIsSerializable(BigDecimalOps.subtract(new BigDecimal(5)));
        assertEquals(new BigDecimal(-33), serializeAndUnserialize(BigDecimalOps.subtract(new BigDecimal(12))).op(new BigDecimal(-21)));
    }
}