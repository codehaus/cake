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
package org.codehaus.cake.internal.cache;

import java.io.Serializable;

import org.codehaus.cake.cache.CacheDataExtractor;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.ops.Ops.Op;

public class InternalDataExtractors {
    public static final Op EXTRACT_BOOLEAN = new InternalExtractIsNotNullOp();
    public static final Op EXTRACT_KEY = new InternalExtractKeyOp();
    public static final Op EXTRACT_VALUE = new InternalExtractValueOp();

    public static class ExtractAttribute<K, V, T> extends CacheDataExtractor<K, V, T> implements Serializable {
        private final Attribute<T> attribute;

        public ExtractAttribute(Attribute<T> attribute) {
            if (attribute == null) {
                throw new NullPointerException("attribute is null");
            }
            this.attribute = attribute;
        }

        public T op(CacheEntry<K, V> a) {
            if (a == null) {
                return attribute.getDefault();
            }
            return a.get(attribute);
        }
    }

    static class InternalExtractIsNotNullOp<K, V> implements Op<CacheEntry<K, V>, Boolean>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Creates a new SizeAttribute. */
        InternalExtractIsNotNullOp() {
        }

        /** {@inheritDoc} */
        public Boolean op(CacheEntry<K, V> a) {
            return a == null ? Boolean.FALSE : Boolean.TRUE;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EXTRACT_BOOLEAN;
        }
    }

    static class InternalExtractKeyOp<K, V> implements Op<CacheEntry<K, V>, K>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Creates a new SizeAttribute. */
        InternalExtractKeyOp() {
        }

        /** {@inheritDoc} */
        public K op(CacheEntry<K, V> a) {
            if (a == null) {
                return null;
            }
            return a.getKey();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EXTRACT_KEY;
        }
    }

    static class InternalExtractValueOp<K, V> implements Op<CacheEntry<K, V>, V>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** Creates a new SizeAttribute. */
        InternalExtractValueOp() {
        }

        /** {@inheritDoc} */
        public V op(CacheEntry<K, V> a) {
            if (a == null) {
                return null;
            }
            return a.getValue();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EXTRACT_VALUE;
        }
    }
}
