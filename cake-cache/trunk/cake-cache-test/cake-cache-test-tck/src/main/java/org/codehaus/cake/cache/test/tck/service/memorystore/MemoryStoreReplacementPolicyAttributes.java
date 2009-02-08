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
package org.codehaus.cake.cache.test.tck.service.memorystore;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.cache.policy.spi.PolicyContext;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class MemoryStoreReplacementPolicyAttributes extends AbstractCacheTCKTest {
    static final Attribute A1 = new LongAttribute() {};
    static final Attribute A2 = new LongAttribute() {};
    static final Attribute A3 = new LongAttribute() {};

    @Test(expected = IllegalStateException.class)
    public void dependHardISE() {
        conf.withMemoryStore().setPolicy(HardPolicy.class).setMaximumSize(5);
        init();
        prestart();
    }

    @Test
    public void dependHard() {
        conf.withMemoryStore().setPolicy(HardPolicy.class).setMaximumSize(5);
        conf.addEntryAttributes(A1);
        init();
        prestart();
        put(M1);
        assertTrue(getEntry(M1).attributes().contains(A1));
    }

    @Test
    public void dependSoft() {
        conf.withMemoryStore().setPolicy(SoftPolicy.class).setMaximumSize(5);
        init();
        prestart();
        put(M1);
        get(M1);
        get(M1);
        assertEquals(3, getEntry(M1).get(CacheEntry.HITS));
        assertFalse(getEntry(M1).attributes().contains(CacheEntry.HITS));
    }

    @Test
    public void dependSoftAlreadyRegistered() {
        conf.withMemoryStore().setPolicy(SoftPolicy.class).setMaximumSize(5);
        conf.addEntryAttributes(CacheEntry.HITS);
        init();
        prestart();
        put(M1);
        get(M1);
        get(M1);
        assertEquals(3, getEntry(M1).get(CacheEntry.HITS));
        assertTrue(getEntry(M1).attributes().contains(CacheEntry.HITS));
    }

    public static class HardPolicy implements ReplacementPolicy<CacheEntry<Integer, String>> {

        public HardPolicy(PolicyContext<?> context) {
            context.dependHard(A1);
        }

        public void add(CacheEntry<Integer, String> entry) {
        }

        public void clear() {
        }

        public CacheEntry<Integer, String> evictNext() {
            return null;
        }

        public void remove(CacheEntry<Integer, String> entry) {
        }

        public void replace(CacheEntry<Integer, String> oldElement, CacheEntry<Integer, String> newElement) {
        }

        public void touch(CacheEntry<Integer, String> element) {
        }

    }

    public static class SoftPolicy implements ReplacementPolicy<CacheEntry<Integer, String>> {

        public SoftPolicy(PolicyContext<?> context) {
            context.dependSoft(CacheEntry.HITS);
        }

        public void add(CacheEntry<Integer, String> entry) {
        }

        public void clear() {
        }

        public CacheEntry<Integer, String> evictNext() {
            return null;
        }

        public void remove(CacheEntry<Integer, String> entry) {
        }

        public void replace(CacheEntry<Integer, String> oldElement, CacheEntry<Integer, String> newElement) {
        }

        public void touch(CacheEntry<Integer, String> element) {
        }

    }

}
