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
package org.codehaus.cake.cache.policy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.RandomReplacementPolicy;
import org.codehaus.cake.cache.policy.spi.PolicyContext;

/**
 * Factory methods for different {@link ReplacementPolicy} implementations. This class provides shortcuts for various
 * replacement policy implementations.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public final class Policies {

    /** A First In First Out replacement policy. */
    public final static Class<? extends ReplacementPolicy> FIFO = LRUReplacementPolicy.class;
    /** A Least Frequenty Used policy. */
    public final static Class<? extends ReplacementPolicy> LFU = LRUReplacementPolicy.class;
    /** A Last In First Out replacement policy. */
    public final static Class<? extends ReplacementPolicy> LIFO = LRUReplacementPolicy.class;
    /** A Least Recently Used replacement policy. */
    public final static Class<? extends ReplacementPolicy> LRU = LRUReplacementPolicy.class;
    /** A Most Frequently Used replacement policy. */
    public final static Class<? extends ReplacementPolicy> MFU = LRUReplacementPolicy.class;
    /** A Most Recently Used replacement policy. */
    public final static Class<? extends ReplacementPolicy> MRU = LRUReplacementPolicy.class;
    /** A Random replacement policy. */
    public final static Class<? extends ReplacementPolicy> RANDOM = RandomReplacementPolicy.class;

    // /CLOVER:OFF
    /** Cannot instantiate. */
    private Policies() {
    }

    // /CLOVER:ON

    /**
     * Creates a new ReplacementPolicy of the specified type.
     * 
     * If the specified replacement type requires a {@link PolicyContext}
     * 
     * @param <T>
     *            the type of replacement policy
     * @param clazz
     *            the type of replacement policy
     * @return the newly created policy
     */
    public static <T extends ReplacementPolicy> T create(Class<T> clazz) {
        return create(clazz, new FakePolicyContext(Object.class));
    }

    /**
     * Creates a new ReplacementPolicy of the specified type
     * 
     * @param <T>
     *            the type of replacement policy
     * @param clazz
     *            the type of replacement policy
     * @param context
     *            the context to used when creating the policy
     * @throws NullPointerException
     *             if the specified clazz or context is null
     * @throws IllegalArgumentException
     *             if an instance of the specified replacement policy type could not be created
     * @return the newly created policy
     */
    public static <T extends ReplacementPolicy> T create(Class<T> clazz, PolicyContext context) {
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        } else if (context == null) {
            throw new NullPointerException("context is null");
        }
        try {
            return create(clazz.getConstructor(PolicyContext.class), context);
        } catch (NoSuchMethodException e) {
            try {
                return create(clazz.getConstructor(), null);
            } catch (NoSuchMethodException e1) {
                throw new IllegalArgumentException(
                        "No public empty constructor, or public constructor taking a single PolicyContext argument");
            }
        }
    }

    private static <T extends ReplacementPolicy> T create(Constructor<T> constructor, PolicyContext context) {
        try {
            if (context != null) {
                return constructor.newInstance(context);
            }
            return constructor.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static class FakePolicyContext<T> implements PolicyContext<T> {

        private final Class<T> type;

        public FakePolicyContext(Class<T> type) {
            if (type == null) {
                throw new NullPointerException("type is null");
            }
            this.type = type;
        }

        public BooleanAttachment attachBoolean() {
            return new BA();
        }

        public IntAttachment attachInt() {
            return new IA();
        }

        public <U> ObjectAttachment<U> attachObject(Class<U> type) {
            return new OA();
        }

        public ObjectAttachment<T> attachSelfReference() {
            return (ObjectAttachment) attachObject(Object.class);
        }

        public void dependHard(Attribute<?> attribute) {
        }

        public void dependSoft(Attribute<?> attribute) {
        }

        public Class getElementType() {
            return Object.class;
        }

        public T[] newArray(int size) {
            return (T[]) new Object[size];
        }

        static class BA implements BooleanAttachment {
            final IdentityHashMap<Object, Boolean> map = new IdentityHashMap<Object, Boolean>();

            public boolean get(Object entry) {
                Boolean b = map.get(entry);
                if (b == null) {
                    throw new IllegalArgumentException("The value of this attachment has not been set previously");
                }
                return b.booleanValue();
            }

            public void set(Object entry, boolean value) {
                map.put(entry, Boolean.valueOf(value));
            }
        }

        static class IA implements IntAttachment {
            final IdentityHashMap<Object, Integer> map = new IdentityHashMap<Object, Integer>();

            public int get(Object entry) {
                Integer i = map.get(entry);
                if (i == null) {
                    throw new IllegalArgumentException("The value of this attachment has not been set previously");
                }
                return i.intValue();
            }

            public int get(Object entry, int defaultValue) {
                Integer i = map.get(entry);
                if (i == null) {
                    return defaultValue;
                }
                return i.intValue();
            }

            public void set(Object entry, int value) {
                map.put(entry, Integer.valueOf(value));
            }
        }

        static class OA implements ObjectAttachment {
            final IdentityHashMap<Object, Object> map = new IdentityHashMap<Object, Object>();

            public Object get(Object entry) {
                return map.get(entry);
            }

            public void set(Object entry, Object value) {
                map.put(entry, value);
            }

        }
    }

}
