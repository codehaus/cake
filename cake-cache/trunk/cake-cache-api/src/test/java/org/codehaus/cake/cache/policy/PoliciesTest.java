/* Copyright 2004 - 2007 Kasper Nielsen <kasper@codehaus.org> Licensed under 
 * the Apache 2.0 License, see http://coconut.codehaus.org/license.
 */
package org.codehaus.cake.cache.policy;

import static org.junit.Assert.assertTrue;

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
    }
}
