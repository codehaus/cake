/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.test.tck.service.loading;

import static org.codehaus.cake.util.attribute.Attributes.from;

import java.util.Arrays;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.junit.Test;

public class LoadAllIterable extends AbstractCacheTCKTest {
    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test(expected = NullPointerException.class)
    public void withKeysNPE1() {
        withLoading().loadAll((Iterable) null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE2() {
        withLoading().loadAll(asList(1, null, 3));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE3() {
        withLoading().loadAll((Iterable) null, asDummy(MutableAttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE4() {
        withLoading().loadAll(asList(1, null, 3), asDummy(MutableAttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE5() {
        withLoading().loadAll(asList(1, 3), null);
    }

    @Test
    public void withLoadAll() {
        assertSize(0);
        loadAll(M1, M2, M3);
        assertGet(M1, M2, M3);
        assertLoads(3);
    }

    @Test
    public void withLoadAllAttributes() {
        loadAll(from(ATR1, "A", ATR2, "B"), M1, M2, M3);
        assertGet(M1, M2, M3);
        assertLoads(3);
    }

    @Test
    public void loadIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoading().loadAll(Arrays.asList(M1.getKey(), M2.getKey()));
        awaitTermination();
        withLoading().loadAll(Arrays.asList(M3.getKey(), M4.getKey()));
        awaitFinishedThreads();
        assertSize(0);
    }

    @Test
    public void withLoadMany() {
        loadAll(M1, M3, M4);
        loadAll(M2, entry(M4, null));
        loadAll(M5, entry(M6, null), entry(M7, null));
        loadAll(entry(M8, null), entry(M9, null));
        assertLoads(5);
    }

    @Test
    public void withLoadTwice() {
        loadAll(M1, M3, M4);
        loadAll(entry(M1, null), entry(M3, null), entry(M4, null));
        assertLoads(3);
    }
}
