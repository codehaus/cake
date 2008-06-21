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
package org.codehaus.cake.internal.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.test.tck.CacheTckRunner;
import org.codehaus.cake.service.test.tck.TckImplementationSpecifier;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CacheTckRunner.class)
@TckImplementationSpecifier(SynchronizedInternalCache.class)
public class SynchronizedInternalCacheTest {

    @Test
    public void prestart() {
        SynchronizedInternalCache c = new SynchronizedInternalCache();
        assertFalse(c.isStarted());
        c.prestart();
        assertTrue(c.isStarted());
    }

}
