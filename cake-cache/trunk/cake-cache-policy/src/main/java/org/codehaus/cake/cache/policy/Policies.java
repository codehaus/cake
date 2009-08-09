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
package org.codehaus.cake.cache.policy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.cake.internal.cache.policy.FakePolicyContext;

/**
 * Provides the type of all common replacement policies available in Cake. In addition to this, provides a number of
 * method that useful for creating {@link PolicyContext} instances that can be used while testing.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public final class Policies {

    /** A First In First Out replacement policy. */
    public final static Class<? extends ReplacementPolicy> FIFO = FIFOReplacementPolicy.class;

    /** A Least Frequenty Used policy. */
    public final static Class<? extends ReplacementPolicy> LFU = LFUReplacementPolicy.class;

    /** A Last In First Out replacement policy. */
    public final static Class<? extends ReplacementPolicy> LIFO = LIFOReplacementPolicy.class;

    /** A Least Recently Used replacement policy. */
    public final static Class<? extends ReplacementPolicy> LRU = LRUReplacementPolicy.class;

    /** A Most Frequently Used replacement policy. */
    public final static Class<? extends ReplacementPolicy> MFU = MFUReplacementPolicy.class;

    /** A Most Recently Used replacement policy. */
    public final static Class<? extends ReplacementPolicy> MRU = MRUReplacementPolicy.class;

    /** A Random replacement policy. */
    public final static Class<? extends ReplacementPolicy> RANDOM = RandomReplacementPolicy.class;

    /** Cannot instantiate. */
    private Policies() {
    }

    /**
     * Creates a new ReplacementPolicy of the specified type.
     * 
     * If the specified replacement type has a constructor taking a {@link PolicyContext}, this method will
     * automatically create a policy context. This is especially usefull while testing replacement policies that needs a
     * {@link PolicyContext} instance. If the specified replacement type does not a constructor taking a policy context,
     * this method will attempt to create an instance of the replacement policy using a public empty constructor.
     * 
     * @param <T>
     *            the type of replacement policy
     * @param clazz
     *            the type of replacement policy
     * @throws IllegalArgumentException
     *             if the specified class does not a public empty constructor or a public constructor taking a single
     *             PolicyContext argument
     * @return the newly created policy
     */
    public static <T extends ReplacementPolicy> T create(Class<T> clazz) {
        return create(clazz, new FakePolicyContext(Object.class));
    }

    /**
     * Creates a new ReplacementPolicy of the specified type.
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
     *             if the specified class does not a public empty constructor or a public constructor taking a single
     *             PolicyContext argument
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

}
