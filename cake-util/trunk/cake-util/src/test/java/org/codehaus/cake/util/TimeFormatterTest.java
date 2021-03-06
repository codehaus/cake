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
package org.codehaus.cake.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimeFormatterTest {
    private TimeFormatter tf;

    @Test
    public void format() {
        tf = new TestHelper();
        assertEquals("1000000", tf.format(1 * 24 * 60 * 60, TimeUnit.SECONDS));
        assertEquals("1493161641000", tf.format(1290061001, TimeUnit.SECONDS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatIAE() {
        new TimeFormatter() {}.formatNanos(1);
    }

    @Test
    public void formatMillies() {
        tf = new TestHelper();
        assertEquals("0000400", tf.formatMillies(4));
        assertEquals("000099900", tf.formatMillies(999));
        assertEquals("0001000", tf.formatMillies(1000));
        assertEquals("0010000", tf.formatMillies(60000));
        assertEquals("0100000", tf.formatMillies(3600000));
        assertEquals("1000000", tf.formatMillies(24 * 3600000L));
    }

    @Test
    public void formatNanos() {
        tf = new TestHelper();
        assertEquals("0000001", tf.formatNanos(1));
        assertEquals("000000999", tf.formatNanos(999));
        assertEquals("0000010", tf.formatNanos(1000));
        assertEquals("0000100", tf.formatNanos(1000000));
        assertEquals("0001000", tf.formatNanos(1000000000));
        assertEquals("0010000", tf.formatNanos(60000000000L));
        assertEquals("0100000", tf.formatNanos(3600000000000L));
        assertEquals("1000000", tf.formatNanos(86400000000000L));
        assertEquals("1111111", tf.formatNanos(90061001001001L));
    }

    @Test
    public void getName() {
        TestHelper th = new TestHelper();
        assertEquals("nanosecond", th.getName(1, TimeUnit.NANOSECONDS));
        assertEquals("nanoseconds", th.getName(2, TimeUnit.NANOSECONDS));
        assertEquals("s", th.getSIName(TimeUnit.SECONDS));

    }

    public static void main(String[] args) {}

    @Test
    public void shortFormat() {
        TimeFormatter tf = TimeFormatter.DEFAULT;
        assertEquals("104 ns", tf.formatNanos(104L));

        // nevermind the codepage, test both . and ,
        assertTrue(tf.formatNanos(1004L).equals("1,004 \u00b5s") || tf.formatNanos(1004L).equals("1.004 \u00b5s"));
        assertTrue(tf.formatNanos(12321033L).equals("12,321 ms") || tf.formatNanos(12321033L).equals("12.321 ms"));
        assertTrue(tf.formatNanos(12321033000L).equals("12,321 s") || tf.formatNanos(12321033000L).equals("12.321 s"));

    }

    @Test
    public void uptimeFormat() {
        tf = TimeFormatter.UPTIME;
        assertEquals("11 day(s), 14:25:43 hours", tf.formatNanos(1002343410000000L));
        assertEquals("11 day(s), 14:25:43 hours", tf.format(1002343, TimeUnit.SECONDS));
        assertEquals("11 day(s), 14:25:00 hours", tf.format(1002300, TimeUnit.SECONDS));
        assertEquals("11 day(s), 14:25:00 hours", tf.format(16705 * 60, TimeUnit.SECONDS));
        assertEquals("11 day(s), 14:00:00 hours", tf.format(278 * 60 * 60, TimeUnit.SECONDS));
    }

    static class TestHelper extends TimeFormatter {
        @Override
        protected String doFormat(int days, int hours, int minutes, int seconds, int millies, int micros, int nano) {
            return "" + days + hours + minutes + seconds + millies + micros + nano;
        }

        @Override
        protected String getName(long value, TimeUnit unit) {
            return super.getName(value, unit);
        }

        @Override
        protected String getSIName(TimeUnit unit) {
            return super.getSIName(unit);
        }
    }
}
