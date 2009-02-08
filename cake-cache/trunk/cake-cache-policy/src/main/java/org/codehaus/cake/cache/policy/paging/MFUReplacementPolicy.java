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

import static org.codehaus.cake.internal.cache.CacheEntryAttributes.HITS;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.policy.AbstractHeapReplacementPolicy;
import org.codehaus.cake.cache.policy.spi.PolicyContext;

/**
 * A Most Frequently Used (MFU) replacement policy.
 * <p>
 * This policy is seldom used. However, it can be used in some situations. See, for example,
 * http://citeseer.ist.psu.edu/mekhiel95multilevel.html
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <T>
 *            the type of elements being cached
 */
public class MFUReplacementPolicy<T extends AttributeMap> extends AbstractHeapReplacementPolicy<T> {

    /** The name of the policy. */
    public static final String NAME = "MFU";

    /**
     * Creates a new MFUReplacementPolicy.
     * 
     * @param context
     *            a policy context instance
     * @throws NullPointerException
     *             if the specified context is null
     */
    public MFUReplacementPolicy(PolicyContext<T> context) {
        super(context);
        // This is used to make sure that the cache will lazy register the HITS attribute
        // if the user has not already done so by using CacheAttributeConfiguration#add(Attribute...)}
        context.dependSoft(HITS);
    }

    /** {@inheritDoc} */
    @Override
    protected int compareEntry(T o1, T o2) {
        return HITS.compare(o2, o1);
    }

    /** {@inheritDoc} */
    @Override
    public void touch(T entry) {
        siftUp(entry);
    }
}
