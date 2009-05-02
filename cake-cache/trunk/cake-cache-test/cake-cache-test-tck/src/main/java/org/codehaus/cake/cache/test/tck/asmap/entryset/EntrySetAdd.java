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
package org.codehaus.cake.cache.test.tck.asmap.entryset;

import java.util.Collections;
import java.util.Set;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * Tests the add method for {@link Cache#entrySet()}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetAdd extends AbstractAsMapTCKTest {

    /**
     * Tests that {@link Set#addAll(java.util.Collection)} throws a {@link NullPointerException} when invoked with
     * <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void addAllNPE() {
        newCache();
        try {
            asMap().entrySet().addAll(null);
        } catch (UnsupportedOperationException e) {
            throw new NullPointerException(); // ok
        }
    }

    /**
     * Tests that {@link Set#addAll(java.util.Collection)} throws a {@link UnsupportedOperationException} when invoked
     * with a valid element.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addAllUOE() {
        newCache();
        asMap().entrySet().addAll(Collections.singleton(M1));
    }

    /**
     * Tests that {@link Set#add(Object)} throws a {@link NullPointerException} when invoked with <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void addNPE() {
        newCache();
        try {
           asMap().entrySet().add(null);
        } catch (UnsupportedOperationException e) {
            throw new NullPointerException(); // OK
        }
    }

    /**
     * Tests that {@link Set#add(Object)} throws a {@link UnsupportedOperationException} when invoked with a valid
     * element.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addUOE() {
        newCache();
        asMap().entrySet().add(M1);
    }
}
