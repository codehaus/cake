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
package org.codehaus.cake.cache.test.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class CountdownLatchLoader implements SimpleCacheLoader<Integer, String> {

    private final AtomicLong loads = new AtomicLong();

    private final AtomicLong loadAlls = new AtomicLong();

    private final CountDownLatch latch;

    private final CountDownLatch beforeLoad;

    private final SimpleCacheLoader<Integer, String> loader;

    public CountdownLatchLoader(SimpleCacheLoader<Integer, String> loader, int counts) {
        this(loader, counts, 0);
    }

    public CountdownLatchLoader(SimpleCacheLoader<Integer, String> loader, int counts, int beforeLoads) {
        this.latch = new CountDownLatch(counts);
        this.loader = loader;
        this.beforeLoad = new CountDownLatch(beforeLoads);
    }

    public String load(Integer key, AttributeMap map) throws Exception {
        beforeLoad.countDown();
        latch.await();
        loads.incrementAndGet();
        return loader.load(key, map);
    }

    // public Map<Integer, String> loadAll(Map<? extends Integer, AttributeMap> mapsWithAttributes)
    // throws Exception {
    // beforeLoad.countDown();
    // latch.await();
    // loadAlls.incrementAndGet();
    // return loader.loadAll(mapsWithAttributes);
    // }

    public void countDown() {
        latch.countDown();
    }

    public CountDownLatch beforeLoad() {
        return beforeLoad;
    }

    public static CountdownLatchLoader integerToStringLoader(int counts) {
        return new CountdownLatchLoader(new IntegerToStringLoader(), counts);
    }

    public long getNumberOfLoads() {
        return loads.get();
    }

    public long getNumberOfLoadAlls() {
        return loadAlls.get();
    }

}
