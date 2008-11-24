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

import java.util.List;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops;
import org.codehaus.cake.ops.Ops.Op;

public abstract class CacheDataExtractor<K, V, T> implements Op<CacheEntry<K, V>, T> {
    public static final Op WHOLE_ENTRY = ObjectOps.CONSTANT_OP;

    public static final Op ONLY_KEY = new Ops.Op<CacheEntry<?, ?>, Object>() {
        public Object op(CacheEntry<?, ?> e) {
            return e == null ? null : e.getKey();
        }
    };
    public static final Op IS_NOT_NULL = new Ops.Op<CacheEntry<?, ?>, Object>() {
        public Object op(CacheEntry<?, ?> e) {
            return Boolean.valueOf(e != null);
        }
    };

    public static <K, V, T> Op<CacheEntry<K, V>, T> toAttribute(Attribute<T> attribute) {
        return new ExtractAttribute<K, V, T>(attribute);
    }

    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromValue(Op<V, T> op) {
        return null;
    }

    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromValueBean(String name) {
        return null;
    }

    public static <K, V, T> Op<CacheEntry<K, V>, T> extractFromKey(Op<K, T> op) {
        return null;
    }

    public static final Op ONLY_VALUE = new Ops.Op<CacheEntry<?, ?>, Object>() {
        public Object op(CacheEntry<?, ?> e) {
            return e == null ? null : e.getValue();
        }
    };

    boolean retainKey;
    boolean retainValue;
    List retainAttributes;

    static class ExtractAttribute<K, V, T> extends CacheDataExtractor<K, V, T> {
        private final Attribute<T> attribute;

        public T op(CacheEntry<K, V> a) {
            if (a == null) {
                return attribute.getDefault();
            }
            return a.getAttributes().get(attribute);
        }

        public ExtractAttribute(Attribute<T> attribute) {
            if (attribute == null) {
                throw new NullPointerException("attribute is null");
            }
            this.attribute = attribute;
        }
    }
}
