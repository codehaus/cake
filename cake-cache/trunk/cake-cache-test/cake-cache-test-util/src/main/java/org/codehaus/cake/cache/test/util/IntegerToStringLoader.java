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

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.test.util.TestUtil;

/**
 * A simple cache loader used for testing. Will return 1->A, 2->B, 3->C, 4->D, 5->E and <code>null</code> for any
 * other key.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: IntegerToStringLoader.java 520 2007-12-21 17:53:31Z kasper $
 */
public class IntegerToStringLoader implements SimpleCacheLoader<Integer, String> {

    public static final Attribute RESULT_ATTRIBUTE_KEY = TestUtil.dummy(Attribute.class);

    private final AtomicLong totalLoads = new AtomicLong();

    private volatile Thread loaderThread;

    private volatile CountDownLatch latch;

    private volatile Integer latestKey;

    private volatile AttributeMap latestMap;

    private volatile int base;

    private volatile boolean doReturnNull;

    public void setDoReturnNull(boolean doReturnNull) {
        this.doReturnNull = doReturnNull;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public void incBase() {
        base++;
    }

    public long getNumberOfLoads() {
        return totalLoads.get();
    }

    public Thread getLoaderThread() {
        return loaderThread;
    }

    public CountDownLatch initializeLatch(int count) {
        latch = new CountDownLatch(count);
        return latch;
    }

    /** {@inheritDoc} */
    public String load(Integer key, AttributeMap ignore) throws Exception {
        latestKey = key;
        latestMap = ignore;
        loaderThread = Thread.currentThread();
        totalLoads.incrementAndGet();
        if (latch != null) {
            latch.countDown();
        }
        if (ignore != null && ignore.contains(RESULT_ATTRIBUTE_KEY)) {
            return (String) ignore.get(RESULT_ATTRIBUTE_KEY);
        }
        if (1 <= key && key <= 5 && !doReturnNull) {
            return "" + (char) (key + 64 + base);
        } else {
            return null;
        }
    }

    public Integer getLatestKey() {
        return latestKey;
    }

    public AttributeMap getLatestAttributeMap() {
        return latestMap;
    }
}
