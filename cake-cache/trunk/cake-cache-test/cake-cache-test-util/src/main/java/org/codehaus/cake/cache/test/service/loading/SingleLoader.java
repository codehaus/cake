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
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class SingleLoader implements SimpleCacheLoader<Integer, String> {

    private AttributeMap attributes;

    private Throwable cause;

    private final AtomicLong count = new AtomicLong();

    private Integer key;

    private String value;

    public Throwable getCause() {
        return cause;
    }

    public long getCount() {
        return count.get();
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String load(Integer key, AttributeMap attributes) throws Exception {
        if (key.equals(this.key)) {
            count.incrementAndGet();
            if (cause instanceof Error) {
                throw (Error) cause;
            } else if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (this.attributes != null) {
                for (Map.Entry<Attribute, Object> me : this.attributes.entrySet()) {
                    attributes.put(me.getKey(), me.getValue());
                }
            }
            return value;
        }
        return null;
    }

    public static SingleLoader from(Integer key) {
        return from(key, "" + (char) (key + 64));
    }

    public static SingleLoader from(Integer key, String value) {
        SingleLoader sl = new SingleLoader();
        sl.key = key;
        sl.value = value;
        return sl;
    }

    public static SingleLoader from(Integer key, String value, AttributeMap attributes) {
        SingleLoader sl = from(key, value);
        sl.setAttributes(attributes);
        return sl;
    }

    public static SingleLoader from(Integer key, Error cause) {
        SingleLoader sl = new SingleLoader();
        sl.key = key;
        sl.cause = cause;
        return sl;
    }

    public static SingleLoader from(Integer key, Exception cause) {
        SingleLoader sl = new SingleLoader();
        sl.key = key;
        sl.cause = cause;
        return sl;
    }

    public void setAttributes(AttributeMap attributes) {
        this.attributes = attributes;
    }

    public <T> SingleLoader addAttribute(Attribute<T> attribute, T value) {
        if (attributes == null) {
            attributes = new DefaultAttributeMap();
        }
        attributes.put(attribute, value);
        // attribute.put(attributes, value);
        return this;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
