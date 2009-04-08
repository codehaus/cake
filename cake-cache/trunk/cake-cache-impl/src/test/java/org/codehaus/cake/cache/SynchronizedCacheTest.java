/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.test.tck.CacheTckRunner;
import org.codehaus.cake.service.test.tck.TckImplementationSpecifier;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CacheTckRunner.class)
@TckImplementationSpecifier(SynchronizedCache.class)
public class SynchronizedCacheTest {

    @Test
    public void prestart() {
        SynchronizedCache c = new SynchronizedCache();
        assertFalse(c.isStarted());
        c.prestart();
        assertTrue(c.isStarted());
    }
    
    @Test
    public void from() {
        CacheConfiguration<Integer, String> conf = CacheConfiguration.newConfiguration("foo");
        SynchronizedCache<Integer, String> c = SynchronizedCache.from(conf);
        assertEquals("foo", c.getName());
    }
}
