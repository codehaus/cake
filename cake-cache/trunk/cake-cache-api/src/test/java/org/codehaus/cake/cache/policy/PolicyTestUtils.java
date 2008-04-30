/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.policy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.util.CollectionUtils;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PolicyTestUtils.java 526 2007-12-27 01:32:16Z kasper $
 */
public final class PolicyTestUtils {
    static final DummyEntry[] VALUES;

    private PolicyTestUtils() {}

    static {
        VALUES = new DummyEntry[1000];
        for (int i = 0; i < VALUES.length; i++) {
            VALUES[i] = new DummyEntry(i, "" + i);
        }
    }

    public static CacheEntry<Integer, String> val(int key) {
        return VALUES[key];
    }

    public static void add(ReplacementPolicy<Integer, String> policy, int key) {
        policy.add(VALUES[key]);
    }

    public static void addToPolicy(ReplacementPolicy<Integer, String> policy, int start, int stop) {
        for (int i = start; i <= stop; i++) {
            policy.add(VALUES[i]);
        }
    }

    public static Integer[] evict(ReplacementPolicy<Integer, String> policy, int num) {
        if (num > 1000)
            throw new IllegalArgumentException("must be <1000");
        Integer[] result = new Integer[num];
        for (int i = 0; i < result.length; i++) {
            result[i] = policy.evictNext().getKey();
        }
        return result;
    }

    public static List<Integer> evictList(ReplacementPolicy<Integer, String> policy, int num) {
        Integer[] result = new Integer[num];
        for (int i = 0; i < result.length; i++) {
            result[i] = policy.evictNext().getKey();
        }
        return Arrays.asList(result);
    }

    public static List<Integer> empty(ReplacementPolicy<Integer, String> policy) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (;;) {
            CacheEntry<Integer, String> ce = policy.evictNext();
            if (ce != null)
                list.add(ce.getKey());
            else
                break;
        }
        return list;
    }

    static class DummyEntry extends CollectionUtils.SimpleImmutableEntry<Integer, String> implements
            CacheEntry<Integer, String> {
        private final AttributeMap map = new DefaultAttributeMap();

        public DummyEntry(Integer key, String value) {
            super(key, value);
        }

        public AttributeMap getAttributes() {
            return map;
        }

    }
}
