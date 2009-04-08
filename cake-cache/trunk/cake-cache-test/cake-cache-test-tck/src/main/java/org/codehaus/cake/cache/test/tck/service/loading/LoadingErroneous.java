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
package org.codehaus.cake.cache.test.tck.service.loading;

import java.util.Arrays;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Test;

public class LoadingErroneous extends AbstractCacheTCKTest {

    @Test
    public void loadFailed() {
        loader.withLoader(M1).setCause(RuntimeException1.INSTANCE);
        withLoading().load(1);
        awaitFinishedThreads();
        assertSize(0);
        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
    }

    @Test
    public void loadAllFailed() {
        loader.withLoader(M2).setCause(RuntimeException1.INSTANCE);
        withLoading().loadAll(Arrays.asList(1, 2, 3));
        awaitFinishedThreads();
        assertSize(2);
        assertTrue(c.containsKey(1) && c.containsKey(3));
        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
    }

}
