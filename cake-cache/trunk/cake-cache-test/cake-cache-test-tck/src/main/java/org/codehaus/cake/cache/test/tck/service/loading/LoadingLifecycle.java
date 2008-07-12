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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.AfterStart;
import org.junit.Test;

public class LoadingLifecycle extends AbstractCacheTCKTest {

    @Test
    public void startedLoader() {
        TestLoader loader = new TestLoader();
        conf.withLoading().setLoader(loader);
        init().prestart();
        assertEquals(1, loader.count);
    }

    @Test
    public void startedPredicate() {
        TestPredicate loader = new TestPredicate();
        conf.withLoading().setNeedsReloadFilter(loader);
        init().prestart();
        assertEquals(1, loader.count);
    }

    public static class TestLoader implements BlockingCacheLoader {

        volatile int count;

        public Object load(Object key, AttributeMap attributes) throws Exception {
            return "foo";
        }

        @AfterStart
        public void started() {
            count++;
        }
    }

    public class TestPredicate implements Predicate {

        volatile int count;

        public boolean op(Object a) {
            return true;
        }

        @AfterStart
        public void started(Cache cache) {
            count++;
            assertSame(c, cache);
        }
    }
}
