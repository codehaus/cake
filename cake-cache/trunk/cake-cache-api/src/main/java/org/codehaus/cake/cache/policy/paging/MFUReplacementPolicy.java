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
package org.codehaus.cake.cache.policy.paging;

import static org.codehaus.cake.cache.CacheEntry.HITS;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.AbstractHeapReplacementPolicy;

/**
 * A Most Frequently Used (MFU) replacement policy.
 * <p>
 * This policy is seldom used. However, it can be used in some situations. See, for example,
 * http://citeseer.ist.psu.edu/mekhiel95multilevel.html
 * 
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of values maintained by the cache
 */
public class MFUReplacementPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "MFU";

    /** Creates a new MFUReplacementPolicy. */
    public MFUReplacementPolicy() {
        // This is used to make sure that the cache will lazy register the HITS attribute
        // if the user has not already done so by using CacheAttributeConfiguration#add(Attribute...)}
        dependSoft(HITS);
    }

    /** {@inheritDoc} */
    @Override
    protected int compareEntry(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -HITS.compare(o1, o2);
    }

    /** {@inheritDoc} */
    @Override
    public void touch(CacheEntry<K, V> entry) {
        siftUp(entry);
    }
}
