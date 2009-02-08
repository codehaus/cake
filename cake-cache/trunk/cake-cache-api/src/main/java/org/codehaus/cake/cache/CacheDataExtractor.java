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
package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.internal.cache.InternalDataExtractors;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.Op;

/**
 * A utility class that can ve used for creating transformers that extract data from {@link CacheEntry CacheEntries}.
 * 
 * TODO put in cache.util
 */
public abstract class CacheDataExtractor<K, V, T> implements Op<CacheEntry<K, V>, T> {
    // // /CLOVER:OFF
    // /** Cannot instantiate. */
    // private CacheDataExtractor() {
    // }
    //
    // // /CLOVER:ON

    /**
     * If the entry being passed to the transformer is <code>null</code> return {@link Boolean#FALSE} otherwise
     * returns {@link Boolean#TRUE}.
     */
    @SuppressWarnings("unchecked")
    public static final Op IS_NOT_NULL = InternalDataExtractors.EXTRACT_BOOLEAN;

    /**
     * Extracts the key part of an entry by calling {@link CacheEntry#getKey()}. If <code>null</code> is passed to
     * the transformer, <code>null</code> is returned
     */
    @SuppressWarnings("unchecked")
    public static final Op ONLY_KEY = InternalDataExtractors.EXTRACT_KEY;

    /**
     * Extracts the value part of an entry by calling {@link CacheEntry#getValue()}. If <code>null</code> is passed
     * to the transformer, <code>null</code> is returned
     */
    @SuppressWarnings("unchecked")
    public static final Op ONLY_VALUE = InternalDataExtractors.EXTRACT_VALUE;

    /** Returns the entry that is passed to the transformer, or <code>null</code> if <code>null</code> is passed. */
    public static final Op WHOLE_ENTRY = ObjectOps.CONSTANT_OP;

    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromKey(Op<K, T> op) {
        return ObjectOps.compoundMapper(ONLY_KEY, op);
    }

    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromValue(Op<V, T> op) {
        return ObjectOps.compoundMapper(ONLY_VALUE, op);
    }

    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromValueBean(String name, Class<T> type) {
        return null;
    }

    /**
     * Creates an {@link Op} that can extract the specified attribute from a {@link CacheEntry}.
     * 
     * @param <K>
     * @param <V>
     * @param <T>
     * @param attribute
     *            the attribute that the Op should
     * @return
     */
    public static <K, V, T> CacheDataExtractor<K, V, T> toAttribute(Attribute<T> attribute) {
        return new InternalDataExtractors.ExtractAttribute<K, V, T>(attribute);
    }
}
