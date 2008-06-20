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

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class ReplaceBiggestPolicyTest {

    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void register() {
        ReplaceBiggestPolicy rbp = new ReplaceBiggestPolicy();
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).attachToPolicy(with(any(Attribute.class)));// heap index
                one(s).dependOnHard(SIZE);
            }
        });
        rbp.registerAttribute(s);
    }

    @Test
    public void compare() {
        ReplaceBiggestPolicy rbp = new ReplaceBiggestPolicy();

        final CacheEntry ce1 = context.mock(CacheEntry.class, "1");
        final AttributeMap am1 = new DefaultAttributeMap();

        final CacheEntry ce2 = context.mock(CacheEntry.class, "2");
        final AttributeMap am2 = new DefaultAttributeMap();

        final CacheEntry ce3 = context.mock(CacheEntry.class, "3");
        final AttributeMap am3 = new DefaultAttributeMap();

        context.checking(new Expectations() {
            {
                allowing(ce1).getAttributes();
                will(returnValue(am1));
                allowing(ce2).getAttributes();
                will(returnValue(am2));
                allowing(ce3).getAttributes();
                will(returnValue(am3));
            }
        });
        SIZE.set(ce1, 1);
        SIZE.set(ce2, 2);
        SIZE.set(ce3, 3);
        rbp.add(ce2);
        rbp.add(ce3);
        rbp.add(ce1);
        assertSame(ce3, rbp.evictNext());
        assertSame(ce2, rbp.evictNext());
        rbp.add(ce3);
        assertSame(ce3, rbp.evictNext());
        assertSame(ce1, rbp.evictNext());
        assertNull(rbp.evictNext());
    }
}
