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

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.junit.Test;

public class LoadForced extends AbstractCacheTCKTest {

    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test(expected = NullPointerException.class)
    public void withKeyNPE() {
        withLoadingForced().load(null);
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE1() {
        withLoadingForced().load(null, asDummy(MutableAttributeMap.class));
    }

    @Test(expected = NullPointerException.class)
    public void withKeyNPE2() {
        withLoadingForced().load(1, null);
    }

    @Test
    public void loadForce() {
        assertPeek(entry(M1, null));
        forceLoad(M1);
        assertPeek(M1);
        assertGet(M1);
        assertLoads(1);
    }

    @Test
    public void loadForceIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoadingForced().load(M1.getKey());
        awaitTermination();
        withLoadingForced().load(M2.getKey());
    }

    @Test
    public void loadForceTwice() {
        forceLoad(M1);
        loader.withLoader(M1).setValue(M2.getValue());
        forceLoad(entry(M1, M2.getValue()));// already here, but force load
        assertLoads(2);
    }

    @Test
    public void loadForceAttributesTwice() {
        forceLoad(M1, from(ATR1, "A", ATR2, "B"));
        loader.withLoader(M1).setValue(M3.getValue());
        // already here, but force load
        forceLoad(entry(M1, M3.getValue()), from(ATR1, "A", ATR3, "C"));
        assertLoads(2);
    }

    @Test
    public void loadForceAttributesIgnoredAfterShutdown() {
        prestart();
        shutdown();
        withLoadingForced().load(M1.getKey(), from(ATR1, "A", ATR2, "B"));
        awaitTermination();
        withLoadingForced().load(M2.getKey(), from(ATR1, "A", ATR2, "B"));
    }
}
