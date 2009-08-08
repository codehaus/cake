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
package org.codehaus.cake.util;

import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.serializeAndUnserialize;
import static org.codehaus.cake.util.Comparators.DOUBLE_NATURAL_COMPARATOR;
import static org.codehaus.cake.util.Comparators.DOUBLE_NATURAL_REVERSE_COMPARATOR;
import static org.codehaus.cake.util.Comparators.FLOAT_NATURAL_COMPARATOR;
import static org.codehaus.cake.util.Comparators.FLOAT_NATURAL_REVERSE_COMPARATOR;
import static org.codehaus.cake.util.Comparators.INT_NATURAL_COMPARATOR;
import static org.codehaus.cake.util.Comparators.INT_NATURAL_REVERSE_COMPARATOR;
import static org.codehaus.cake.util.Comparators.LONG_NATURAL_COMPARATOR;
import static org.codehaus.cake.util.Comparators.LONG_NATURAL_REVERSE_COMPARATOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.ops.Ops.DoubleComparator;
import org.codehaus.cake.util.ops.Ops.FloatComparator;
import org.codehaus.cake.util.ops.Ops.IntComparator;
import org.codehaus.cake.util.ops.Ops.LongComparator;
import org.junit.Test;
/**
 * Various implementations of primitive comparators.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PrimitiveComparators.vm 245 2008-12-27 16:17:02Z kasper ComparatorsPrimitiveTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class ComparatorsPrimitiveTest {

    
       /**
     * Tests {@link DoubleOps#COMPARATOR}.
     */
    @Test
    public void doubleComparator() {
        assertEquals(0, DOUBLE_NATURAL_COMPARATOR.compare(1D, 1D));
        assertEquals(0, DOUBLE_NATURAL_COMPARATOR.compare(Double.NaN, Double.NaN));
        assertTrue(DOUBLE_NATURAL_COMPARATOR.compare(2D, 1D) > 0);
        assertTrue(DOUBLE_NATURAL_COMPARATOR.compare(1D, 2D) < 0);
        DOUBLE_NATURAL_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_NATURAL_COMPARATOR);
    }
    
   /**
     * Tests {@link DoubleOps#REVERSE_COMPARATOR}.
     */
    @Test
    public void doubleComparatorReverse() {
        assertEquals(0, DOUBLE_NATURAL_REVERSE_COMPARATOR.compare(1D, 1D));
        assertEquals(0, DOUBLE_NATURAL_REVERSE_COMPARATOR.compare(Double.NaN, Double.NaN));
        assertTrue(DOUBLE_NATURAL_REVERSE_COMPARATOR.compare(2D, 1D) < 0);
        assertTrue(DOUBLE_NATURAL_REVERSE_COMPARATOR.compare(1D, 2D) > 0);
        DOUBLE_NATURAL_REVERSE_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(DOUBLE_NATURAL_REVERSE_COMPARATOR);
    }
    
    /**
     * Tests {@link DoubleOps#reverseOrder}.
     */
    @Test
    public void doubleReverseOrder() {
        assertEquals(0, Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR).compare(1D, 1D));
        assertEquals(0, Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR).compare(Double.NaN, Double.NaN));
        assertTrue(Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR).compare(2D, 1D) < 0);
        assertTrue(Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR).compare(1D, 2D) > 0);
        Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR).toString(); // does not fail
        assertIsSerializable(Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR));
        assertTrue(serializeAndUnserialize(Comparators.reverseOrder(DOUBLE_NATURAL_COMPARATOR)).compare(2D, 1D) < 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void doubleReverseOrder_NPE() {
        Comparators.reverseOrder((DoubleComparator) null);
    }
   /**
     * Tests {@link FloatOps#COMPARATOR}.
     */
    @Test
    public void floatComparator() {
        assertEquals(0, FLOAT_NATURAL_COMPARATOR.compare(1F, 1F));
        assertEquals(0, FLOAT_NATURAL_COMPARATOR.compare(Float.NaN, Float.NaN));
        assertTrue(FLOAT_NATURAL_COMPARATOR.compare(2F, 1F) > 0);
        assertTrue(FLOAT_NATURAL_COMPARATOR.compare(1F, 2F) < 0);
        FLOAT_NATURAL_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_NATURAL_COMPARATOR);
    }
    
   /**
     * Tests {@link FloatOps#REVERSE_COMPARATOR}.
     */
    @Test
    public void floatComparatorReverse() {
        assertEquals(0, FLOAT_NATURAL_REVERSE_COMPARATOR.compare(1F, 1F));
        assertEquals(0, FLOAT_NATURAL_REVERSE_COMPARATOR.compare(Float.NaN, Float.NaN));
        assertTrue(FLOAT_NATURAL_REVERSE_COMPARATOR.compare(2F, 1F) < 0);
        assertTrue(FLOAT_NATURAL_REVERSE_COMPARATOR.compare(1F, 2F) > 0);
        FLOAT_NATURAL_REVERSE_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(FLOAT_NATURAL_REVERSE_COMPARATOR);
    }
    
    /**
     * Tests {@link FloatOps#reverseOrder}.
     */
    @Test
    public void floatReverseOrder() {
        assertEquals(0, Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR).compare(1F, 1F));
        assertEquals(0, Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR).compare(Float.NaN, Float.NaN));
        assertTrue(Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR).compare(2F, 1F) < 0);
        assertTrue(Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR).compare(1F, 2F) > 0);
        Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR).toString(); // does not fail
        assertIsSerializable(Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR));
        assertTrue(serializeAndUnserialize(Comparators.reverseOrder(FLOAT_NATURAL_COMPARATOR)).compare(2F, 1F) < 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void floatReverseOrder_NPE() {
        Comparators.reverseOrder((FloatComparator) null);
    }
   /**
     * Tests {@link IntOps#COMPARATOR}.
     */
    @Test
    public void intComparator() {
        assertEquals(0, INT_NATURAL_COMPARATOR.compare(1, 1));
        assertTrue(INT_NATURAL_COMPARATOR.compare(2, 1) > 0);
        assertTrue(INT_NATURAL_COMPARATOR.compare(1, 2) < 0);
        INT_NATURAL_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_NATURAL_COMPARATOR);
    }
    
   /**
     * Tests {@link IntOps#REVERSE_COMPARATOR}.
     */
    @Test
    public void intComparatorReverse() {
        assertEquals(0, INT_NATURAL_REVERSE_COMPARATOR.compare(1, 1));
        assertTrue(INT_NATURAL_REVERSE_COMPARATOR.compare(2, 1) < 0);
        assertTrue(INT_NATURAL_REVERSE_COMPARATOR.compare(1, 2) > 0);
        INT_NATURAL_REVERSE_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(INT_NATURAL_REVERSE_COMPARATOR);
    }
    
    /**
     * Tests {@link IntOps#reverseOrder}.
     */
    @Test
    public void intReverseOrder() {
        assertEquals(0, Comparators.reverseOrder(INT_NATURAL_COMPARATOR).compare(1, 1));
        assertTrue(Comparators.reverseOrder(INT_NATURAL_COMPARATOR).compare(2, 1) < 0);
        assertTrue(Comparators.reverseOrder(INT_NATURAL_COMPARATOR).compare(1, 2) > 0);
        Comparators.reverseOrder(INT_NATURAL_COMPARATOR).toString(); // does not fail
        assertIsSerializable(Comparators.reverseOrder(INT_NATURAL_COMPARATOR));
        assertTrue(serializeAndUnserialize(Comparators.reverseOrder(INT_NATURAL_COMPARATOR)).compare(2, 1) < 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void intReverseOrder_NPE() {
        Comparators.reverseOrder((IntComparator) null);
    }
   /**
     * Tests {@link LongOps#COMPARATOR}.
     */
    @Test
    public void longComparator() {
        assertEquals(0, LONG_NATURAL_COMPARATOR.compare(1L, 1L));
        assertTrue(LONG_NATURAL_COMPARATOR.compare(2L, 1L) > 0);
        assertTrue(LONG_NATURAL_COMPARATOR.compare(1L, 2L) < 0);
        LONG_NATURAL_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_NATURAL_COMPARATOR);
    }
    
   /**
     * Tests {@link LongOps#REVERSE_COMPARATOR}.
     */
    @Test
    public void longComparatorReverse() {
        assertEquals(0, LONG_NATURAL_REVERSE_COMPARATOR.compare(1L, 1L));
        assertTrue(LONG_NATURAL_REVERSE_COMPARATOR.compare(2L, 1L) < 0);
        assertTrue(LONG_NATURAL_REVERSE_COMPARATOR.compare(1L, 2L) > 0);
        LONG_NATURAL_REVERSE_COMPARATOR.toString(); // does not fail
        TestUtil.assertSingletonSerializable(LONG_NATURAL_REVERSE_COMPARATOR);
    }
    
    /**
     * Tests {@link LongOps#reverseOrder}.
     */
    @Test
    public void longReverseOrder() {
        assertEquals(0, Comparators.reverseOrder(LONG_NATURAL_COMPARATOR).compare(1L, 1L));
        assertTrue(Comparators.reverseOrder(LONG_NATURAL_COMPARATOR).compare(2L, 1L) < 0);
        assertTrue(Comparators.reverseOrder(LONG_NATURAL_COMPARATOR).compare(1L, 2L) > 0);
        Comparators.reverseOrder(LONG_NATURAL_COMPARATOR).toString(); // does not fail
        assertIsSerializable(Comparators.reverseOrder(LONG_NATURAL_COMPARATOR));
        assertTrue(serializeAndUnserialize(Comparators.reverseOrder(LONG_NATURAL_COMPARATOR)).compare(2L, 1L) < 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void longReverseOrder_NPE() {
        Comparators.reverseOrder((LongComparator) null);
    }
    
}