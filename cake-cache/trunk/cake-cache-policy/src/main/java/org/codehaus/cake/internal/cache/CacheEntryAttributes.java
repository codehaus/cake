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
package org.codehaus.cake.internal.cache;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;

/**
 * @author knielsen22
 * 
 */
public class CacheEntryAttributes {

    // - Logger ->Log with stacktrace, log with timing information

    // - TimeToRefresh, RefreshTime
    // - TimeToExpire, ExpirationTime

    // public static long getTimeCreated(CacheEntry<?, ?> entry) {
    // return CacheEntry.TIME_CREATED.get(entry);
    // }
    //
    // public static void main(String[] args) {
    // CacheConfiguration<Integer, String> conf = new CacheConfiguration<Integer, String>();
    // conf.addEntryAttributes(CacheEntry.TIME_CREATED);
    // Cache<Integer, String> cache = new UnsynchronizedCache<Integer, String>(conf);
    // cache.put(4, "5");
    // System.out.println(getTimeCreated(cache.getEntry(4)));
    // System.out.println(CacheEntry.TIME_CREATED.get(cache.getEntry(4)));
    // }

    /**
     * The <tt>Cost attribute</tt> is used to indicate the cost of retrieving an entry. The idea is that when memory
     * is sparse the cache can choose to evict entries that are least costly to retrieve again. Currently this attribute
     * is used only by the {@link ReplaceCostliestPolicy} .
     * <p>
     * A frequent used unit for this attribute is time. For example, how many milliseconds does it take to retrieve the
     * entry. However, any unit can be used. Because any policy should only use the relative cost difference between
     * entries to determine what entries to evict.
     * <p>
     * <blockquote> <table border>
     * <tr>
     * <td>Type</td>
     * <td>double</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>1.0</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All values, except {@link Double#NEGATIVE_INFINITY} , {@link Double#POSITIVE_INFINITY} and
     * {@link Double#NaN} </td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Primarily <tt>time</tt>, but any unit is acceptable</td>
     * </tr>
     * </table> </blockquote>
     */
    public    static final DoubleAttribute COST = new CacheEntryAttributes.CostAttribute();
    /**
     * A count of how many times an entry has been accessed through {@link Cache#get(Object)} ,
     * {@link Cache#getEntry(Object)} or {@link Cache#getAll(java.util.Collection)} . <p/> The following list describes
     * how this attribute is obtained.
     * <ul>
     * <li> If an entry is being put or loaded and the <tt>HITS</tt> attribute has been set the cache will use this
     * value.</li>
     * <li> Else if this entry is replacing an existing entry the hit count from the existing entry will be used. </li>
     * <li> Else if this entry is accessed through <tt>get</tt>, <tt>getEntry</tt> or <tt>getAll</tt> the hit
     * count is incremented by 1</li>
     * </ul>
     * <p>
     * <blockquote> <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Number of Accesses</td>
     * </tr>
     * </table> </blockquote>
     */
    public   static final LongAttribute HITS = new CacheEntryAttributes.HitsAttribute();
    /**
     * The size of the cache entry. The volume of a cache is defined as the sum of the individual sizes of all entries
     * in the cache. This attribute is also used for deciding which entries to evict first in
     * {@link ReplaceBiggestPolicy}
     */
    public    static final LongAttribute SIZE = new CacheEntryAttributes.SizeAttribute();
    /**
     * The time between when the entry was last accessed (through calls to {@link Cache#get(Object)} and midnight,
     * January 1, 1970 UTC. This is also the value returned by {@link System#currentTimeMillis()} .
     * <p>
     * <blockquote> <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>System#currentTimeMillis() (see previous description)</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Milliseconds</td>
     * </tr>
     * </table> </blockquote>
     */
    public   static final TimeInstanceAttribute TIME_ACCESSED = new CacheEntryAttributes.TimeAccessedAttribute();
    /**
     * The time between when the entry was created and midnight, January 1, 1970 UTC. This is also the value returned by
     * {@link System#currentTimeMillis()} . <p/> The following list describes how this attribute is obtained.
     * <ul>
     * <li> If the entry is being put or loaded and the <tt>TIME_CREATED</tt> attribute has been set the cache will
     * use this value.</li>
     * <li> Else if this entry is replacing an existing entry the creation time from the existing entry will be used.
     * </li>
     * <li> Else if a clock is set through {@link CacheConfiguration#setClock(org.codehaus.cake.util.Clock)} a timestamp
     * is obtained by calling {@link org.codehaus.cake.util.Clock#timeOfDay()} . </li>
     * <li> Else if no clock has been set System#currentTimeMillis() is used for obtaining a timestamp. </li>
     * </ul>
     * <p>
     * <blockquote> <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>System#currentTimeMillis() (see previous description)</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Milliseconds</td>
     * </tr>
     * </table> </blockquote>
     */
    public   static final TimeInstanceAttribute TIME_CREATED = new CacheEntryAttributes.TimeCreatedAttribute();
    /**
     * The time between when the entry was last modified and midnight, January 1, 1970 UTC. This is also the value
     * returned by {@link System#currentTimeMillis()} .
     * <p>
     * The mapped value must be of a type <tt>long</tt> between 1 and {@link Long#MAX_VALUE} .
     */
    public   static final TimeInstanceAttribute TIME_MODIFIED = new CacheEntryAttributes.TimeModificedAttribute();
    /**
     * A count of how many times the value of an entry has been modified. <p/> The following list describes how this
     * attribute is obtained.
     * <ul>
     * <li> If the entry is being loaded and the <tt>VERSION</tt> attribute has been set the cache will use this
     * value.</li>
     * <li> Else if this entry is replacing an existing entry the hit count from the existing entry + 1 will be used.
     * </li>
     * <li> Else the version is initialized to 1</li>
     * </ul>
     * <p>
     * <blockquote> <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Number of modifications</td>
     * </tr>
     * </table> </blockquote>
     */
    public    static final LongAttribute VERSION = new CacheEntryAttributes.VersionAttribute();

    /** The Cost attribute. */
    static final class CostAttribute extends DoubleAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new CostAttribute. */
        CostAttribute() {
            super("Cost", 1.0);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return COST;
        }
    }

    /**
     * The <tt>Hits</tt> attribute indicates the number of hits for a cache element. The mapped value must be of a
     * type <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     */
    static final class HitsAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        HitsAttribute() {
            super("Hits");
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long hits) {
            return hits >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return HITS;
        }
    }

    /** The size attribute */
    static final class SizeAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        SizeAttribute() {
            super("Size", 1);
        }

        @Override
        protected String checkValidFailureMessage(Long value) {
            return "invalid size (size = " + value + ")";
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SIZE;
        }
    }

    static final class TimeAccessedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        TimeAccessedAttribute() {
            super("AccessTime");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return TIME_ACCESSED;
        }
    }

    static final class TimeCreatedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        TimeCreatedAttribute() {
            super("CreationTime");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return TIME_CREATED;
        }
    }

    static final class TimeModificedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        TimeModificedAttribute() {
            super("ModificationTime");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return TIME_MODIFIED;
        }
    }

    /**
     * This key can be used to indicate how long time an object should live. The time-to-live value should be a long
     * between 1 and {@link Long#MAX_VALUE} measured in nanoseconds. Use {@link java.util.concurrent.TimeUnit} to
     * convert between different time units.
     * 
     */
    // static final class TimeToLiveAttribute extends DurationAttribute {
    //
    // /** serialVersionUID. */
    // private static final long serialVersionUID = -2353351535602223603L;
    //
    // /** The singleton instance of this attribute. */
    // public static final TimeToLiveAttribute TIME_TO_LIVE = new TimeToLiveAttribute();
    //
    // /** Creates a new TimeToLiveAttribute. */
    // TimeToLiveAttribute() {
    // super("TimeToLive");
    // }
    //
    // /** @return Preserves singleton property */
    // private Object readResolve() {
    // return TIME_TO_LIVE;
    // }
    // }
    /**
     * The <tt>Version</tt> attribute indicates the number of hits for a cache element. The mapped value must be of a
     * type <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     */
    static final class VersionAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -235335135602223603L;

        /** Creates a new SizeAttribute. */
        VersionAttribute() {
            super("Version", 1);
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long hits) {
            return hits > 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return VERSION;
        }
    }
}
