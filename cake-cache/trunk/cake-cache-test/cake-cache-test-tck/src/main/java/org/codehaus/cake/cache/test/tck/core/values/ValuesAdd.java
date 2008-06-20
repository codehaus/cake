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
package org.codehaus.cake.cache.test.tck.core.values;

import java.util.Collections;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ValuesAdd.java 415 2007-11-09 08:25:23Z kasper $
 */
public class ValuesAdd extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void addNPE() {
        try {
            newCache().values().add(null);
        } catch (UnsupportedOperationException e) {
            throw new NullPointerException(); // OK
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addUOE() {
        newCache().values().add("1");
    }

    @Test(expected = NullPointerException.class)
    public void addAllNPE() {
        try {
            newCache().values().addAll(null);
        } catch (UnsupportedOperationException e) {
            throw new NullPointerException(); // OK
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllUOE() {
        newCache().values().addAll(Collections.singleton("1"));
    }
}
