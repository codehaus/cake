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

import static org.codehaus.cake.internal.cache.CacheEntryAttributes.COST;

import org.codehaus.cake.cache.policy.AbstractHeapReplacementPolicy;
import org.codehaus.cake.cache.policy.PolicyContext;
import org.codehaus.cake.util.attribute.AttributeMap;

/**
 * A replacement policy that replaces the entry with the smallest {@link org.codehaus.cake.cache.CacheEntry#COST} when
 * evicting. The rational for this policy is that we should attempt to keep entries that have the highest cost to
 * retrieve again.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <T>
 *            the type of elements being cached
 */
public class ReplaceCostliestPolicy<T extends AttributeMap> extends AbstractHeapReplacementPolicy<T> {

    /** The name of the policy. */
    public static final String NAME = "ReplaceCostliest";

    /** Creates a new ReplaceCostliestPolicy. */
    public ReplaceCostliestPolicy(PolicyContext<T> context) {
        super(context);
        // This is used to make sure that users have registered the COST attribute
        // with CacheAttributeConfiguration#add(Attribute...)}
        // it would not make sense using this policy if entries didn't have a COST attached
        context.dependHard(COST);
    }

    /** {@inheritDoc} */
    protected int compareEntry(T o1, T o2) {
        return COST.compare(o2, o1);
    }
}
