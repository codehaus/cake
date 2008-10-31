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

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import javax.management.RuntimeMBeanException;

import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreMXBean;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.cache.test.tck.service.management.AbstractManagementTest;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.ops.LongOps;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Before;
import org.junit.Test;

@RequireService( { Manageable.class })
public class MemoryStoreManagement extends AbstractManagementTest {

    static MemoryStoreConfiguration<?, ?> DEFAULT = new MemoryStoreConfiguration();

    public AbstractCacheTCKTest assertSize(int size) {
        super.assertSize(size);
        assertEquals(size, mxBean().getSize());
        return this;
    }

    public AbstractCacheTCKTest assertVolume(long volume) {
        super.assertVolume(volume);
        assertEquals(volume, mxBean().getVolume());
        return this;
    }

    /**
     * Tests maximum size.
     */
    @Test
    public void maximumSize() {
        assertEquals(Integer.MAX_VALUE, mxBean().getMaximumSize());
        mxBean().setMaximumSize(1000);
        assertEquals(1000, mxBean().getMaximumSize());
        assertEquals(1000, withMemoryStore().getMaximumSize());

        // start value
        conf.withMemoryStore().setMaximumSize(5000);
        init();
        assertEquals(5000, mxBean().getMaximumSize());

        // Exception
        try {
            mxBean().setMaximumSize(-1);
            fail("Did not throw exception");
        } catch (IllegalArgumentException e) {/* ok */
        } catch (RuntimeMBeanException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests maximum capacity.
     */
    @Test
    public void maximumVolume() {
        assertVolume(0);
        assertEquals(Long.MAX_VALUE, mxBean().getMaximumVolume());
        mxBean().setMaximumVolume(1000);
        assertEquals(1000, mxBean().getMaximumVolume());
        assertEquals(1000, withMemoryStore().getMaximumVolume());

        // start value
        conf.withMemoryStore().setMaximumVolume(5000);
        init();
        // assertEquals(5000, withMemoryStore().getMaximumVolume());
        assertEquals(5000, mxBean().getMaximumVolume());

        // Exception
        try {
            mxBean().setMaximumVolume(-1);
            fail("Did not throw exception");
        } catch (IllegalArgumentException e) {} catch (RuntimeMBeanException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    MemoryStoreMXBean mxBean() {
        return getManagedInstance(MemoryStoreMXBean.class);
    }

    @Before
    public void setupTest() {
        conf.withAttributes().add(SIZE);
        init();
    }

    /**
     * Tests trimToSize.
     */
    @Test
    public void trimToSize() {
        put(5);
        assertSize(5);
        mxBean().trimToSize(3);
        assertSize(3);
        put(10, 15);
        assertSize(9);
        mxBean().trimToSize(1);
        assertSize(1);
        put(20, 25);
        assertSize(7);

        mxBean().trimToSize(-3);
        assertSize(4);
    }

    @Test
    public void trimToVolume() {
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.withMemoryStore().setPolicy(Policies.newLRU());
        init();
        c.get(1);
        assertVolume(2);
        c.get(2);
        assertVolume(5);
        c.get(4);
        assertVolume(10);
        c.get(3);
        c.get(5);
        assertVolume(20);

        mxBean().trimToVolume(21);
        assertVolume(20);

        mxBean().trimToVolume(20);
        assertVolume(20);

        mxBean().trimToVolume(19);
        assertFalse(c.containsKey(1));
        assertVolume(18);

        mxBean().trimToVolume(12);
        assertVolume(10);

        mxBean().trimToVolume(-3);
        assertVolume(6);

        mxBean().trimToVolume(Long.MIN_VALUE);
        assertVolume(0);

    }
}
