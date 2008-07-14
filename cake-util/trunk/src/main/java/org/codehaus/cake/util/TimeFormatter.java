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

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * A small utility class for formatting time durations.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
public abstract class TimeFormatter {

    public static final TimeFormatter DEFAULT = new DefaultFormatter();

    /**
     * A <tt>TimeFormatter</tt> that will format time the same was as the unix 'uptime' command.
     */
    public static final TimeFormatter UPTIME = new UptimeFormatter();

    private static final String[] NAME = new String[] { "nanosecond", "microsecond", "millisecond", "second", "minute",
            "hour", "day" };

    private static final String[] NAMES = new String[] { "nanoseconds", "microseconds", "milliseconds", "seconds",
            "minutes", "hours", "days" };

    private static final DecimalFormat NN = new DecimalFormat("00");

    private static final String[] SI_NAMES = new String[] { "ns", "\u00b5s", "ms", "s", "min", "h", "d" };

    private static final DecimalFormat Z = new DecimalFormat("##0.000");

    /**
     * Formats the specified time parameter.
     * 
     * @param nano
     *            the nanoseconds part
     * @return the formatted string
     */
    protected String doFormat(int nano) {
        return doFormat(0, nano);
    }

    /**
     * Formats the specified time parameters.
     * 
     * @param micros
     *            the microseconds part
     * @param nano
     *            the nanoseconds part
     * 
     * @return the formatted string
     */
    protected String doFormat(int micros, int nano) {
        return doFormat(0, micros, nano);
    }

    /**
     * Formats the specified time parameters.
     * 
     * @param millies
     *            the milliseconds part
     * @param micros
     *            the microseconds part
     * @param nano
     *            the nanoseconds part
     * 
     * @return the formatted string
     */
    protected String doFormat(int millies, int micros, int nano) {
        return doFormat(0, millies, micros, nano);
    }

    /**
     * Formats the specified time parameters.
     * 
     * @param seconds
     *            the seconds part
     * @param millies
     *            the milliseconds part
     * @param micros
     *            the microseconds part
     * @param nano
     *            the nanoseconds part
     * 
     * @return the formatted string
     */
    protected String doFormat(int seconds, int millies, int micros, int nano) {
        return doFormat(0, seconds, millies, micros, nano);
    }

    /**
     * Formats the specified time parameters.
     * 
     * @param minutes
     *            the minutes part
     * @param seconds
     *            the seconds part
     * @param millies
     *            the milliseconds part
     * @param micros
     *            the microseconds part
     * @param nano
     *            the nanoseconds part
     * 
     * @return the formatted string
     */
    protected String doFormat(int minutes, int seconds, int millies, int micros, int nano) {
        return doFormat(0, minutes, seconds, millies, micros, nano);
    }

    /**
     * Formats the specified time parameters.
     * 
     * @param hours
     *            the hours part
     * @param minutes
     *            the minutes part
     * @param seconds
     *            the seconds part
     * @param millies
     *            the milliseconds part
     * @param micros
     *            the microseconds part
     * @param nano
     *            the nanoseconds part
     * 
     * @return the formatted string
     */
    protected String doFormat(int hours, int minutes, int seconds, int millies, int micros, int nano) {
        return doFormat(0, hours, minutes, seconds, millies, micros, nano);
    }

    /**
     * Formats the specified time parameters.
     * 
     * @param days
     *            the days part
     * @param hours
     *            the hours part
     * @param minutes
     *            the minutes part
     * @param seconds
     *            the seconds part
     * @param millies
     *            the milliseconds part
     * @param micros
     *            the microseconds part
     * @param nano
     *            the nanoseconds part
     * 
     * @return the formatted string
     */
    protected String doFormat(int days, int hours, int minutes, int seconds, int millies, int micros, int nano) {
        throw new IllegalArgumentException("Cannot format the specified time");
    }

    /**
     * Formats the specified time to produce a string.
     * 
     * @param time
     *            the amount of time
     * @param unit
     *            the unit of the specified time
     * @return the formatting string
     */
    public String format(long time, TimeUnit unit) {
        return formatNanos(unit.toNanos(time));
    }

    /**
     * Formats the specified time to produce a string.
     * 
     * @param millies
     *            the amount of time in milliseconds
     * @return the formatting string
     */
    public String formatMillies(long millies) {
        return formatNanos(TimeUnit.MILLISECONDS.toNanos(millies));
    }

    /**
     * Formats the specified time to produce a string.
     * 
     * @param nanos
     *            the amount of time in nanoseconds
     * @return the formatting string
     */
    public String formatNanos(long nanos) {
        int nano = (int) (nanos % 1000);
        int micro = (int) (nanos / 1000 % 1000);
        int milli = (int) (nanos / 1000 / 1000 % 1000);
        int second = (int) (nanos / 1000 / 1000 / 1000 % 60);
        int minute = (int) (nanos / 1000 / 1000 / 1000 / 60 % 60);
        int hour = (int) (nanos / 1000 / 1000 / 1000 / 60 / 60 % 24);
        int day = (int) (nanos / 1000 / 1000 / 1000 / 60 / 60 / 24);
        if (day > 0) {
            return doFormat(day, hour, minute, second, milli, micro, nano);
        } else if (hour > 0) {
            return doFormat(hour, minute, second, milli, micro, nano);
        } else if (minute > 0) {
            return doFormat(minute, second, milli, micro, nano);
        } else if (second > 0) {
            return doFormat(second, milli, micro, nano);
        } else if (milli > 0) {
            return doFormat(milli, micro, nano);
        } else if (micro > 0) {
            return doFormat(micro, nano);
        } else {
            return doFormat(nano);
        }
    }

    protected String getName(long value, TimeUnit unit) {
        return value == 1 ? NAME[unit.ordinal()] : NAMES[unit.ordinal()];
    }

    protected String getSIName(TimeUnit unit) {
        return SI_NAMES[unit.ordinal()];
    }

    static class DefaultFormatter extends UptimeFormatter {
        /** {@inheritDoc} */
        @Override
        protected String doFormat(int nano) {
            return nano + " " + SI_NAMES[0];
        }

        /** {@inheritDoc} */
        @Override
        protected String doFormat(int micros, int nano) {
            return Z.format((micros * 1000 + nano) / 1000.0) + " " + SI_NAMES[1];
        }

        /** {@inheritDoc} */
        @Override
        protected String doFormat(int millies, int micros, int nano) {
            return Z.format((millies * 1000 + micros) / 1000.0) + " " + SI_NAMES[2];
        }

        /** {@inheritDoc} */
        @Override
        protected String doFormat(int seconds, int millies, int micros, int nano) {
            return Z.format((seconds * 1000 + millies) / 1000.0) + " s";
        }
    }

    static class UptimeFormatter extends TimeFormatter {
        /** {@inheritDoc} */
        @Override
        protected String doFormat(int days, int hours, int minutes, int seconds, int millies, int micros, int nano) {
            return days + " day(s), " + NN.format(hours) + ":" + NN.format(minutes) + ":" + NN.format(seconds)
                    + " hours";
        }
    }
}
