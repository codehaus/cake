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
package org.codehaus.cake.cache.policy.spi;

import static org.codehaus.cake.test.util.TestUtil.dummy;
import static org.junit.Assert.assertSame;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class AbstractReplacementPolicyTest {
    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    Attribute A1 = new LongAttribute() {};
    Attribute A2 = new LongAttribute() {};
    Attribute A3 = new LongAttribute() {};
    Attribute A4 = new LongAttribute() {};
    Attribute A5 = new LongAttribute() {};
    Attribute A6 = new LongAttribute() {};
    DummyPolicy policy;

    @Before
    public void setup() {
        this.policy = new DummyPolicy();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE1() {
        policy.attachToEntry(A1);
        policy.attachToEntry(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE2() {
        policy.dependHard(A1);
        policy.attachToEntry(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE3() {
        policy.dependSoft(A1);
        policy.attachToEntry(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE4() {
        policy.attachToEntry(A1);
        policy.dependHard(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE5() {
        policy.dependHard(A1);
        policy.dependHard(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE6() {
        policy.dependSoft(A1);
        policy.dependHard(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE7() {
        policy.attachToEntry(A1);
        policy.dependSoft(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE8() {
        policy.dependHard(A1);
        policy.dependSoft(A1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerIAE9() {
        policy.dependSoft(A1);
        policy.dependSoft(A1);
    }

    @Test
    public void testDefault() {
        CacheEntry ce1 = dummy(CacheEntry.class);
        CacheEntry ce2 = dummy(CacheEntry.class);
        assertSame(ce2, policy.replace(ce1, ce2));
        policy.touch(ce2);// does nothing
    }

    @Test
    public void registration() {
        policy.attachToEntry(A1);
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).attachToPolicy(A1);
            }
        });
        policy.registerAttribute(s);
    }

    @Test
    public void registrationHard() {
        policy.dependHard(A1);
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).dependOnHard(A1);
            }
        });
        policy.registerAttribute(s);
    }

    @Test
    public void registrationSoft() {
        policy.dependSoft(A1);
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).dependOnSoft(A1);
            }
        });
        policy.registerAttribute(s);
    }

    @Test(expected = IllegalStateException.class)
    public void registrationTwiceISE() {
        policy.dependHard(A1);
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).dependOnHard(A1);
            }
        });
        policy.registerAttribute(s);
        policy.registerAttribute(s);
    }

    static class DummyPolicy extends AbstractReplacementPolicy {

        public boolean add(CacheEntry entry) {
            // TODO Auto-generated method stub
            return false;
        }

        public void clear() {
        // TODO Auto-generated method stub

        }

        public CacheEntry evictNext() {
            // TODO Auto-generated method stub
            return null;
        }

        public void remove(CacheEntry entry) {
        // TODO Auto-generated method stub

        }

    }
}
