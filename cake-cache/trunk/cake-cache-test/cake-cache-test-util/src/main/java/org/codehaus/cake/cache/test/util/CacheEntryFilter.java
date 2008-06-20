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

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Predicate;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheEntryFilter.java 504 2007-12-05 17:49:24Z kasper $
 */
public class CacheEntryFilter implements Predicate<CacheEntry<Integer, String>> {

    private volatile boolean accept;

    private volatile CacheEntry<Integer, String> lastEntry;

    public boolean op(CacheEntry<Integer, String> element) {
        lastEntry = element;
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public CacheEntry<Integer, String> getLastEntry() {
        return lastEntry;
    }

    public void assertLastEquals(Map.Entry<Integer, String> e) {
        assertEquals(e.getKey(), lastEntry.getKey());
        assertEquals(e.getValue(), lastEntry.getValue());
    }
}
