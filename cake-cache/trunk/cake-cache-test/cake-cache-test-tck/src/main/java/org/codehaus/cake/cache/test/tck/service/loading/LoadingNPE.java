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
package org.codehaus.cake.cache.test.tck.service.loading;

import java.util.Map;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadingNPE extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void withKeysNPE3() {
        withLoading().loadAll((Map) null);
    }
    //
    // @Test(expected = NullPointerException.class)
    // public void withKeysNPE4() {
    // withLoading().withKeys(
    // asMap(1, asDummy(AttributeMap.class), null, asDummy(AttributeMap.class)));
    // }
    //
    // @Test(expected = NullPointerException.class)
    // public void withKeysNPE5() {
    // withLoading().withKeys(asMap(1, asDummy(AttributeMap.class), 1, null));
    // }
}
