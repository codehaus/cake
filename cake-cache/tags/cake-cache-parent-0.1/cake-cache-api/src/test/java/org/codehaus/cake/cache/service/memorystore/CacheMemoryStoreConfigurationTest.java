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
package org.codehaus.cake.cache.service.memorystore;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.ops.Ops;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.Procedure;
import org.junit.Test;

public class CacheMemoryStoreConfigurationTest {

    @Test
    public void maximumVolume() {
        MemoryStoreConfiguration<Integer, String> c = new MemoryStoreConfiguration<Integer, String>();
        assertEquals(0, c.getMaximumVolume());
        assertSame(c, c.setMaximumVolume(1));
        assertEquals(1, c.getMaximumVolume());
    }

    @Test(expected = IllegalArgumentException.class)
    public void maximumVolumeIAE() {
        new MemoryStoreConfiguration<Integer, String>().setMaximumVolume(-1);
    }

    @Test
    public void maximumSize() {
        MemoryStoreConfiguration<Integer, String> c = new MemoryStoreConfiguration<Integer, String>();
        assertEquals(0, c.getMaximumSize());
        assertSame(c, c.setMaximumSize(1));
        assertEquals(1, c.getMaximumSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void maximumSizeIAE() {
        new MemoryStoreConfiguration<Integer, String>().setMaximumSize(-1);
    }

    @Test
    public void isDisabled() {
        MemoryStoreConfiguration<Integer, String> c = new MemoryStoreConfiguration<Integer, String>();
        assertFalse(c.isDisabled());
        assertSame(c, c.setDisabled(true));
        assertTrue(c.isDisabled());
    }

    @Test
    public void evictor() {
        MemoryStoreConfiguration<Integer, String> c = new MemoryStoreConfiguration<Integer, String>();
        assertNull(c.getEvictor());

        Ops.Procedure<MemoryStoreService<Integer, String>> p = dummy(Procedure.class);

        assertSame(c, c.setEvictor(p));
        assertSame(p, c.getEvictor());
    }

    @Test
    public void policy() {
        MemoryStoreConfiguration<Integer, String> c = new MemoryStoreConfiguration<Integer, String>();
        assertNull(c.getPolicy());

        ReplacementPolicy<Integer, String> p = dummy(ReplacementPolicy.class);

        assertSame(c, c.setPolicy(p));
        assertSame(p, c.getPolicy());
    }

    @Test
    public void filter() {
        MemoryStoreConfiguration<Integer, String> c = new MemoryStoreConfiguration<Integer, String>();
        assertNull(c.getIsCacheableFilter());
        Predicate<CacheEntry<Integer, String>> p = dummy(Predicate.class);

        assertSame(c, c.setIsCacheableFilter(p));
        assertSame(p, c.getIsCacheableFilter());

        Predicate<Object> p1 = dummy(Predicate.class);
        c.setIsCacheableFilter(p1);
        Predicate<CacheEntry> p2 = dummy(Predicate.class);
        c.setIsCacheableFilter(p2);

        Predicate<CacheEntry<Object, Object>> p3 = dummy(Predicate.class);
        // c.setNeedsReloadFilter(p3);

    }

}
