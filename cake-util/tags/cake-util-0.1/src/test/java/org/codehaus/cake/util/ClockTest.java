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
package org.codehaus.cake.util;

import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.util.Clock.DeterministicClock;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ClockTest.java 415 2007-11-09 08:25:23Z kasper $
 */
public class ClockTest {

    @Test
    public void testDefaultClock() {
        Clock c = Clock.DEFAULT_CLOCK;
        assertTrue(System.nanoTime() <= c.nanoTime());
        assertTrue(c.nanoTime() <= System.nanoTime());

        assertTrue(System.currentTimeMillis() <= c.timeOfDay());
        assertTrue(c.timeOfDay() <= System.currentTimeMillis());

//        long now = System.currentTimeMillis();
//        long deadline = c.getDeadlineFromNow(1, TimeUnit.SECONDS);
//        assertTrue(now + 1000 <= deadline);
//        assertTrue(deadline <= System.currentTimeMillis() + 1000);

        //assertTrue(c.isPassed(c.timeOfDay()));

        assertIsSerializable(c);
    }

    @Test
    public void testDeterministiskClock() {
        DeterministicClock c = new Clock.DeterministicClock();
        assertEquals(0l, c.nanoTime());
        assertEquals(0l, c.timeOfDay());
        c.setNanoTime(100);
        c.setTimeOfDay(200);

        assertEquals(100l, c.nanoTime());
        assertEquals(200l, c.timeOfDay());

        c.incrementNanoTime();
        c.incrementTimeOfDay();

        assertEquals(101l, c.nanoTime());
        assertEquals(201l, c.timeOfDay());

        c.incrementNanoTime(10);
        c.incrementTimeOfDay(10);

        assertEquals(111l, c.nanoTime());
        assertEquals(211l, c.timeOfDay());

        assertIsSerializable(c);
    }
}
