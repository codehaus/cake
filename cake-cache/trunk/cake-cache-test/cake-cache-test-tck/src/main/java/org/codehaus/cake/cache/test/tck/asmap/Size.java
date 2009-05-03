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

public class Size extends AbstractAsMapTCKTest {

    /** size returns the correct values. */
    @Test
    public void sizeBasic() {
        init(0);
        assertSize(0);
        init(5);
        assertSize(5);
    }

    /** {@link Cache#size()} lazy starts the cache. */
    @Test
    public void sizeLazyStart() {
        init();
        asMap();// shoudnt start it
        assertNotStarted();
        asMap().size();
        assertStarted();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void sizeShutdown() {
        init(5);
        assertStarted();
        shutdown();
        asMap().size();// shouldnt fail
        awaitTermination();
        assertSize(0);
    }

}
