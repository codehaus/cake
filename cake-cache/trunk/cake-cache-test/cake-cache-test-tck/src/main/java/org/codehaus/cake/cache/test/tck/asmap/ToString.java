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
package org.codehaus.cake.cache.test.tck.asmap;

import org.codehaus.cake.cache.Cache;
import org.junit.Test;

public class ToString extends AbstractAsMapTCKTest {

    /**
     * Just test that the toString() method works on an empty cache.
     */
    @Test
    public void toStringEmpty() {
        init();
        assertEquals("{}", asMap().toString());
    }

    /**
     * Just test that the toString() method works on an empty started cache.
     */
    @Test
    public void toStringStartedEmpty() {
        init();
        asMap().size();// lazy start
        assertEquals("{}", asMap().toString());
    }

    /**
     * Just test that the toString() method works.
     */
    @Test
    public void toStringX() {
        init(5);
        String s = asMap().toString();
        try {
            for (int i = 1; i < 6; i++) {
                assertTrue(s.indexOf(String.valueOf(i)) >= 0);
                assertTrue(s.indexOf("" + (char) (i + 64)) >= 0);
            }
        } catch (AssertionError ar) {
            System.out.println(s);
            throw ar;
        }
    }

    /**
     * Just test that the toString() does recursively call {@link Cache#toString()} if the cache is put into itself as a
     * value or a key.
     */
    @Test
    public void toStringCacheInCache() {
        Cache c = newCache();
        c.put(c.asMap(), "foo");
        asMap().toString();
        c.toString();
        c.put("foo", c.asMap());
        asMap().toString();
        c.toString();
    }
}
