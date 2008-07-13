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
 * Various tests for {@link BigIntegerOps}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: BigIntegerOpsTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class BigIntegerOpsTest {

    /**
     * Tests {@link BigIntegerOps#ABS_OP}.
     */
    @Test
    public void abs() {
        assertEquals(BigInteger.valueOf(1L), BigIntegerOps.ABS_OP.op(BigInteger.valueOf(-1L)));
        assertEquals(BigInteger.valueOf(1L), BigIntegerOps.ABS_OP.op(BigInteger.valueOf(1L)));
        assertSame(BigIntegerOps.ABS_OP, BigIntegerOps.abs());
        BigIntegerOps.ABS_OP.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigIntegerOps.ABS_OP);
    }   /**
     * Tests {@link BigIntegerOps#ADD_REDUCER} and {@link BigIntegerOps#add()}.
     */
    @Test
    public void add() {
        assertEquals(BigInteger.valueOf(3L), BigIntegerOps.ADD_REDUCER.op(BigInteger.valueOf(1L), BigInteger.valueOf(2L)));
        assertEquals(BigInteger.valueOf(3L), BigIntegerOps.ADD_REDUCER.op(BigInteger.valueOf(2L), BigInteger.valueOf(1L)));
        assertSame(BigIntegerOps.ADD_REDUCER, BigIntegerOps.add());
        BigIntegerOps.ADD_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigIntegerOps.ADD_REDUCER);
    }

    /**
     * Tests {@link BigIntegerOps#add(BigInteger)}.
     */
    @Test
    public void addArg() {
        assertEquals(BigInteger.valueOf(9L), BigIntegerOps.add(BigInteger.valueOf(5L)).op(BigInteger.valueOf(4L)));
        assertEquals(BigInteger.valueOf(9L), BigIntegerOps.add(BigInteger.valueOf(4L)).op(BigInteger.valueOf(5L)));
        BigIntegerOps.add(BigInteger.valueOf(9L)).toString(); // does not fail
        assertIsSerializable(BigIntegerOps.add(BigInteger.valueOf(5L)));
        assertEquals(BigInteger.valueOf(-9L), serializeAndUnserialize(BigIntegerOps.add(BigInteger.valueOf(12L))).op(BigInteger.valueOf(-21L)));
    }
    
   /**
     * Tests {@link BigIntegerOps#DIVIDE_REDUCER} and {@link BigIntegerOps#divide()}.
     */
    @Test
    public void divide() {
        assertEquals(BigInteger.valueOf(4L), BigIntegerOps.DIVIDE_REDUCER.op(BigInteger.valueOf(16L), BigInteger.valueOf(4L)));
        assertEquals(BigInteger.valueOf(-4L), BigIntegerOps.DIVIDE_REDUCER.op(BigInteger.valueOf(-8L), BigInteger.valueOf(2L)));
        assertSame(BigIntegerOps.DIVIDE_REDUCER, BigIntegerOps.divide());
        BigIntegerOps.DIVIDE_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigIntegerOps.DIVIDE_REDUCER);
    }

    /**
     * Tests {@link BigIntegerOps#divide(BigInteger)}.
     */
    @Test
    public void divideArg() {
        assertEquals(BigInteger.valueOf(-2L), BigIntegerOps.divide(BigInteger.valueOf(4L)).op(BigInteger.valueOf(-8L)));
        assertEquals(BigInteger.valueOf(5L), BigIntegerOps.divide(BigInteger.valueOf(5L)).op(BigInteger.valueOf(25L)));
        BigIntegerOps.divide(BigInteger.valueOf(9L)).toString(); // does not fail
        assertIsSerializable(BigIntegerOps.divide(BigInteger.valueOf(5L)));
        assertEquals(BigInteger.valueOf(-4L), serializeAndUnserialize(BigIntegerOps.divide(BigInteger.valueOf(4L))).op(BigInteger.valueOf(-16L)));
    }
    
       
       /**
     * Tests {@link BigIntegerOps#MULTIPLY_REDUCER} and {@link BigIntegerOps#multiply()}.
     */
    @Test
    public void multiply() {
        assertEquals(BigInteger.valueOf(16L), BigIntegerOps.MULTIPLY_REDUCER.op(BigInteger.valueOf(4L), BigInteger.valueOf(4L)));
        assertEquals(BigInteger.valueOf(-8L), BigIntegerOps.MULTIPLY_REDUCER.op(BigInteger.valueOf(-4L), BigInteger.valueOf(2L)));
        assertSame(BigIntegerOps.MULTIPLY_REDUCER, BigIntegerOps.multiply());
        BigIntegerOps.MULTIPLY_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigIntegerOps.MULTIPLY_REDUCER);
    }

    /**
     * Tests {@link BigIntegerOps#multiply(BigInteger)}.
     */
    @Test
    public void multiplyArg() {
        assertEquals(BigInteger.valueOf(-8L), BigIntegerOps.multiply(BigInteger.valueOf(4L)).op(BigInteger.valueOf(-2L)));
        assertEquals(BigInteger.valueOf(25L), BigIntegerOps.multiply(BigInteger.valueOf(5L)).op(BigInteger.valueOf(5L)));
        BigIntegerOps.multiply(BigInteger.valueOf(9L)).toString(); // does not fail
        assertIsSerializable(BigIntegerOps.multiply(BigInteger.valueOf(5L)));
        assertEquals(BigInteger.valueOf(-16L), serializeAndUnserialize(BigIntegerOps.multiply(BigInteger.valueOf(4L))).op(BigInteger.valueOf(-4L)));
    }
    
        /**
     * Tests {@link BigIntegerOps#SUBTRACT_REDUCER} and {@link BigIntegerOps#subtract()}.
     */
    @Test
    public void subtract() {
        assertEquals(BigInteger.valueOf(-1L), BigIntegerOps.SUBTRACT_REDUCER.op(BigInteger.valueOf(1L), BigInteger.valueOf(2L)));
        assertEquals(BigInteger.valueOf(1L), BigIntegerOps.SUBTRACT_REDUCER.op(BigInteger.valueOf(2L), BigInteger.valueOf(1L)));
        assertSame(BigIntegerOps.SUBTRACT_REDUCER, BigIntegerOps.subtract());
        BigIntegerOps.SUBTRACT_REDUCER.toString(); // does not fail
        TestUtil.assertSingletonSerializable(BigIntegerOps.SUBTRACT_REDUCER);
    }

    /**
     * Tests {@link BigIntegerOps#subtract(BigInteger)}.
     */
    @Test
    public void subtractArg() {
        assertEquals(BigInteger.valueOf(-1L), BigIntegerOps.subtract(BigInteger.valueOf(5L)).op(BigInteger.valueOf(4L)));
        assertEquals(BigInteger.valueOf(1L), BigIntegerOps.subtract(BigInteger.valueOf(4L)).op(BigInteger.valueOf(5L)));
        BigIntegerOps.subtract(BigInteger.valueOf(9L)).toString(); // does not fail
        assertIsSerializable(BigIntegerOps.subtract(BigInteger.valueOf(5L)));
        assertEquals(BigInteger.valueOf(-33L), serializeAndUnserialize(BigIntegerOps.subtract(BigInteger.valueOf(12L))).op(BigInteger.valueOf(-21L)));
    }
}