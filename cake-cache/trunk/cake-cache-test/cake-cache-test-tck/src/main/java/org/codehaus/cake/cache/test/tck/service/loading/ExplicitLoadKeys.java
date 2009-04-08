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

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.junit.Test;

/**
 * Tests loading through one of the {@link Cache#get(Object)} or {@link Cache#getAllOld(java.util.Collection)} methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class ExplicitLoadKeys extends AbstractCacheTCKTest {

    public static final Attribute ATR1 = new ObjectAttribute("ATR1", String.class, "0") {};
    public static final Attribute ATR2 = new ObjectAttribute("ATR2", String.class, "1") {};
    public static final Attribute ATR3 = new ObjectAttribute("ATR3", String.class, "2") {};

    @Test
    public void withLoadAll() {
        assertSize(0);
        loadAll(M1, M2, M3);
        assertGet(M1, M2, M3);
        assertLoads(3);
    }

    @Test
    public void withLoadAllForce() {
        assertPeek(entry(M1, null));
        forceLoadAll(M1, M2, M3);
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
    public void withLoadAttributesTwice() {
        loadAll(from(ATR1, "A", ATR2, "B"), M1, M2, M3);
        // already here
        loadAll(from(ATR1, "A", ATR2, "B"), entry(M1, null), entry(M2, null), entry(M3, null));
        assertLoads(3);
    }

    @Test
    public void withLoadAttributesTwiceDiff() {
        loadAll(from(ATR1, "A", ATR2, "B"), M1, M3, M4);
        // already here
        loadAll(from(ATR1, "A", ATR2, "B"), entry(M1, null), M2, entry(M3, null), M5);
        assertLoads(5);
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
        forceLoadAll(from(ATR1, "A", ATR2, "B"), M1, M3);
        loader.withLoader(M1).setValue(M2.getValue());
        loader.withLoader(M3).setValue(M5.getValue());
        // already here, but force load
        forceLoadAll(from(ATR1, "A", ATR2, "B"), entry(M1, M2.getValue()), entry(M3, M5.getValue()));
        assertLoads(4);
    }
}
