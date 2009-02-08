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
package org.codehaus.cake.cache.policy;

import static org.junit.Assert.assertSame;

import org.codehaus.cake.cache.policy.paging.FIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LFUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.MFUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.MRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.RandomReplacementPolicy;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class PoliciesTest {

    @Test
    public void assertInstances() {
        assertSame(Policies.FIFO, FIFOReplacementPolicy.class);
        assertSame(Policies.LFU, LFUReplacementPolicy.class);
        assertSame(Policies.LIFO, LIFOReplacementPolicy.class);
        assertSame(Policies.LRU, LRUReplacementPolicy.class);
        assertSame(Policies.MFU, MFUReplacementPolicy.class);
        assertSame(Policies.MRU, MRUReplacementPolicy.class);
        assertSame(Policies.RANDOM, RandomReplacementPolicy.class);
    }
}