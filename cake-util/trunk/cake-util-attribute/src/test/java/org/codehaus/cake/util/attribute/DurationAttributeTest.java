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
package org.codehaus.cake.util.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class DurationAttributeTest extends AtrStubs {
    static final DurationAttribute DA = new DurationAttribute("foo") {};

    @Test
    public void checkValid() {
        assertTrue(DA.isValid(1));
        assertTrue(DA.isValid(Long.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        DA.checkValid(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE1() {
        DA.checkValid(Long.MIN_VALUE);
    }

    @Test
    public void get() {
        MutableAttributeMap am = Attributes.EMPTY_ATTRIBUTE_MAP;
        AttributeMap am1 = Attributes.singleton(DA, 1L);
        AttributeMap am10000 = Attributes.singleton(DA, 10000L);
        AttributeMap ammax = Attributes.singleton(DA, Long.MAX_VALUE);

        // No default
        assertEquals(Long.MAX_VALUE, DA.get(am, TimeUnit.NANOSECONDS));
        assertEquals(1l, DA.get(am1, TimeUnit.NANOSECONDS));
        assertEquals(10000l, DA.get(am10000, TimeUnit.NANOSECONDS));
        assertEquals(Long.MAX_VALUE, DA.get(ammax, TimeUnit.NANOSECONDS));

        assertEquals(Long.MAX_VALUE, DA.get(am, TimeUnit.MICROSECONDS));
        assertEquals(0l, DA.get(am1, TimeUnit.MICROSECONDS));
        assertEquals(10l, DA.get(am10000, TimeUnit.MICROSECONDS));
        assertEquals(Long.MAX_VALUE, DA.get(ammax, TimeUnit.MICROSECONDS));

        // With default
        assertEquals(1l, DA.get(am, TimeUnit.NANOSECONDS, 1l));
        assertEquals(-1l, DA.get(am, TimeUnit.NANOSECONDS, -1l));
        assertEquals(1l, DA.get(am, TimeUnit.MICROSECONDS, 1l));
        assertEquals(Long.MAX_VALUE, DA.get(am, TimeUnit.MICROSECONDS, Long.MAX_VALUE));

        assertEquals(1l, DA.get(am1, TimeUnit.NANOSECONDS, 2));
        assertEquals(10000l, DA.get(am10000, TimeUnit.NANOSECONDS, 2));
        assertEquals(Long.MAX_VALUE, DA.get(ammax, TimeUnit.NANOSECONDS, 2));
        assertEquals(0l, DA.get(am1, TimeUnit.MICROSECONDS, 2));
        assertEquals(10l, DA.get(am10000, TimeUnit.MICROSECONDS, 2));
        assertEquals(Long.MAX_VALUE, DA.get(ammax, TimeUnit.MICROSECONDS, 2));
    }

    @Test
    public void isValid() {
        assertFalse(DA.isValid(Long.MIN_VALUE));
        assertFalse(DA.isValid(0));
        assertTrue(DA.isValid(1));
        assertTrue(DA.isValid(Long.MAX_VALUE));
    }

    protected MutableAttributeMap newMap() {
        return new DefaultAttributeMap();
    }

    @Test
    public void set() {
        MutableAttributeMap am = newMap();
        DA.set(am, 10l, TimeUnit.NANOSECONDS);
        assertEquals(10l, am.get(DA));

        DA.set(am, 10l, TimeUnit.MICROSECONDS);
        assertEquals(10000l, am.get(DA));

        DA.set(am, Long.valueOf(10), TimeUnit.MICROSECONDS);
        assertEquals(10000l, am.get(DA));

        DA.set(am, Long.MAX_VALUE, TimeUnit.MICROSECONDS);
        assertEquals(Long.MAX_VALUE, am.get(DA));
    }

    @Test
    public void toSingleton() {
        assertEquals(10l, DA.singleton(10, TimeUnit.NANOSECONDS).get(DA));
        assertEquals(10000l, DA.singleton(10, TimeUnit.MICROSECONDS).get(DA));
        assertEquals(Long.MAX_VALUE, DA.singleton(Long.MAX_VALUE, TimeUnit.MICROSECONDS).get(DA));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        DA.singleton(-10, TimeUnit.NANOSECONDS);
    }
}
