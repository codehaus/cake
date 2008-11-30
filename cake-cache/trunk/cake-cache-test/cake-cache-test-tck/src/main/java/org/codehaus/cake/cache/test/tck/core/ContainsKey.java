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
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#containsKey}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class ContainsKey extends AbstractCacheTCKTest {

    // TODO contains=lazyStart???
    /**
     * {@link Cache#containsKey} returns <code>true</code> for contained keys.
     */
    @Test
    public void containsKey() {
        init(5).assertContainsKey(M1).assertNotContainsKey(M6);
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsKeyLazyStart() {
        init();
        assertNotStarted();
        assertNotContainsKey(M1).assertStarted();
    }

    /**
     * <code>null</code> parameter to {@link Cache#containsKey} throws {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void containsKeyNPE() {
        init(5).containsKey(null);
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void containsKeyShutdown() throws InterruptedException {
        init(5).assertStarted();
        shutdown().containsKey(1);
        assertFalse(awaitTermination().containsKey(1));
    }
}
