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
package org.codehaus.cake.cache.test.operations;

import org.codehaus.cake.cache.Cache;

public class CacheOps {

    static final String PREFIX = "test.opr.cache.";

    @TestOperation(PREFIX + "get")
    public static class Get<K, V> extends AbstractOperation<K, V> {
        /** {@inheritDoc} */
        public void run() {
            cache.get(keyGenerator.op());
        }
    }

    /**
     * Invokes the {@link Cache#clear()} method.
     */
    public static class EntrySetIterateAll<K, V> extends AbstractOperation<K, V> {
        /** The name of this operation. */
        public static final String NAME = PREFIX + "entrySetIterateAll";

        /** {@inheritDoc} */
        public void run() {
            synchronized (cache) {
                int currentSize = cache.asMap().size();
                int size = 0;
                for (Object o : cache.asMap().entrySet()) {
                    size++;
                }
                if (size != currentSize) {
                    throw new AssertionError("Size difference should be 0, expected " + currentSize + ", was " + size);
                }
            }
        }
    }

    /**
     * Invokes the {@link Cache#clear()} method.
     */
    public static class Clear<K, V> extends AbstractOperation<K, V> {
        /** The name of this operation. */
        public static final String NAME = PREFIX + "clear";

        /** {@inheritDoc} */
        public void run() {
            cache.clear();
        }
    }

    /**
     * Invokes the {@link Cache#shutdown()} method.
     */
    public static class Shutdown<K, V> extends AbstractOperation<K, V> {
        /** The name of this operation. */
        public static final String NAME = PREFIX + "shutdown";

        /** {@inheritDoc} */
        public void run() {
            cache.shutdown();
        }
    }

    /**
     * Invokes the {@link Cache#shutdownNow()} method.
     */
    public static class ShutdownNow<K, V> extends AbstractOperation<K, V> {
        /** The name of this operation. */
        public static final String NAME = PREFIX + "shutdownNow";

        /** {@inheritDoc} */
        public void run() {
            cache.shutdown();
        }
    }

    public static class Peek<K, V> extends AbstractOperation<K, V> {
        public static final String NAME = PREFIX + "peek";

        /** {@inheritDoc} */
        public void run() {
            cache.peek(keyGenerator.op());
        }
    }
}
