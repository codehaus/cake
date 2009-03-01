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
package org.codehaus.cake.util.collection;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1;
import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;
import static org.codehaus.cake.test.util.CollectionTestUtil.NULL_A;
import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.dummy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.collection.Iterables;
import org.codehaus.cake.util.collection.Maps;
import org.codehaus.cake.util.ops.ObjectOps;
import org.codehaus.cake.util.ops.Predicates;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.Procedure;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class IterablesTest {
    Mockery context = new JUnit4Mockery();

    @Test
    public void containedWithin() {
        assertTrue(Iterables.containedWithin(Arrays.asList(1)).op(1));
        assertFalse(Iterables.containedWithin(Arrays.asList(1)).op(-1));
        assertNotNull(Iterables.containedWithin(Arrays.asList(1)).toString());
        assertIsSerializable(Iterables.containedWithin(Arrays.asList(1)));
    }

    @Test(expected = NullPointerException.class)
    public void containedWithin_NPE() {
        Iterables.containedWithin(null);
    }

    @Test
    public void addToCollection() {
        final Collection col = context.mock(Collection.class);
        context.checking(new Expectations() {
            {
                one(col).add(-1);
            }
        });
        Iterables.addToCollectionProcedure(col).op(-1);
        assertIsSerializable(Iterables.addToCollectionProcedure(new ArrayList(1)));
    }

    @Test(expected = NullPointerException.class)
    public void addToCollection_NPE() {
        Iterables.addToCollectionProcedure(null);
    }

    @Test
    public void filter() {
        Predicate<Number> p = Predicates.<Number> equalsToAny(2, 3);
        Collection<Integer> col = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list = Iterables.filteredList(col, p);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).intValue());// some compilers need intValue()
        assertEquals(3, list.get(1).intValue());// some compilers need intValue()
    }

    @Test(expected = NullPointerException.class)
    public void filter_NPE1() {
        Iterables.filteredList(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void filter_NPE2() {
        Iterables.filteredList(new ArrayList(), null);
    }

    @Test
    public void filterMap() {
        Predicate<Map.Entry<Integer, String>> p = new Predicate<Map.Entry<Integer, String>>() {
            public boolean op(Map.Entry<Integer, String> element) {
                return element.getKey().equals(2) || element.getValue().equals("3");
            }
        };
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        Map<Integer, String> filteredMap = Maps.filteredMap(map, p);
        assertEquals(2, filteredMap.size());
        assertEquals("2", filteredMap.get(2));
        assertEquals("3", filteredMap.get(3));
    }

    @Test(expected = NullPointerException.class)
    public void filterMap_NPE1() {
        Maps.filteredMap(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void filterMap_NPE2() {
        Maps.filteredMap(new HashMap(), null);
    }

    @Test
    public void filterMapKey() {
        Predicate<Number> p = new Predicate<Number>() {
            public boolean op(Number element) {
                return element.equals(2) || element.equals(3);
            }
        };
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        Map<Integer, String> filteredMap = Maps.filteredMapOnKeys(map, p);
        assertEquals(2, filteredMap.size());
        assertEquals("2", filteredMap.get(2));
        assertEquals("3", filteredMap.get(3));
    }

    @Test(expected = NullPointerException.class)
    public void filterMapKey_NPE1() {
        Maps.filteredMapOnKeys(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void filterMapKey_NPE2() {
        Maps.filteredMapOnKeys(new HashMap(), null);
    }

    @Test
    public void filterMapValue() {
        Predicate<String> p = new Predicate<String>() {
            public boolean op(String element) {
                return element.equals("2") || element.equals("3");
            }
        };
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        Map<Integer, String> filteredMap = Maps.filteredMapOnValues(map, p);
        assertEquals(2, filteredMap.size());
        assertEquals("2", filteredMap.get(2));
        assertEquals("3", filteredMap.get(3));
    }

    @Test(expected = NullPointerException.class)
    public void filterMapValue_NPE1() {
        Maps.filteredMapOnValues(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void filterMapValue_NPE2() {
        Maps.filteredMapOnValues(new HashMap(), null);
    }

    @Test
    public void isAllTrue() {
        Predicate<Number> p = Predicates.<Number> equalsToAny(2, 3);
        Predicate<Number> p2 = Predicates.<Number> equalsToAny(1, 2, 3, 4, 5);
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        assertFalse(Iterables.isAllTrue(list, p));
        assertTrue(Iterables.isAllTrue(list, p2));
    }

    @Test(expected = NullPointerException.class)
    public void isAllTrue_NPE1() {
        Iterables.isAllTrue(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void isAllTrue_NPE2() {
        Iterables.isAllTrue(new ArrayList(), null);
    }

    @Test
    public void isAnyTrue() {
        Predicate<Number> p = Predicates.<Number> equalsToAny(2, 3);
        Predicate<Number> p2 = Predicates.<Number> equalsToAny(5, 6, 7, 8);
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        assertTrue(Iterables.isAnyTrue(list, p));
        assertFalse(Iterables.isAnyTrue(list, p2));
    }

    @Test(expected = NullPointerException.class)
    public void isAnyTrue_NPE1() {
        Iterables.isAnyTrue(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void isAnyTrue_NPE2() {
        Iterables.isAnyTrue(new ArrayList(), null);
    }

    /**
     * Tests {@link ObjectOps#CONSTANT_OP}.
     */
    @Test
    public void keyFromMap() {
        assertEquals(M1.getKey(), Maps.MAP_ENTRY_TO_KEY_OP.op(M1));
        assertEquals(null, Maps.MAP_ENTRY_TO_KEY_OP.op(NULL_A));
        assertSame(Maps.MAP_ENTRY_TO_KEY_OP, Maps.mapEntryToKey());
        Maps.MAP_ENTRY_TO_KEY_OP.toString(); // does not fail
        assertIsSerializable(Maps.mapEntryToKey());
        assertSame(Maps.MAP_ENTRY_TO_KEY_OP, TestUtil.serializeAndUnserialize(Maps.MAP_ENTRY_TO_KEY_OP));
    }

    @Test
    public void offerToQueue() {
        final Queue q = context.mock(Queue.class);
        context.checking(new Expectations() {
            {
                one(q).offer(-1);
            }
        });
        Procedure processor = Iterables.offerToQueue(q);
        processor.op(-1);
        assertIsSerializable(Iterables.offerToQueue(new ArrayBlockingQueue(1)));
    }

    @Test(expected = NullPointerException.class)
    public void offerToQueue_NPE() {
        Iterables.offerToQueue(null);
    }

    @Test
    public void retain() {
        Predicate<Number> p = Predicates.<Number> equalsToAny(2, 3);
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        Iterables.retain(list, p);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).intValue());
        assertEquals(3, list.get(1).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void retain_NPE1() {
        Iterables.retain(null, dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void retain_NPE2() {
        Iterables.retain(new ArrayList(), null);
    }

    /**
     * Tests {@link ObjectOps#CONSTANT_OP}.
     */
    @Test
    public void valueFromMap() {
        assertEquals(M1.getValue(), Maps.MAP_ENTRY_TO_VALUE_OP.op(M1));
        assertEquals(null, Maps.MAP_ENTRY_TO_VALUE_OP.op(M1_NULL));
        assertSame(Maps.MAP_ENTRY_TO_VALUE_OP, Maps.mapEntryToValue());
        Maps.MAP_ENTRY_TO_VALUE_OP.toString(); // does not fail
        assertIsSerializable(Maps.mapEntryToValue());
        assertSame(Maps.MAP_ENTRY_TO_VALUE_OP, TestUtil.serializeAndUnserialize(Maps.MAP_ENTRY_TO_VALUE_OP));
    }
}
