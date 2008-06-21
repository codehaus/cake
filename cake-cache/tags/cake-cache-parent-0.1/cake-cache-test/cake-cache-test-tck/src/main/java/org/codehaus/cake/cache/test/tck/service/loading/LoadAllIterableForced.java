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

import java.util.Arrays;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class LoadAllIterableForced extends AbstractCacheTCKTest {
    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test
    public void withLoadAllForce() {
        assertPeek(entry(M1, null));
        forceLoadAll(M1, M2, M3);
        assertGet(M1, M2, M3);
        assertLoads(3);
    }

    @Test
    public void withLoadForceTwice() {
        forceLoadAll(M1, M3, M4);
        loader.withLoader(M1).setValue(M2.getValue());
        loader.withLoader(M3).setValue(M5.getValue());
        loader.withLoader(M4).setValue(M6.getValue());

        // already here, but force load
        forceLoadAll(M5, entry(M1, M2.getValue()), entry(M3, M5.getValue()), entry(M4, M6.getValue()));
        assertLoads(7);
    }

    @Test
    public void withLoadForceAttributesTwice() {
        forceLoadAll(asAtrMap(ATR1, "A", ATR2, "B"), M1, M3);
        loader.withLoader(M1).setValue(M2.getValue());
        loader.withLoader(M3).setValue(M5.getValue());
        // already here, but force load
        forceLoadAll(asAtrMap(ATR1, "A", ATR2, "B"), entry(M1, M2.getValue()), entry(M3, M5.getValue()));
        assertLoads(4);
    }

    @Test
    public void loadIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoadingForced().loadAll(Arrays.asList(M1.getKey(), M2.getKey()));
        awaitTermination();
        withLoadingForced().loadAll(Arrays.asList(M3.getKey(), M4.getKey()));
        awaitFinishedThreads();
        assertSize(0);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE1() {
        withLoadingForced().loadAll((Iterable) null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE2() {
        withLoadingForced().loadAll(asList(1, null, 3));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE3() {
        withLoadingForced().loadAll((Iterable) null, asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE4() {
        withLoadingForced().loadAll(asList(1, null, 3), asDummy(AttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeysNPE5() {
        withLoadingForced().loadAll(asList(1, 3), null);
    }
}
