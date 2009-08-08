package org.codehaus.cake.cache.test.tck.view;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.codehaus.cake.util.Iterables;
import org.codehaus.cake.util.collection.View;
import org.codehaus.cake.util.ops.StringOps;

public class ViewAsserts {

    static final Comparator<Integer> MAX_EVEN = new Comparator<Integer>() {
        public int compare(Integer o1, Integer o2) {
            if (o1.intValue() % 2 != o1.intValue() % 2) {
                return o1.intValue() % 2 - o1.intValue() % 2;
            }
            return o1.intValue() - o2.intValue();
        }
    };

    static <T> void assertOrderedViewEquals(View<T> v, List<T> list, Class<T> type) {
        assertViewEquals(v, list, type);

        assertArrayEquals(list.toArray(), v.toArray());
        assertEquals(list, v.toList());

    }

    static <T> void assertViewEquals(View<T> v, Collection<T> list, Class<T> type) {

    }

    static <T> void assertViewEquals(View<T> v, Collection<T> list, Class<T> type, boolean isOrdered) {
        // any
        assertTrue(list.contains(v.any(null)));
        // apply (currently ignores any ordering)
        Queue<T> res = new ConcurrentLinkedQueue<T>();
        v.apply(Iterables.offerToQueue(res));
        assertEquals(list.size(), res.size());
        assertEquals(new HashSet<T>(list), new HashSet<T>(res));
        //

        // Max min Natural
        if (list.size() > 0) {
            if (type.isAssignableFrom(Comparable.class)) {
                assertEquals(Collections.max((Collection) list), v.max());
                assertEquals(Collections.min((Collection) list), v.min());
            }
            if (type == Integer.class) {
                assertEquals(Collections.max((Collection) list, MAX_EVEN), v.orderBy((Comparator) MAX_EVEN).max());
                assertEquals(Collections.min((Collection) list, MAX_EVEN), v.orderBy((Comparator) MAX_EVEN).min());
                List<Integer> numbers = new ArrayList(list);
                Collections.sort(numbers, MAX_EVEN);
                assertEquals(numbers, v.orderBy((Comparator) MAX_EVEN).toList());
            }
        }
        if (type == Integer.class) {
            List<String> l = new ArrayList<String>();
            for (Object o : list) {
                l.add(o.toString());
            }
            if (isOrdered) {
                assertEquals(l, v.map(StringOps.toStringOp()).toList());
            } else {
                assertEquals(new HashSet(l), new HashSet(v.map(StringOps.toStringOp()).toList()));
            }
        }

        // Set Limit
        if (list.size() > 1) {
            assertEquals(1, v.setLimit(1).size());
            assertEquals(1, v.setLimit(1).toList().size());
            assertEquals(list.size() - 1, v.setLimit(list.size() - 1).size());
            assertEquals(list.size() - 1, v.setLimit(list.size() - 1).toArray().length);
            //Todo do some ordered tests
        }
        // ToArray
        Object[] a = v.toArray();
        assertEquals(list.size(), a.length);
        if (isOrdered) {
            assertArrayEquals(list.toArray(), v.toArray());
        } else {
            assertEquals(new HashSet(list), new HashSet(Arrays.asList(a)));
        }
        //ToArray - array arg
        // size
        assertEquals(list.size(), v.size());
    }

}
