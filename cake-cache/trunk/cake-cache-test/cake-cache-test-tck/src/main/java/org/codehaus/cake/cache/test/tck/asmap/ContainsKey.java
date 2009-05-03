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

import java.util.concurrent.ConcurrentMap;

import org.junit.Test;

/**
 * Tests {@link ConcurrentMap#containsKey}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ContainsKey.java 327 2009-04-08 09:34:27Z kasper $
 */
public class ContainsKey extends AbstractAsMapTCKTest {

    /** {@link ConcurrentMap#containsKey} returns <code>true</code> for contained keys. */
    @Test
    public void containsKey() {
        init(5);
        assertContainsKey(M1);
        assertNotContainsKey(M6);
    }

    /** {@link ConcurrentMap#containsKey} lazy starts the cache. */
    @Test
    public void containsKeyLazyStart() {
        init();
        assertNotStarted();
        assertNotContainsKey(M1);
        assertStarted();
    }

    /** <code>null</code> parameter to {@link ConcurrentMap#containsKey} throws {@link NullPointerException}. */
    @Test(expected = NullPointerException.class)
    public void containsKeyNPE() {
        init(5);
        containsKey(null);
    }

    /** {@link ConcurrentMap#containsKey()} */
    @Test
    public void containsKeyShutdown() {
        init(5);
        assertStarted();
        shutdown();
        containsKey(1);// result undefined, but shouldnt fail
        awaitTermination();
        assertFalse(containsKey(1));
    }
}
