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

import static org.codehaus.cake.attribute.Attributes.from;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Load extends AbstractCacheTCKTest {

    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test(expected = NullPointerException.class)
    public void withKeyNPE() {
        withLoading().load(null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE1() {
        withLoading().load(null, asDummy(MutableAttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE2() {
        withLoading().load(1, null);
    }

    @Test
    public void load() {
        assertPeek(entry(M1, null));
        load(M1);
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void loadAttributes() {
        assertPeek(entry(M1, null));
        load(M1, from(ATR1, "A", ATR2, "B"));
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void loadAttributesIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoading().load(M3.getKey(), from(ATR1, "A", ATR2, "B"));
        awaitTermination();
        withLoading().load(M4.getKey(), from(ATR1, "A", ATR2, "B"));
        awaitFinishedThreads();
        assertSize(0);
    }

    @Test
    public void loadAttributesTwice() {
        load(M1, from(ATR1, "A", ATR2, "B"));
        load(entry(M1, null), from(ATR1, "A", ATR2, "B"));// already here
        assertLoads(1);
    }

    @Test
    public void loadIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoading().load(M1.getKey());
        awaitTermination();
        withLoading().load(M2.getKey());
        awaitFinishedThreads();
        assertSize(0);
    }

    @Test
    public void loadMany() {
        load(M1);
        load(M2);
        load(M3);
        load(M4);
        load(M5);
        load(entry(M6, null));// not in loader
        assertLoads(5);
    }

    @Test
    public void loadTwice() {
        load(M1);
        load(entry(M1, null));// already here
        assertLoads(1);
    }

}
