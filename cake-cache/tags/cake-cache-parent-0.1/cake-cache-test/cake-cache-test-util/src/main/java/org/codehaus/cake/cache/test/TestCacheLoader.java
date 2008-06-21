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
package org.codehaus.cake.cache.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class TestCacheLoader implements SimpleCacheLoader<Integer, String> {

    List<RequestInfo> list = new CopyOnWriteArrayList<RequestInfo>();

    private final List<Attribute> l = new ArrayList<Attribute>();

    public TestCacheLoader(Attribute... attributes) {
        if (attributes.length > 0) {
            for (Attribute a : attributes) {
                if (a != CacheEntry.SIZE) {
                    throw new IllegalArgumentException("Unknown attribute " + a);
                }
                l.add(a);
            }
        }
    }

    public int getTotalLoads() {
        return list.size();
    }

    public String load(Integer key, AttributeMap attributes) throws Exception {
        String result = doLoad(key, attributes);
        list.add(new RequestInfo(key, attributes, result));
        if (l.contains(CacheEntry.SIZE)) {
            CacheEntry.SIZE.set(attributes, key.longValue());
        }
        return result;
    }

    protected String doLoad(Integer key, AttributeMap attributes) {
        if (1 <= key && key <= 5) {
            return "" + (char) (key + 64);
        } else {
            return null;
        }
    }

    public static class RequestInfo {
        private final Thread loadingThread;

        private final Integer key;

        private final AttributeMap attributes;

        private final String result;

        RequestInfo(Integer key, AttributeMap attributes, String result) {
            loadingThread = Thread.currentThread();
            this.key = key;
            this.attributes = attributes;
            this.result = result;
        }
    }
}
