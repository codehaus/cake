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

import java.util.Random;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractArrayReplacementPolicy;

/**
 * A replacement policy that picks a random element to evict.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: RandomPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of values maintained by the cache
 */
public class RandomReplacementPolicy<K, V> extends AbstractArrayReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "Random";

    private final Random rnd = new Random();

    /** {@inheritDoc} */
    public CacheEntry<K, V> evictNext() {
        int size = size();
        return size == 0 ? null : removeByIndex(rnd.nextInt(size));
    }
}
