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
import org.codehaus.cake.cache.policy.spi.AbstractReplacementPolicy;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class MemoryStoreReplacementPolicyAttributes extends AbstractCacheTCKTest {
    static final Attribute A1 = new LongAttribute() {};
    static final Attribute A2 = new LongAttribute() {};
    static final Attribute A3 = new LongAttribute() {};

    @Test(expected = IllegalStateException.class)
    public void dependHardISE() {
        conf.withMemoryStore().setPolicy(new HardPolicy()).setMaximumSize(5);
        init();
        prestart();
    }

    @Test
    public void dependHard() {
        conf.withMemoryStore().setPolicy(new HardPolicy()).setMaximumSize(5);
        conf.withAttributes().add(A1);
        init();
        prestart();
        put(M1);
        assertTrue(getEntry(M1).getAttributes().attributeSet().contains(A1));
    }

    @Test
    public void dependSoft() {
        conf.withMemoryStore().setPolicy(new SoftPolicy()).setMaximumSize(5);
        init();
        prestart();
        put(M1);
        get(M1);
        get(M1);
        assertEquals(3, getEntry(M1).getAttributes().get(CacheEntry.HITS));
        assertFalse(getEntry(M1).getAttributes().attributeSet().contains(CacheEntry.HITS));
    }

    @Test
    public void dependSoftAlreadyRegistered() {
        conf.withMemoryStore().setPolicy(new SoftPolicy()).setMaximumSize(5);
        conf.withAttributes().add(CacheEntry.HITS);
        init();
        prestart();
        put(M1);
        get(M1);
        get(M1);
        assertEquals(3, getEntry(M1).getAttributes().get(CacheEntry.HITS));
        assertTrue(getEntry(M1).getAttributes().attributeSet().contains(CacheEntry.HITS));
    }

    static class HardPolicy extends AbstractReplacementPolicy<Integer, String> {

        public HardPolicy() {
            super.dependHard(A1);
        }

        public boolean add(CacheEntry<Integer, String> entry) {
            return true;
        }

        public void clear() {}

        public CacheEntry<Integer, String> evictNext() {
            return null;
        }

        public void remove(CacheEntry<Integer, String> entry) {}

    }

    static class SoftPolicy extends AbstractReplacementPolicy<Integer, String> {

        public SoftPolicy() {
            super.dependSoft(CacheEntry.HITS);
        }

        public boolean add(CacheEntry<Integer, String> entry) {
            return true;
        }

        public void clear() {}

        public CacheEntry<Integer, String> evictNext() {
            return null;
        }

        public void remove(CacheEntry<Integer, String> entry) {}

    }

}
