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
package org.codehaus.cake.internal.cache.service.memorystore;

import org.codehaus.cake.cache.CacheEntry;

/**
 * A <tt>Pair</tt> consists of two references to two other objects. The two objects that are being wrapped are
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id$
 */
@Deprecated
public final class SingleEntryUpdate<K, V> {
    /** The first instance. */
    private final CacheEntry<K, V> instance1;

    /** The second instance. */
    private final CacheEntry<K, V> instance2;

    private final Iterable<CacheEntry<K, V>> trimmed;

    /**
     * Constructs a new Pair. <tt>null</tt> values are permitted.
     * 
     * @param item1
     *            the first item in the Pair
     * @param item2
     *            the second item in the Pair
     */
    public SingleEntryUpdate(final CacheEntry<K, V> item1, final CacheEntry<K, V> item2,
            Iterable<CacheEntry<K, V>> trimmed) {
        this.instance1 = item1;
        this.instance2 = item2;
        this.trimmed = trimmed;
    }

    // public static <K, V> EntryPair<K, V> nullPair() {
    // return NULLPAIR;
    // }

    /** @return any previous entry that was removed. */
    public CacheEntry<K, V> getPrevious() {
        return instance1;
    }

    /** @return the entry that was just created. */
    public CacheEntry<K, V> getNewEntry() {
        return instance2;
    }

    public Iterable<CacheEntry<K, V>> trimmed() {
        return trimmed;
    }
}
