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

import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LoadSimpleOp extends AbstractCacheTCKTest {

    @Before
    public void before() {
        newConfiguration();
        conf.withLoading().setLoader(new SimpleLoader1());
        newCache();
    }

    @Test
    public void get() {
        assertEquals("1", c.get(1));
        assertNull(c.get(-1));
    }

    @Test
    public void load() {
        withLoading().load(100);
        withLoading().load(200, CacheEntry.TIME_CREATED.singleton(10));
        awaitFinishedThreads();
        assertSize(2);
        assertEquals("100", c.peek(100));
        assertEquals("200", c.peek(200));
    }

    @Test
    public void load2() {
        withLoading().load(100);
        withLoading().load(200, CacheEntry.TIME_CREATED.singleton(10));
        awaitFinishedThreads();
        assertSize(2);
        assertEquals("100", c.peek(100));
        assertEquals("200", c.peek(200));
    }

    @Test
    public void attributes() {
        withLoading().load(200, CacheEntry.TIME_CREATED.singleton(10));
        awaitFinishedThreads();
        assertEquals(CacheEntry.TIME_CREATED.getDefaultValue(), c.getEntry(200).getAttributes().get(
                CacheEntry.TIME_CREATED));

        newConfiguration();
        conf.withAttributes().add(CacheEntry.TIME_CREATED);
        conf.withLoading().setLoader(new SimpleLoader1());
        newCache();
        withLoading().load(200, CacheEntry.TIME_CREATED.singleton(10));
        awaitFinishedThreads();
        assertSize(1);
        assertEquals("200", c.peek(200));
        assertEquals(10, c.getEntry(200).getAttributes().get(CacheEntry.TIME_CREATED));
    }

    @Test
    public void attributesCustom() {
        LongAttribute la = new LongAttribute() {};
        newConfiguration();
        conf.withAttributes().add(la);
        conf.withLoading().setLoader(new SimpleLoader1());
        newCache();

        withLoading().load(1);
        withLoading().load(2);
        withLoading().load(3);
        awaitFinishedThreads();
        assertTrue(c.getEntry(1).getAttributes().get(la) != 10);
        assertTrue(c.getEntry(2).getAttributes().get(la) != 10);
        assertTrue(c.getEntry(3).getAttributes().get(la) != 10);

        withLoadingForced().loadAll(la.singleton(10));
        awaitFinishedThreads();
        assertEquals(10, c.peekEntry(1).getAttributes().get(la));
        assertEquals(10, c.peekEntry(2).getAttributes().get(la));
        assertEquals(10, c.peekEntry(3).getAttributes().get(la));
    }

    @Test
    @Ignore
    public void attributesAll() {
        newConfiguration();
        conf.withAttributes().add(CacheEntry.TIME_CREATED);
        conf.withLoading().setLoader(new SimpleLoader1());
        newCache();

        withLoading().load(1);
        withLoading().load(2);
        withLoading().load(3);
        awaitFinishedThreads();
        assertTrue(c.getEntry(1).getAttributes().get(CacheEntry.TIME_CREATED) != 10);
        assertTrue(c.getEntry(2).getAttributes().get(CacheEntry.TIME_CREATED) != 10);
        assertTrue(c.getEntry(3).getAttributes().get(CacheEntry.TIME_CREATED) != 10);

        withLoadingForced().loadAll(CacheEntry.TIME_CREATED.singleton(10));
        awaitFinishedThreads();
        assertEquals(10, c.peekEntry(1).getAttributes().get(CacheEntry.TIME_CREATED));
        assertEquals(10, c.peekEntry(2).getAttributes().get(CacheEntry.TIME_CREATED));
        assertEquals(10, c.peekEntry(3).getAttributes().get(CacheEntry.TIME_CREATED));
    }

    static class SimpleLoader1 implements Op<Integer, String> {

        public String op(Integer a) {
            if (a == 0) {
                throw RuntimeException1.INSTANCE;
            } else if (a > 0) {
                return "" + a;
            } else {
                return null;
            }
        }
    }
}
