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

import java.util.concurrent.atomic.AtomicReference;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;

/**
 * A simple cache loader used for testing. Will return 1->A, 2->B, 3->C, 4->D, 5->E and <code>null</code> for any
 * other key.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: MutableLoader.java 544 2008-01-05 01:19:03Z kasper $
 */
public class MutableLoader implements BlockingCacheLoader<Integer, String> {

    private final AtomicReference<String> ref = new AtomicReference<String>();

    private volatile Integer lastKey;

    private volatile AttributeMap lastAttributeMap;

    public String load(Integer key, AttributeMap attributes) {
        this.lastKey = key;
        this.lastAttributeMap = attributes;
        return ref.get();
    }

    public void setLoadNext(String loadNext) {
        ref.set(loadNext);
    }

    public Integer getLastKey() {
        return lastKey;
    }

    public AttributeMap getLastAttributeMap() {
        return lastAttributeMap;
    }
}
