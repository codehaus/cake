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
package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;

/**
 * @author knielsen22
 *
 */
class CacheEntryAttributes {
 //    - Logger ->Log with stacktrace, log with timing information
    
 //   - TimeToRefresh, RefreshTime
 //   - TimeToExpire, ExpirationTime

//    public static long getTimeCreated(CacheEntry<?, ?> entry) {
//        return CacheEntry.TIME_CREATED.get(entry);
//    }
//
//    public static void main(String[] args) {
//        CacheConfiguration<Integer, String> conf = new CacheConfiguration<Integer, String>();
//        conf.addEntryAttributes(CacheEntry.TIME_CREATED);
//        Cache<Integer, String> cache = new UnsynchronizedCache<Integer, String>(conf);
//        cache.put(4, "5");
//        System.out.println(getTimeCreated(cache.getEntry(4)));
//        System.out.println(CacheEntry.TIME_CREATED.get(cache.getEntry(4)));
//    }

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
            return CacheEntry.COST;
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
            return CacheEntry.HITS;
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
    
        /** {@inheritDoc} */
        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }
    
        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheEntry.SIZE;
        }

        @Override
        protected String checkValidFailureMessage(Long value) {
            return "invalid size (size = " + value + ")";
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
            return CacheEntry.TIME_ACCESSED;
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
            return CacheEntry.TIME_CREATED;
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
            return CacheEntry.TIME_MODIFIED;
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
            return CacheEntry.VERSION;
        }
    }
}
