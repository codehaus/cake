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

import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.policy.costsize.ReplaceBiggestPolicy;
import org.codehaus.cake.cache.policy.costsize.ReplaceCostliestPolicy;
import org.codehaus.cake.cache.policy.paging.FIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.MRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.RandomReplacementPolicy;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PoliciesTest.java 491 2007-11-30 22:05:50Z kasper $
 */
public class PoliciesTest {

    @Test
    public void assertInstances() {
        assertTrue(Policies.newFIFO() instanceof FIFOReplacementPolicy);
        assertTrue(Policies.newLIFO() instanceof LIFOReplacementPolicy);
        assertTrue(Policies.newLRU() instanceof LRUReplacementPolicy);
        assertTrue(Policies.newMRU() instanceof MRUReplacementPolicy);
        assertTrue(Policies.newRandom() instanceof RandomReplacementPolicy);
        assertTrue(Policies.newReplaceBiggest() instanceof ReplaceBiggestPolicy);
        assertTrue(Policies.newReplaceCostliest() instanceof ReplaceCostliestPolicy);
    }
}
