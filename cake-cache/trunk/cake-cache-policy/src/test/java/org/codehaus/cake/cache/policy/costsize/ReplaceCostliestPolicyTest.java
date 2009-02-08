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

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.internal.cache.CacheEntryAttributes.COST;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.policy.AbstractPolicyTest;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.cache.policy.spi.PolicyContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class ReplaceCostliestPolicyTest extends AbstractPolicyTest{

    protected ReplacementPolicy<AttributeMap> policy;

    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void register() {
        final PolicyContext<AttributeMap> pc = context.mock(PolicyContext.class);
        context.checking(new Expectations() {
            {
                one(pc).newArray(0);
                one(pc).attachInt();
                one(pc).dependHard(COST);
            }
        });
        ReplaceCostliestPolicy rbp = new ReplaceCostliestPolicy(pc);

    }

    @Test
    public void compare() {
        policy = Policies.create(ReplaceCostliestPolicy.class);
        final AttributeMap ce1 = createEntry(COST, 1.5);
        final AttributeMap ce2 = createEntry(COST, 2.5);
        final AttributeMap ce3 = createEntry(COST, 3.5);

        policy.add(ce1);
        policy.add(ce3);
        policy.add(ce2);
        assertSame(ce3, policy.evictNext());
        assertSame(ce2, policy.evictNext());
        policy.add(ce3);
        assertSame(ce3, policy.evictNext());
        assertSame(ce1, policy.evictNext());
        assertNull(policy.evictNext());
    }
}
