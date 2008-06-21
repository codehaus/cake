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
package org.codehaus.cake.cache.test.tck.service.management;

import org.codehaus.cake.cache.CacheMXBean;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService( { Manageable.class })
public class ManagementCacheMXBean extends AbstractManagementTest {

    CacheMXBean cache() {
        return getManagedInstance(CacheMXBean.class);
    }

    @Test
    public void getName() {
        conf.setName("foo");
        init();
        assertEquals("foo", cache().getName());
        assertEquals(c.getName(), cache().getName());
    }

    @Test
    public void getNameUninitialized() {
        assertEquals(c.getName(), cache().getName());
    }

    @Test
    public void getSize() {
        assertEquals(c.size(), cache().getSize());
        put(M1);
        put(M2);
        assertEquals(2, cache().getSize());
        assertEquals(c.size(), cache().getSize());
    }

    @Test
    public void clearCache() {
        assertSize(0);
        put(M1);
        put(M2);
        assertSize(2);
        cache().clear();
        assertSize(0);
    }

    @Test
    // TODO (expected = IllegalArgumentException.class)
    public void onShutdown() {

        put(M1, M2);
        assertEquals(2, cache().getSize());
        shutdownAndAwaitTermination(); // should be removed from server
        cache().getSize();
    }
}
