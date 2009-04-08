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
package org.codehaus.cake.cache.policy.costsize;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.internal.cache.CacheEntryAttributes.SIZE;

import org.codehaus.cake.cache.policy.AbstractPolicyTest;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.PolicyContext;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class ReplaceBiggestPolicyTest extends AbstractPolicyTest {

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
                one(pc).dependHard(SIZE);
            }
        });
        ReplaceBiggestPolicy rbp = new ReplaceBiggestPolicy(pc);
    }

    @Test
    public void compare() {
        policy = Policies.create(ReplaceBiggestPolicy.class);
        AttributeMap ce1 = createEntry(SIZE, 1L);
        AttributeMap ce2 = createEntry(SIZE, 2L);
        AttributeMap ce3 = createEntry(SIZE, 3L);

        policy.add(ce2);
        policy.add(ce3);
        policy.add(ce1);
        assertSame(ce3, policy.evictNext());
        assertSame(ce2, policy.evictNext());
        policy.add(ce3);
        assertSame(ce3, policy.evictNext());
        assertSame(ce1, policy.evictNext());
        assertNull(policy.evictNext());
    }
   
}
