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
package org.codehaus.cake.internal.cache.service.management;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.junit.Test;

public class DefaultCacheLoadingMXBeanTest {

    @Test(expected = NullPointerException.class)
    public void constructor_NPE() {
        new DefaultCacheLoadingMXBean(null, dummy(CacheLoadingService.class));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_NPE1() {
        new DefaultCacheLoadingMXBean(dummy(CacheLoadingService.class), null);
    }
}
