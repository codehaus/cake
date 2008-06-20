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
package org.codehaus.cake.cache.test.tck.attributes.cache;

import static org.codehaus.cake.cache.CacheEntry.HITS;

import org.codehaus.cake.cache.CacheEntry;

/**
 * Tests the size attribute of {@link CacheEntry}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Size.java 555 2008-01-09 04:52:48Z kasper $
 */
public class Hits extends AbstractAttributeTest {
    public Hits() {
        super(HITS);
    }

    // /**
    // * Tests default size of 1.
    // */
    // @SuppressWarnings("unchecked")
    // @Test
    // public void put() {
    // put(M1);
    // assertAttribute(M1);
    // putAll(M1, M2);
    // assertAttribute(M1);
    // assertAttribute(M2);
    // }
    //
    // /**
    // * Tests that put overrides the cost of an existing item.
    // */
    // @Test
    // public void putOverride() {
    // loader.withLoader(M1).addAttribute(atr, 4l);
    // assertGet(M1);
    // assertAttribute(M1, 4l);
    // put(M1);
    // assertAttribute(M1);
    // }
}
