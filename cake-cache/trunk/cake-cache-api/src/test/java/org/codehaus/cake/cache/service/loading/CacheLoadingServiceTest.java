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
package org.codehaus.cake.cache.service.loading;

import static org.codehaus.cake.cache.service.loading.CacheLoadingService.IS_FORCED;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.junit.Test;

public class CacheLoadingServiceTest {
    /**
     * Tests {@link CacheLoadingService#IS_FORCED.
     */
    @Test
    public void attributeCreationTime() {
        assertTrue(IS_FORCED instanceof BooleanAttribute);

        TestUtil.assertSingletonSerializable(IS_FORCED);
    }

}
