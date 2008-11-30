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

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A Clock is used to create time values according to some custom policy. There are number of
 * situations where this is useful.
 * <ul>
 * <li>For simulation, if you have enough CPU/memory, a simulated environment can run much much
 * faster than a real clock</li>
 * <li>For testing, you often want to have some kind of determinism for your time values.</li>
 * <li>Sometimes you want to run with a clock that is offset from your computers clock. This can be
 * particularly helpful in a situation like Planetlab where some nodes have incorrect system clocks
 * due to misconfigured NTP servers. </li>
 * </ul>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id$
 */
public abstract class Clock {

    /** An instance of the {@link DefaultClock}. */
    public static final Clock DEFAULT_CLOCK = new DefaultClock();

    /**
     * This method can only be used to measure elapsed time and is not related to any other notion
     * of system or wall-clock time. The value returned represents nanoseconds since some fixed but
     * arbitrary time (perhaps in the future, so values may be negative). This method provides
     * nanosecond precision, but not necessarily nanosecond accuracy. No guarantees are made about
     * how frequently values change. Differences in successive calls that span greater than
     * approximately 292 years (2<sup>63</sup> nanoseconds) will not accurately compute elapsed
     * time due to numerical overflow.
     * <p>
     * 
     * @return The current time value in nanoseconds.
     */
    public abstract long nanoTime();

    /**
     * Returns the current time in milliseconds. Note that while the unit of time of the return
     * value is a millisecond, the granularity of the value depends on the underlying implementation
     * may be larger.
     * <p>
     * See the description of the class <code>Date</code> for a discussion of slight discrepancies
     * that may arise between "computer time" and coordinated universal time (UTC).
     * 
     * @return the difference, measured in milliseconds, between the current time and midnight,
     *         January 1, 1970 UTC.
     * @see java.util.Date
     */
    public abstract long timeOfDay();

    /**
     * The default implementation of Clock. {@link Clock#timeOfDay()} returns a value obtained from
     * {@link System#currentTimeMillis()}. {@link Clock#nanoTime()} returns a value obtained from
     * {@link System#nanoTime()}.
     */
    public static final  class DefaultClock extends Clock implements Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -3343971832371995608L;

        /** {@inheritDoc} */
        @Override
        public long nanoTime() {
            return System.nanoTime();
        }

        /** {@inheritDoc} */
        @Override
        public long timeOfDay() {
            return System.currentTimeMillis();
        }
    }

    /**
     * DeterministicClock is useful for testing components that rely on precise time interleavings.
     * This class is safe for use between multiple threads.
     * 
     * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
     * @version $Id$
     */
    public static class DeterministicClock extends Clock implements Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -7045902747103949579L;

        /** The current nano time. */
        private final AtomicLong nanoTime = new AtomicLong();

        /** The current timeOfDay. */
        private final AtomicLong timeOfDay = new AtomicLong();

        /** Increments the current relative time by 1. */
        public void incrementNanoTime() {
            nanoTime.incrementAndGet();
        }

        /**
         * Increments the current nano time by the specified amount.
         * 
         * @param amount
         *            the amount to increment the current nano time with
         */
        public void incrementNanoTime(int amount) {
            nanoTime.addAndGet(amount);
        }

        /** Increments the current time of day by 1. */
        public void incrementTimeOfDay() {
            timeOfDay.incrementAndGet();
        }

        /**
         * Increments the current time of day by the specified amount.
         * 
         * @param amount
         *            the amount to increment the current time of day with
         */
        public void incrementTimeOfDay(int amount) {
            timeOfDay.addAndGet(amount);
        }

        /** {@inheritDoc} */
        @Override
        public long nanoTime() {
            return nanoTime.get();
        }

        /**
         * Sets the current nano time.
         * 
         * @param nanoTime
         *            the nano time to set
         */
        public void setNanoTime(long nanoTime) {
            this.nanoTime.set(nanoTime);
        }

        /**
         * Sets the current time of day.
         * 
         * @param timeOfDay
         *            the time of day value to set
         */
        public void setTimeOfDay(long timeOfDay) {
            this.timeOfDay.set(timeOfDay);
        }

        /** {@inheritDoc} */
        @Override
        public long timeOfDay() {
            return timeOfDay.get();
        }
    }
// Utility functions that might be added at a later time
//  /**
//  * @return <tt>timeOfDay() + unit.toMillis(timeout)</tt>
//  */
// public long getDeadlineFromNow(long timeout, TimeUnit unit) {
//     return timeOfDay() + unit.toMillis(timeout);
// }

// /**
//  * @return timeOfDay() >= timeStampToCheck;
//  */
// public boolean isPassed(long timestamp) {
//     return isPassed(timeOfDay(), timestamp);
// }
//    /**
//     * @return <tt>currentTimeStamp >= timeStampToCheck</tt>
//     */
//    public static boolean isPassed(long currentTimeStamp, long timeStampToCheck) {
//        return currentTimeStamp >= timeStampToCheck;
//    }
}
