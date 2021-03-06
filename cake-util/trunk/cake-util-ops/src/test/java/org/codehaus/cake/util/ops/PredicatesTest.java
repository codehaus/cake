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
package org.codehaus.cake.util.ops;

import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.dummy;
import static org.codehaus.cake.util.ops.Predicates.FALSE;
import static org.codehaus.cake.util.ops.Predicates.TRUE;
import static org.codehaus.cake.util.ops.Predicates.greaterThen;
import static org.codehaus.cake.util.ops.Predicates.greaterThenOrEqual;
import static org.codehaus.cake.util.ops.Predicates.lessThen;
import static org.codehaus.cake.util.ops.Predicates.lessThenOrEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.AllPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.AndPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.AnyPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.GreaterThenOrEqualPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.GreaterThenPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.IsEqualsPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.IsSamePredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.IsTypePredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.LessThenOrEqualPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.LessThenPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.MapAndEvaluatePredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.NotPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.OrPredicate;
import org.codehaus.cake.internal.util.ops.InternalObjectPredicates.XorPredicate;
import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.test.util.ComparatorTestUtil.Dummy;
import org.codehaus.cake.test.util.ComparatorTestUtil.DummyComparator;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.junit.Test;

/**
 * Tests {@link Predicates}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id$
 */
public class PredicatesTest {

    private static final Comparator<Dummy> COMP = new DummyComparator();

    private static Predicate[] PREDICATES_WITH_NULL_ARRAY = { TRUE, null, TRUE };

    private static Iterable PREDICATES_WITH_NULL_ITERABLE = Arrays.asList(PREDICATES_WITH_NULL_ARRAY);

    private static Iterable STRING_PREDICATE_ITERABLE = Arrays.asList(StringOps.startsWith("foo"), StringOps
            .contains("boo"));

    private static Predicate[] TRUE_FALSE_TRUE_ARRAY = { TRUE, FALSE, TRUE };

    private static Iterable TRUE_FALSE_TRUE_ITERABLE = Arrays.asList(TRUE_FALSE_TRUE_ARRAY);

    /**
     * Tests {@link Predicates#allTrue(Predicate...)}.
     */
    @Test
    public void allArray() {
        AllPredicate<?> p = (AllPredicate) Predicates.allTrue(TRUE_FALSE_TRUE_ARRAY);

        // evaluate
        assertTrue((Predicates.allTrue(TRUE)).op(null));
        assertFalse(Predicates.allTrue(FALSE).op(null));
        assertTrue(Predicates.allTrue(TRUE, TRUE).op(null));
        assertFalse(Predicates.allTrue(TRUE, FALSE).op(null));

        // getPredicates
        assertEquals(3, p.getPredicates().size());
        assertEquals(TRUE, p.getPredicates().get(0));
        assertEquals(FALSE, p.getPredicates().get(1));
        assertEquals(TRUE, p.getPredicates().get(2));

        // iterable
        Iterator<?> i = p.iterator();
        assertSame(TRUE, i.next());
        assertSame(FALSE, i.next());
        assertSame(TRUE, i.next());
        assertFalse(i.hasNext());

        // Serializable
        TestUtil.assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        Predicates.allTrue(TRUE_FALSE_TRUE_ARRAY).toString();
        Predicates.allTrue(new Predicate[] {}).toString();
        Predicates.allTrue(new Predicate[] { TRUE }).toString();

        // shortcircuted evaluation
        Predicates.allTrue(TRUE, FALSE, TestUtil.dummy(Predicate.class)).op(null);
    }

    /**
     * Tests that {@link Predicates#allTrue(Predicate...)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void allArrayNPE() {
        Predicates.allTrue((Predicate[]) null);
    }

    /**
     * Tests that {@link Predicates#allTrue(Predicate...)} throws a {@link NullPointerException} when invoked with an
     * array containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void allArrayNPE1() {
        Predicates.allTrue(PREDICATES_WITH_NULL_ARRAY);
    }

    /**
     * Tests {@link Predicates#allTrue(Iterable)}.
     */
    @Test
    public void allIterable() {
        AllPredicate<String> p = (AllPredicate) Predicates.allTrue(STRING_PREDICATE_ITERABLE);

        // evaluate
        assertFalse(p.op("fobo"));
        assertFalse(p.op("foobo"));
        assertFalse(p.op("foboo"));
        assertTrue(p.op("fooboo"));

        // getPredicates
        assertEquals(2, p.getPredicates().size());
        assertEquals(StringOps.startsWith("foo"), p.getPredicates().get(0));
        assertEquals(StringOps.contains("boo"), p.getPredicates().get(1));

        // iterable
        Iterator<?> i = p.iterator();
        assertEquals(StringOps.startsWith("foo"), i.next());
        assertEquals(StringOps.contains("boo"), i.next());
        assertFalse(i.hasNext());

        // Serializable
        TestUtil.assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        Predicates.allTrue(TRUE_FALSE_TRUE_ITERABLE).toString();
        Predicates.allTrue((Iterable) Arrays.asList()).toString();
        Predicates.allTrue((Iterable) Arrays.asList(TRUE)).toString();

        // shortcircuted evaluation
        Predicates.allTrue((Iterable) Arrays.asList(TRUE, FALSE, TestUtil.dummy(Predicate.class))).op(null);
    }

    /**
     * Tests that {@link Predicates#allTrue(Iterable)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void allIterableNPE() {
        Predicates.allTrue((Iterable) null);
    }

    /**
     * Tests that {@link Predicates#allTrue(Iterable)} throws a {@link NullPointerException} when invoked with an
     * iterable containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void allIterableNPE1() {
        Predicates.allTrue(PREDICATES_WITH_NULL_ITERABLE);
    }

    /**
     * Tests {@link Predicates#and(Predicate, Predicate)}.
     */
    @Test
    public void and() {
        assertTrue(Predicates.and(TRUE, TRUE).op(null));
        assertFalse(Predicates.and(TRUE, FALSE).op(null));
        assertFalse(Predicates.and(FALSE, TRUE).op(null));
        assertFalse(Predicates.and(FALSE, FALSE).op(null));

        AndPredicate p = new AndPredicate(FALSE, TRUE);
        assertSame(p.getLeftPredicate(), FALSE);
        assertSame(p.getRightPredicate(), TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        Predicates.and(FALSE, TestUtil.dummy(Predicate.class)).op(null);
    }

    /**
     * Tests that {@link Predicates#and(Predicate, Predicate)} throws a {@link NullPointerException} when invoked with a
     * left side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE() {
        Predicates.and(null, TRUE);
    }

    /**
     * Tests that {@link Predicates#and(Predicate, Predicate)} throws a {@link NullPointerException} when invoked with a
     * right side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void andNPE1() {
        Predicates.and(TRUE, null);
    }

    /**
     * Tests {@link Predicates#anyTrue(Predicate...)}.
     */
    @Test
    public void anyArray() {
        AnyPredicate<?> filter = (AnyPredicate) Predicates.anyTrue(TRUE_FALSE_TRUE_ARRAY);

        // evaluate
        assertTrue((Predicates.anyTrue(TRUE)).op(null));
        assertFalse(Predicates.anyTrue(FALSE).op(null));
        assertTrue(Predicates.anyTrue(TRUE, TRUE).op(null));
        assertTrue(Predicates.anyTrue(TRUE, FALSE).op(null));
        assertFalse(Predicates.anyTrue(FALSE, FALSE).op(null));

        // getPredicates
        assertEquals(3, filter.getPredicates().size());
        assertEquals(TRUE, filter.getPredicates().get(0));
        assertEquals(FALSE, filter.getPredicates().get(1));
        assertEquals(TRUE, filter.getPredicates().get(2));

        // iterable
        Iterator<?> i = filter.iterator();
        assertSame(TRUE, i.next());
        assertSame(FALSE, i.next());
        assertSame(TRUE, i.next());
        assertFalse(i.hasNext());

        // Serializable
        TestUtil.assertIsSerializable(filter);

        // toString, just check that they don't throw exceptions
        Predicates.anyTrue(TRUE_FALSE_TRUE_ARRAY).toString();
        Predicates.anyTrue(new Predicate[] {}).toString();
        Predicates.anyTrue(new Predicate[] { TRUE }).toString();

        // shortcircuted evaluation
        Predicates.anyTrue(FALSE, TRUE, TestUtil.dummy(Predicate.class)).op(null);
    }

    /**
     * Tests that {@link Predicates#anyTrue(Predicate...)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyArrayNPE() {
        Predicates.anyTrue((Predicate[]) null);
    }

    /**
     * Tests that {@link Predicates#anyTrue(Predicate...)} throws a {@link NullPointerException} when invoked with an
     * array containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyArrayNPE1() {
        Predicates.anyTrue(PREDICATES_WITH_NULL_ARRAY);
    }

    /**
     * Tests {@link Predicates#equalsToAny(Object...)}.
     */
    @Test
    public void anyEqualsArray() {
        Predicate<?> p = Predicates.equalsToAny(TRUE_FALSE_TRUE_ARRAY);

        // evaluate
        assertTrue((Predicates.equalsToAny(TRUE)).op(TRUE));
        assertFalse(Predicates.equalsToAny(TRUE).op(FALSE));
        assertTrue(Predicates.equalsToAny(TRUE, FALSE).op(TRUE));
        assertTrue(Predicates.equalsToAny(TRUE, FALSE).op(FALSE));
        assertFalse(Predicates.equalsToAny(FALSE, FALSE).op(TRUE));
        // Serializable
        assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        Predicates.equalsToAny(TRUE_FALSE_TRUE_ARRAY).toString();
        Predicates.equalsToAny(new Predicate[] {}).toString();
        Predicates.equalsToAny(new Predicate[] { TRUE }).toString();

        // shortcircuted evaluation
        Predicates.equalsToAny(FALSE, TRUE, TestUtil.dummy(Predicate.class)).op(TRUE);
    }

    /**
     * Tests that {@link Predicates#equalsToAny(Object...)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyEqualsArrayNPE() {
        Predicates.equalsToAny((Predicate[]) null);
    }

    /**
     * Tests that {@link Predicates#equalsToAny(Object...)} throws a {@link NullPointerException} when invoked with an
     * array containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyEqualsArrayNPE1() {
        Predicates.equalsToAny(PREDICATES_WITH_NULL_ARRAY);
    }

    /**
     * Tests {@link Predicates#equalsToAny(Iterable)}.
     */
    @Test
    public void anyEqualsIterable() {
        Predicate<?> p = Predicates.equalsToAny(TRUE_FALSE_TRUE_ITERABLE);
        assertTrue(Predicates.equalsToAny(Arrays.asList(TRUE, FALSE)).op(FALSE));
        assertFalse(Predicates.equalsToAny(Arrays.asList(FALSE, FALSE)).op(TRUE));
        // Serializable
        assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        Predicates.equalsToAny(TRUE_FALSE_TRUE_ITERABLE).toString();
        Predicates.equalsToAny(Arrays.asList()).toString();
        Predicates.equalsToAny(Arrays.asList(TRUE)).toString();

        // shortcircuted evaluation
        Predicates.equalsToAny(Arrays.asList(FALSE, TRUE, dummy(Predicate.class))).op(TRUE);

    }

    /**
     * Tests that {@link Predicates#equalsToAny(Iterable)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyEqualsIterableNPE() {
        Predicates.equalsToAny((Iterable) null);
    }

    /**
     * Tests that {@link Predicates#equalsToAny(Iterable)} throws a {@link NullPointerException} when invoked with an
     * iterable containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyEqualsIterableNPE1() {
        Predicates.equalsToAny(PREDICATES_WITH_NULL_ITERABLE);
    }

    /**
     * Tests {@link Predicates#anyTrue(Iterable)}.
     */
    @Test
    public void anyIterable() {
        AnyPredicate<String> p = (AnyPredicate) Predicates.anyTrue(STRING_PREDICATE_ITERABLE);

        // evaluate
        assertFalse(p.op("fobo"));
        assertTrue(p.op("foobo"));
        assertTrue(p.op("foboo"));
        assertTrue(p.op("fooboo"));

        // getPredicates
        assertEquals(2, p.getPredicates().size());
        assertEquals(StringOps.startsWith("foo"), p.getPredicates().get(0));
        assertEquals(StringOps.contains("boo"), p.getPredicates().get(1));

        // iterable
        Iterator<?> i = p.iterator();
        assertEquals(StringOps.startsWith("foo"), i.next());
        assertEquals(StringOps.contains("boo"), i.next());
        assertFalse(i.hasNext());

        // Serializable
        TestUtil.assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        Predicates.anyTrue(TRUE_FALSE_TRUE_ITERABLE).toString();
        Predicates.anyTrue((Iterable) Arrays.asList()).toString();
        Predicates.anyTrue((Iterable) Arrays.asList(TRUE)).toString();

        // shortcircuted evaluation
        Predicates.anyTrue((Iterable) Arrays.asList(FALSE, TRUE, TestUtil.dummy(Predicate.class))).op(null);
    }

    /**
     * Tests that {@link Predicates#anyTrue(Iterable)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyIterableNPE() {
        Predicates.anyTrue((Iterable) null);
    }

    /**
     * Tests that {@link Predicates#anyTrue(Iterable)} throws a {@link NullPointerException} when invoked with an
     * iterable containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyIterableNPE1() {
        Predicates.anyTrue(PREDICATES_WITH_NULL_ITERABLE);
    }

    /**
     * Tests {@link Predicates#anyType(Class...)}.
     */
    @Test
    public void anyTypeArray() {
        Predicate p = Predicates.anyType(String.class, Number.class, LinkedList.class);

        // evaluate
        assertTrue(p.op("ddd"));
        assertTrue(p.op(1));
        assertTrue(p.op(1.0d));
        assertTrue(p.op(new LinkedList()));
        assertFalse(p.op(new ArrayList()));
        assertFalse(p.op(this));
        assertTrue(p.op(Byte.valueOf((byte) 3)));
        assertFalse(p.op(Boolean.FALSE));
        assertFalse(p.op(new Object()));

        // Serializable
        assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        p.toString();
        Predicates.anyType(new Class[] {}).toString();
        Predicates.anyType(new Class[] { Predicates.class }).toString();
    }

    /**
     * Tests that {@link Predicates#anyType(Class...)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyTypeArrayNPE() {
        Predicates.anyType((Class[]) null);
    }

    /**
     * Tests that {@link Predicates#anyType(Class...)} throws a {@link NullPointerException} when invoked with an array
     * containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyTypeArrayNPE1() {
        Predicates.anyType(String.class, Long.class, null, Integer.class);
    }

    /**
     * Tests {@link Predicates#anyType(Iterable)}.
     */
    @Test
    public void anyTypeIterable() {
        Predicate p = Predicates.anyType(Arrays.asList(String.class, Number.class, LinkedList.class));

        // evaluate
        assertTrue(p.op("ddd"));
        assertTrue(p.op(1));
        assertTrue(p.op(1.0d));
        assertTrue(p.op(new LinkedList()));
        assertFalse(p.op(new ArrayList()));
        assertFalse(p.op(this));
        assertTrue(p.op(Byte.valueOf((byte) 3)));
        assertFalse(p.op(Boolean.FALSE));
        assertFalse(p.op(new Object()));

        // Serializable
        assertIsSerializable(p);

        // toString, just check that they don't throw exceptions
        p.toString();
        Predicates.anyType((Iterable) Arrays.asList()).toString();
        Predicates.anyType(Arrays.asList(Predicates.class)).toString();
    }

    /**
     * Tests that {@link Predicates#anyType(Iterable)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyTypeIterableNPE() {
        Predicates.anyType((Iterable) null);
    }

    /**
     * Tests that {@link Predicates#anyType(Iterable)} throws a {@link NullPointerException} when invoked with an
     * iterable containing a <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void anyTypeIterableNPE1() {
        Predicates.anyType(Arrays.asList(String.class, Long.class, null, Integer.class));
    }

    @Test
    public void between() {
        Predicate b = Predicates.between(2, 4);
        assertFalse(b.op(1));
        assertTrue(b.op(2));
        assertTrue(b.op(3));
        assertTrue(b.op(4));
        assertFalse(b.op(5));
        assertIsSerializable(b);
    }

    @Test(expected = ClassCastException.class)
    public void betweenCCE() {
        Predicate p = Predicates.between(2, 4);
        p.op("foo");
    }

    @Test
    public void betweenComparator() {
        Predicate b = Predicates.between(Dummy.D2, Dummy.D4, COMP);
        assertFalse(b.op(Dummy.D1));
        assertTrue(b.op(Dummy.D2));
        assertTrue(b.op(Dummy.D3));
        assertTrue(b.op(Dummy.D4));
        assertFalse(b.op(Dummy.D5));
        assertIsSerializable(b);
    }

    @Test(expected = NullPointerException.class)
    public void betweenNPE() {
        Predicates.between(null, 2);
    }

    @Test(expected = NullPointerException.class)
    public void betweenNPE1() {
        Predicates.between(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void betweenNPE2() {
        Predicates.between(null, 2, COMP);
    }

    @Test(expected = NullPointerException.class)
    public void betweenNPE3() {
        Predicates.between(1, null, COMP);
    }

    @Test(expected = NullPointerException.class)
    public void betweenNPE4() {
        Predicates.between(1, 2, null);
    }

    /**
     * Tests {@link Predicates#FALSE}.
     */
    @Test
    public void falsePredicate() {
        assertFalse(FALSE.op(null));
        assertFalse(FALSE.op(this));
        assertSame(FALSE, Predicates.isFalse());
        FALSE.toString(); // does not fail
        assertIsSerializable(Predicates.isFalse());
        assertSame(FALSE, TestUtil.serializeAndUnserialize(FALSE));
    }

    /* Test greater then */
    @Test
    public void greaterThenComparable() {
        GreaterThenPredicate<Integer> f = (GreaterThenPredicate) greaterThen(5);
        assertEquals(5, f.getObject().intValue());
        assertNull(f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertFalse(f.op(4));
        assertFalse(f.op(5));
        assertTrue(f.op(6));

        f.toString(); // no exceptions
    }

    @Test
    public void greaterThenComperator() {
        GreaterThenPredicate<Dummy> f = (GreaterThenPredicate) greaterThen(Dummy.D2, COMP);
        assertEquals(Dummy.D2, f.getObject());
        assertEquals(COMP, f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertFalse(f.op(Dummy.D1));
        assertFalse(f.op(Dummy.D2));
        assertTrue(f.op(Dummy.D3));

        f.toString(); // no exceptions
    }

    @Test(expected = NullPointerException.class)
    public void greaterThenNPE() {
        greaterThen((Integer) null);
    }

    @Test(expected = NullPointerException.class)
    public void greaterThenNPE1() {
        greaterThen(null, COMP);
    }

    @Test(expected = NullPointerException.class)
    public void greaterThenNPE2() {
        greaterThen(2, null);
    };

    /* Test greaterTheOrEqual */
    @Test
    public void greaterThenOrEqualComparable() {
        GreaterThenOrEqualPredicate<Integer> f = (GreaterThenOrEqualPredicate) greaterThenOrEqual(5);
        assertEquals(5, f.getObject().intValue());
        assertNull(f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertFalse(f.op(4));
        assertTrue(f.op(5));
        assertTrue(f.op(6));

        f.toString(); // no exceptions
    }

    @Test
    public void greaterThenOrEqualComparator() {
        GreaterThenOrEqualPredicate<Dummy> f = (GreaterThenOrEqualPredicate) greaterThenOrEqual(Dummy.D2, COMP);
        assertEquals(Dummy.D2, f.getObject());
        assertEquals(COMP, f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertFalse(f.op(Dummy.D1));
        assertTrue(f.op(Dummy.D2));
        assertTrue(f.op(Dummy.D3));

        f.toString(); // no exceptions
    }

    @Test(expected = NullPointerException.class)
    public void greaterThenOrEqualNPE() {
        greaterThenOrEqual((Integer) null);
    }

    @Test(expected = NullPointerException.class)
    public void greaterThenOrEqualNPE1() {
        greaterThenOrEqual(null, COMP);
    }

    @Test(expected = NullPointerException.class)
    public void greaterThenOrEqualNPE2() {
        greaterThenOrEqual(2, null);
    }

    /**
     * Tests {@link Predicates#equalsTo(Object)}.
     */
    @Test
    public void isEquals() {
        assertTrue(Predicates.equalsTo("1").op("1"));
        assertTrue(Predicates.equalsTo(new HashMap()).op(new HashMap()));
        assertFalse(Predicates.equalsTo("1").op("2"));
        assertFalse(Predicates.equalsTo("1").op(null));

        IsEqualsPredicate p = new IsEqualsPredicate("1");
        assertEquals("1", p.getElement());
        p.toString(); // check no exception
        assertIsSerializable(p);
    }

    /**
     * Tests {@link Predicates#equalsTo(Object)}.
     */
    @Test
    public void notIsEquals() {
        assertFalse(Predicates.notEqualsTo("1").op("1"));
        assertFalse(Predicates.notEqualsTo(new HashMap()).op(new HashMap()));
        assertTrue(Predicates.notEqualsTo("1").op("2"));
        assertTrue(Predicates.notEqualsTo("1").op(null));

        Predicate p = Predicates.notEqualsTo("1");
        p.toString(); // check no exception
        assertIsSerializable(p);
    }

    /**
     * Tests that {@link Predicates#notEqualsTo(Object)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void notIsEqualsNPE() {
        Predicates.notEqualsTo(null);
    }

    /**
     * Tests that {@link Predicates#equalsTo(Object)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void isEqualsNPE() {
        Predicates.equalsTo(null);
    }

    /**
     * Tests {@link Predicates#isNull()}.
     */
    @Test
    public void isNull() {
        assertTrue(Predicates.isNull().op(null));
        assertFalse(Predicates.isNull().op(1));
        assertFalse(Predicates.isNull().op("f"));
        Predicates.IS_NULL.toString();// no fail
        TestUtil.assertIsSerializable(Predicates.IS_NULL);
        assertSame(Predicates.IS_NULL, TestUtil.serializeAndUnserialize(Predicates.IS_NULL));
    }

    /**
     * Tests {@link Predicates#isNotNull()}.
     */
    @Test
    public void isNotNull() {
        assertFalse(Predicates.isNotNull().op(null));
        assertTrue(Predicates.isNotNull().op(1));
        assertTrue(Predicates.isNotNull().op("f"));
        assertSame(Predicates.IS_NOT_NULL, Predicates.isNotNull());
        Predicates.IS_NOT_NULL.toString();// no fail
        TestUtil.assertIsSerializable(Predicates.IS_NOT_NULL);
        assertSame(Predicates.IS_NOT_NULL, TestUtil.serializeAndUnserialize(Predicates.IS_NOT_NULL));
    }

    /**
     * Tests {@link Predicates#sameAs(Object)}.
     */
    @Test
    public void isSame() {
        Object o = new Object();
        assertTrue(Predicates.sameAs(o).op(o));
        assertFalse(Predicates.sameAs(o).op(new Object()));
        assertFalse(Predicates.sameAs(o).op(null));
        assertEquals(new HashMap(), new HashMap());
        assertFalse(Predicates.sameAs(new HashMap()).op(new HashMap()));

        IsSamePredicate p = new IsSamePredicate("1");
        assertEquals("1", p.getElement());
        p.toString(); // check no exception
        assertIsSerializable(p);
    }

    /**
     * Tests that {@link Predicates#sameAs(Object)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> element.
     */
    @Test(expected = NullPointerException.class)
    public void isSameNPE() {
        Predicates.sameAs(null);
    }

    /**
     * Tests {@link Predicates#isTypeOf(Class)}.
     */
    @Test
    public void isType() {
        assertTrue(Predicates.isTypeOf(Object.class).op(new Object()));
        assertTrue(Predicates.isTypeOf(Object.class).op(1));
        assertTrue(Predicates.isTypeOf(Object.class).op(new HashMap()));

        assertFalse(Predicates.isTypeOf(Number.class).op(new Object()));
        assertTrue(Predicates.isTypeOf(Number.class).op(1));
        assertTrue(Predicates.isTypeOf(Number.class).op(1L));
        assertFalse(Predicates.isTypeOf(Number.class).op(new HashMap()));

        IsTypePredicate p = new IsTypePredicate(Map.class);
        assertEquals(Map.class, p.getType());
        assertTrue(p.op(new HashMap()));
        assertFalse(p.op(new Object()));
        TestUtil.assertIsSerializable(p);
    }

    /**
     * Tests that {@link Predicates#isTypeOf(Class)} throws a {@link IllegalArgumentException} when invoked with a
     * pritimive type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void isTypeIAE() {
        Predicates.isTypeOf(Long.TYPE);
    }

    /**
     * Tests that {@link Predicates#isTypeOf(Class)} throws a {@link NullPointerException} when invoked with a right
     * side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void isTypeNPE() {
        Predicates.isTypeOf(null);
    }

    /* Test lessThen */
    @Test
    public void lessThenComparable() {
        LessThenPredicate<Integer> f = (LessThenPredicate) lessThen(5);
        assertEquals(5, f.getObject().intValue());
        assertNull(f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertTrue(f.op(4));
        assertFalse(f.op(5));
        assertFalse(f.op(6));

        f.toString(); // no exceptions
    }

    @Test
    public void lessThenComparator() {

        LessThenPredicate<Dummy> f = (LessThenPredicate) lessThen(Dummy.D2, COMP);
        assertEquals(Dummy.D2, f.getObject());
        assertEquals(COMP, f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertTrue(f.op(Dummy.D1));
        assertFalse(f.op(Dummy.D2));
        assertFalse(f.op(Dummy.D3));

        f.toString(); // no exceptions
    }

    @Test(expected = NullPointerException.class)
    public void lessThenNPE() {
        lessThen((Integer) null);
    }

    @Test(expected = NullPointerException.class)
    public void lessThenNPE1() {
        lessThen(null, COMP);
    }

    @Test(expected = NullPointerException.class)
    public void lessThenNPE2() {
        lessThen(2, null);
    }

    /* Test lessThenOrEqual */
    @Test
    public void lessThenOrEqualComparable() {
        LessThenOrEqualPredicate<Integer> f = (LessThenOrEqualPredicate) lessThenOrEqual(5);
        assertEquals(5, f.getObject().intValue());
        assertNull(f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertTrue(f.op(4));
        assertTrue(f.op(5));
        assertFalse(f.op(6));

        f.toString(); // no exceptions
    }

    @Test
    public void lessThenOrEqualComparator() {
        LessThenOrEqualPredicate<Dummy> f = (LessThenOrEqualPredicate) lessThenOrEqual(Dummy.D2, COMP);
        assertEquals(Dummy.D2, f.getObject());
        assertEquals(COMP, f.getComparator());
        TestUtil.assertIsSerializable(f);
        assertTrue(f.op(Dummy.D1));
        assertTrue(f.op(Dummy.D2));
        assertFalse(f.op(Dummy.D3));

        f.toString(); // no exceptions
    }

    @Test(expected = NullPointerException.class)
    public void lessThenOrEqualNPE() {
        lessThenOrEqual((Integer) null);
    }

    @Test(expected = NullPointerException.class)
    public void lessThenOrEqualNPE1() {
        lessThenOrEqual(null, COMP);
    }

    @Test(expected = NullPointerException.class)
    public void lessThenOrEqualNPE2() {
        lessThenOrEqual(2, null);
    }

    @Test
    public void mapperPredicate() {
        Predicate<Number> p = (Predicate) Predicates.equalsToAny(4, 16);
        Op<Integer, Integer> m = new Op<Integer, Integer>() {
            public Integer op(Integer from) {
                return from.intValue() * from.intValue();
            }
        };
        Predicate mapped = Predicates.mapAndEvaluate(m, p);
        assertTrue(mapped.op(2));
        assertFalse(mapped.op(3));
        assertTrue(mapped.op(4));

        assertSame(p, ((MapAndEvaluatePredicate) mapped).getPredicate());
        assertSame(m, ((MapAndEvaluatePredicate) mapped).getMapper());
        mapped.toString();
    }

    @Test(expected = NullPointerException.class)
    public void mapperPredicateNPE1() {
        Predicates.mapAndEvaluate(null, TestUtil.dummy(Predicate.class));
    }

    @Test(expected = NullPointerException.class)
    public void mapperPredicateNPE2() {
        Predicates.mapAndEvaluate(TestUtil.dummy(Op.class), null);
    }

    /**
     * Tests {@link Predicates#not(Predicate)}.
     */
    @Test
    public void not() {
        assertFalse(Predicates.not(TRUE).op(null));
        assertTrue(Predicates.not(FALSE).op(null));
        assertFalse(Predicates.not(Predicates.IS_NULL).op(null));
        assertTrue(Predicates.not(Predicates.IS_NOT_NULL).op(null));
        assertFalse(Predicates.not(new Predicate() {
            public boolean op(Object a) {
                return true;
            }
        }).op(null));
        assertTrue(Predicates.not(Predicates.not(new Predicate() {
            public boolean op(Object a) {
                return true;
            }
        })).op(null)); 
        NotPredicate p = new NotPredicate(TRUE);
        assertSame(p.getPredicate(), TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);
    }

    /**
     * Tests that {@link Predicates#not(Predicate)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void notNPE() {
        Predicates.not(null);
    }

    /**
     * Tests {@link Predicates#notNullAnd(Predicate)}.
     */
    @Test
    public void notNullAnd() {
        Predicate<Integer> p = Predicates.notNullAnd(Predicates.equalsToAny(1, 2));
        assertFalse(p.op(null));
        assertTrue(p.op(1));
        assertFalse(p.op(3));
        p.toString();
        assertIsSerializable(p);
    }

    /**
     * Tests that {@link Predicates#notNullAnd(Predicate)} throws a {@link NullPointerException} when invoked with a
     * <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void notNullAndNPE() {
        Predicates.notNullAnd(null);
    }

    /**
     * Tests {@link Predicates#or(Predicate, Predicate)}.
     */
    @Test
    public void or() {
        assertTrue(Predicates.or(TRUE, TRUE).op(null));
        assertTrue(Predicates.or(TRUE, FALSE).op(null));
        assertTrue(Predicates.or(FALSE, TRUE).op(null));
        assertFalse(Predicates.or(FALSE, FALSE).op(null));

        OrPredicate p = new OrPredicate(FALSE, TRUE);
        assertSame(p.getLeftPredicate(), FALSE);
        assertSame(p.getRightPredicate(), TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);

        // shortcircuted evaluation
        Predicates.or(TRUE, TestUtil.dummy(Predicate.class)).op(null);
    }

    /**
     * Tests that {@link Predicates#or(Predicate, Predicate)} throws a {@link NullPointerException} when invoked with a
     * left side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE() {
        Predicates.or(null, TRUE);
    }

    /**
     * Tests that {@link Predicates#or(Predicate, Predicate)} throws a {@link NullPointerException} when invoked with a
     * right side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void orNPE1() {
        Predicates.or(TRUE, null);
    }

    /**
     * Tests {@link Predicates#TRUE}.
     */
    @Test
    public void truePredicate() {
        assertTrue(TRUE.op(null));
        assertTrue(TRUE.op(this));
        assertSame(TRUE, Predicates.isTrue());
        TRUE.toString(); // does not fail
        assertIsSerializable(Predicates.isTrue());
        assertSame(TRUE, TestUtil.serializeAndUnserialize(TRUE));
    }

    /**
     * Tests {@link Predicates#xor(Predicate, Predicate)}.
     */
    @Test
    public void xor() {
        assertFalse(Predicates.xor(TRUE, TRUE).op(null));
        assertTrue(Predicates.xor(TRUE, FALSE).op(null));
        assertTrue(Predicates.xor(FALSE, TRUE).op(null));
        assertFalse(Predicates.xor(FALSE, FALSE).op(null));

        XorPredicate p = new XorPredicate(FALSE, TRUE);
        assertSame(p.getLeftPredicate(), FALSE);
        assertSame(p.getRightPredicate(), TRUE);
        p.toString(); // no exception
        assertIsSerializable(p);
    }

    /**
     * Tests that {@link Predicates#xor(Predicate, Predicate)} throws a {@link NullPointerException} when invoked with a
     * left side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void xorNPE() {
        Predicates.xor(null, TRUE);
    }

    /**
     * Tests that {@link Predicates#xor(Predicate, Predicate)} throws a {@link NullPointerException} when invoked with a
     * right side <code>null</code> argument.
     */
    @Test(expected = NullPointerException.class)
    public void xorNPE1() {
        Predicates.xor(TRUE, null);
    }
}
