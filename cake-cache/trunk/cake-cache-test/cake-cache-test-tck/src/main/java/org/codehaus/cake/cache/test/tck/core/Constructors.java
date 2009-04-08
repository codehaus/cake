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
package org.codehaus.cake.cache.test.tck.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * This test tests that the two required constructors are present, a no argument constructor and one taking a
 * CacheConfiguration.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class Constructors extends AbstractCacheTCKTest {

    @SuppressWarnings("unchecked")
    private Class<Cache> getClazz() {
        return (Class<Cache>) newCache().getClass();
    }

    /**
     * Tests that the cache has a no argument constructor.
     * 
     * @throws Exception
     *             the test failed for some unknown reason
     */
    @Test
    public void testNoArgumentConstructor() throws Exception {
        Constructor con = getClazz().getConstructor((Class[]) null);
        Cache c = (Cache) con.newInstance((Object[]) null);
        assertNotNull(c);
        assertTrue(c.isEmpty());
    }

    /**
     * Tests that the cache implementation has a single constructor taking a CacheConfiguration.
     * 
     * @throws Throwable
     */
    @Test(expected = NullPointerException.class)
    public void testNullCacheConfigurationArgumentConstructor() throws Throwable {
        Constructor con = getClazz().getConstructor(CacheConfiguration.class);
        try {
            con.newInstance(new Object[] { null });
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }
}
