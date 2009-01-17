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
package org.codehaus.cake.cache.test.service.loading;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;
import org.codehaus.cake.ops.Ops.LongOp;
import org.codehaus.cake.ops.Ops.Op;

public class TestLoader implements BlockingCacheLoader<Integer, String> {

    private final ConcurrentHashMap<Integer, SingleLoader> map = new ConcurrentHashMap<Integer, SingleLoader>();

    private volatile Integer latestKey;
    private volatile MutableAttributeMap latestAttributeMap;

    public String load(Integer key, MutableAttributeMap attributes) throws Exception {
        latestKey = key;
        latestAttributeMap = attributes;
        // System.out.println("load " + key);
        SingleLoader sl = map.get(key);
        if (sl != null) {
            return sl.load(key, attributes);
        }
        return null;
    }

    public Integer getLatestKey() {
        return latestKey;
    }

    public MutableAttributeMap getLatestAttributeMap() {
        return latestAttributeMap;
    }

    public SingleLoader get(Integer key) {
        return map.get(key);
    }

    public long totalLoads() {
        long count = 0;
        for (SingleLoader sl : map.values()) {
            count += sl.getCount();
        }
        return count;
    }

    public static TestLoader create(int entries) {
        TestLoader tl = new TestLoader();
        for (int i = 1; i <= entries; i++) {
            tl.map.put(i, SingleLoader.from(i, "" + (char) (i + 64)));
        }
        return tl;
    }

    public <T> void setAttribute(Attribute<T> atr, Op<Integer, T> op) {
        for (Map.Entry<Integer, SingleLoader> e : map.entrySet()) {
            e.getValue().addAttribute(atr, op.op(e.getKey()));
        }
    }

    public <T> void setAttribute(LongAttribute atr, LongOp op) {
        for (Map.Entry<Integer, SingleLoader> e : map.entrySet()) {
            e.getValue().addAttribute(atr, op.op(e.getKey()));
        }
    }

    public void clearAndFromBase(int entries, int base) {
        map.clear();
        for (int i = 1; i <= entries; i++) {
            map.put(i, SingleLoader.from(i, "" + (char) (i + base + 64)));
        }
    }

    public static TestLoader createFromBase(int entries, int base) {
        TestLoader tl = new TestLoader();
        for (int i = 1; i <= entries; i++) {
            tl.map.put(i, SingleLoader.from(i, "" + (char) (i + base + 64)));
        }
        return tl;
    }

    public void clear() {
        map.clear();
    }

    public TestLoader add(Map.Entry<Integer, String>... entries) {
        for (Map.Entry<Integer, String> entry : entries) {
            map.put(entry.getKey(), SingleLoader.from(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public TestLoader add(Map.Entry<Integer, String> entry, String value) {
        map.put(entry.getKey(), SingleLoader.from(entry.getKey(), value));
        return this;
    }

    public TestLoader add(Integer key, String value) {
        map.put(key, SingleLoader.from(key, value));
        return this;
    }

    public TestLoader add(Integer key, String value, MutableAttributeMap attributes) {
        map.put(key, SingleLoader.from(key, value, attributes));
        return this;
    }

    public <T> TestLoader add(Map.Entry<Integer, String> entry, Attribute<T> attribute, T avalue) {
        return add(entry.getKey(), entry.getValue(), attribute, avalue);
    }

    public <T, S> TestLoader add(Map.Entry<Integer, String> entry, Attribute<T> attribute, T avalue,
            Attribute<S> attributes, S svalue) {
        MutableAttributeMap ma = new DefaultAttributeMap();
        ma.put(attribute, avalue);
        ma.put(attributes, svalue);
        return add(entry.getKey(), entry.getValue(), ma);
    }

    public <T> TestLoader add(Integer key, String value, Attribute<T> attribute, T avalue) {
        map.put(key, SingleLoader.from(key, value, new DefaultAttributeMap(Attributes.singleton(attribute, avalue))));
        return this;
    }

    public SingleLoader withLoader(Map.Entry<Integer, String> entry) {
        return map.get(entry.getKey());
    }
}
