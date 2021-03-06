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
package org.codehaus.cake.cache.test.tck.core.entryset;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#entrySet()} lazy start and shutdown.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySet.java 526 2007-12-27 01:32:16Z kasper $
 */
public class EntrySet extends AbstractCacheTCKTest {

    /**
     * Calls to {@link Cache#entrySet} should not start the cache.
     */
    @Test
    public void noLazyStart() {
        init();
        c.entrySet();
        assertNotStarted();
    }

    /**
     * Calls to {@link Cache#entrySet} should not fail when the cache is shutdown.
     */
    @Test
    public void noFailOnShutdown() {
        init(5).assertStarted();
        shutdown();
        c.entrySet();
        awaitTermination();
        c.entrySet();
    }
}
