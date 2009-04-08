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

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Not sure these methods are to usefull though.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EqualsHashcode extends AbstractCacheTCKTest {

    @Test
    public void equals() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        assertTrue(c4.equals(c4));
        assertTrue(c3.equals(c3));
        assertFalse(c3.equals(c4));
        assertFalse(c4.equals(c3));
    }

    @Test
    public void hashcode() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        c4.hashCode();
        c3.hashCode();
    }

    @Test
    public void hashcodeShutdown() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        c3.shutdown();
        c4.shutdown();
        c4.hashCode();
        c3.hashCode();
    }

    @Test
    public void equalsShutdown() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        c3.shutdown();
        c4.shutdown();
        assertTrue(c4.equals(c4));
        assertTrue(c3.equals(c3));
        // assertTrue(c3.equals(c4));
        // assertTrue(c4.equals(c3));
    }
}
