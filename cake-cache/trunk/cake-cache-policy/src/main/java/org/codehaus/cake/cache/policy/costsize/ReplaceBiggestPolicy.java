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
package org.codehaus.cake.cache.policy.costsize;

import static org.codehaus.cake.internal.cache.CacheEntryAttributes.SIZE;

import org.codehaus.cake.cache.policy.AbstractHeapReplacementPolicy;
import org.codehaus.cake.cache.policy.PolicyContext;
import org.codehaus.cake.util.attribute.AttributeMap;

/**
 * A replacement policy that replaces the entry with the biggest {@link org.codehaus.cake.cache.CacheEntry#SIZE} when evicting. The rational for
 * this policy is that we should rather cache a lot of smaller entries than a few large items.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <T>
 *            the type of elements being cached
 */
public class ReplaceBiggestPolicy<T extends AttributeMap> extends AbstractHeapReplacementPolicy<T> {

    /** The name of the policy. */
    public static final String NAME = "ReplaceBiggest";

    /** Creates a new ReplaceBiggestPolicy. */
    public ReplaceBiggestPolicy(PolicyContext<T> context) {
        super(context);
        // This is used to make sure that users have registered the SIZE attribute
        // with CacheAttributeConfiguration#add(Attribute...)}
        context.dependHard(SIZE);
    }

    /** {@inheritDoc} */
    protected int compareEntry(T o1, T o2) {
        return SIZE.compare(o2, o1);
    }
}
