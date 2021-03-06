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
package org.codehaus.cake.cache.loading;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.junit.Test;

public class CacheLoadingConfigurationTest {

    @Test
    public void setLoader() {
        CacheLoadingConfiguration<Integer, String> c = new CacheLoadingConfiguration<Integer, String>();
        c.setLoader(new Loader());
    }
    static class Loader {
        @CacheLoader
        public void load(String foo) {
            
        }
        
    }
    @Test
    public void filter() {
        CacheLoadingConfiguration<Integer, String> c = new CacheLoadingConfiguration<Integer, String>();
        assertNull(c.getNeedsReloadCondition());
        Predicate<CacheEntry<Integer, String>> p = dummy(Predicate.class);

        assertSame(c, c.setNeedsReloadCondition(p));
        assertSame(p, c.getNeedsReloadCondition());

        Predicate<Object> p1 = dummy(Predicate.class);
        c.setNeedsReloadCondition(p1);
        Predicate<CacheEntry> p2 = dummy(Predicate.class);
        c.setNeedsReloadCondition(p2);

        Predicate<CacheEntry<Object, Object>> p3 = dummy(Predicate.class);
        // c.setNeedsReloadFilter(p3);

    }

    // @Test
    // public void loaderOp() {
    // CacheLoadingConfiguration<Number, Number> c = new CacheLoadingConfiguration<Number, Number>();
    // assertNull(c.getLoader());
    // Op<Number, Number> op = dummy(Op.class);
    //
    // assertSame(c, c.setLoader(op));
    // assertSame(op, c.getLoader());
    //
    // Op<Number, Integer> op1 = dummy(Op.class);
    // c.setLoader(op1);
    //
    // Op<Object, Number> op2 = dummy(Op.class);
    // c.setLoader(op2);
    // }
    //
    // @Test
    // public void loaderSimple() {
    // CacheLoadingConfiguration<Number, Number> c = new CacheLoadingConfiguration<Number, Number>();
    // assertNull(c.getLoader());
    // BlockingCacheLoader<Number, Number> scl = dummy(BlockingCacheLoader.class);
    //
    // assertSame(c, c.setLoader(scl));
    // assertSame(scl, c.getLoader());
    //
    // BlockingCacheLoader<Number, Integer> SimpleCacheLoader1 = dummy(BlockingCacheLoader.class);
    // c.setLoader(SimpleCacheLoader1);
    //
    // BlockingCacheLoader<Object, Number> SimpleCacheLoader2 = dummy(BlockingCacheLoader.class);
    // c.setLoader(SimpleCacheLoader2);
    // }
}
