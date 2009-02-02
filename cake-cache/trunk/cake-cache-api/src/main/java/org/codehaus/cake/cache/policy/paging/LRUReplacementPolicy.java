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

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.AbstractDoubleLinkedReplacementPolicy;
import org.codehaus.cake.cache.policy.spi.PolicyContext;

/**
 * A Least Recently Used (LRU) replacement policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of values maintained by the cache
 */
public class LRUReplacementPolicy<T> extends AbstractDoubleLinkedReplacementPolicy<T> {

    public LRUReplacementPolicy(PolicyContext<T> context) {
        super(context);
    }

    /** A unique policy name. */
    public static final String NAME = "LRU";

    /** {@inheritDoc} */
    public void add(T entry) {
        addFirst(entry);
    }

    /** {@inheritDoc} */
    @Override
    public void touch(T entry) {
        moveFirst(entry);
    }

    /** {@inheritDoc} */
    public T evictNext() {
        return removeLast();
    }
}
