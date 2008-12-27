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
import static org.codehaus.cake.cache.CacheEntry.SIZE;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.AbstractPolicyTest;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class ReplaceBiggestPolicyTest extends AbstractPolicyTest {

    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void register() {
        ReplaceBiggestPolicy rbp = new ReplaceBiggestPolicy();
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).newInt();
                one(s).dependOnHard(SIZE);
            }
        });
        rbp.registerAttribute(s);
    }

    @Test
    public void compare() {
        policy = new ReplaceBiggestPolicy();
        init();
        final CacheEntry ce1 = createEntry(SIZE, 1L);
        final CacheEntry ce2 = createEntry(SIZE, 2L);
        final CacheEntry ce3 = createEntry(SIZE, 3L);

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
