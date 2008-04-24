package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.common.DurationAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;

/**
 * There are usual
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
public class CacheAttributes {
    /* Entry attributes */

    public static final DoubleAttribute ENTRY_COST = new CostAttribute();

    static final TimeInstanceAttribute ENTRY_DATE_ACCESSED = new DateCreatedAttribute();

    /**
     * The time between the creation time of the entry and midnight, January 1, 1970 UTC. The mapped
     * value must be of a type <tt>long</tt> between 1 and {@link Long#MAX_VALUE}.
     */
    public static final TimeInstanceAttribute ENTRY_DATE_CREATED = new DateCreatedAttribute();
    public static final TimeInstanceAttribute ENTRY_DATE_MODIFIED = new DateModificedAttribute();
    /**
     * The size of the cache entry.
     */
    public static final LongAttribute ENTRY_SIZE = new SizeAttribute();

    // public static final TimeInstanceAttribute ENTRY_TIME_TO_LIVE = ENTRY_DATE_MODIFIED;

    // cost of retrieving the item
    // time to live
    // expiration time = time to live + System.timestamp
    // Logger <-detailed logging about an entry.

    static final class CostAttribute extends DoubleAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new CostAttribute. */
        private CostAttribute() {
            super("Cost", 1.0);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ENTRY_COST;
        }
    }

    static final class DateCreatedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        DateCreatedAttribute() {
            super("Date Created");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ENTRY_DATE_CREATED;
        }
    }

    static final class DateModificedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        DateModificedAttribute() {
            super("Date Modified");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ENTRY_DATE_MODIFIED;
        }
    }

    static final class DateAccessedAttribute extends TimeInstanceAttribute {
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new DateCreatedAttribute. */
        DateAccessedAttribute() {
            super("Date Accessed");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ENTRY_DATE_ACCESSED;
        }
    }

    static final class SizeAttribute extends LongAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        private SizeAttribute() {
            super("size", 1);
        }

        /** {@inheritDoc} */
        @Override
        public void checkValid(long value) {
            if (value < 0) {
                throw new IllegalArgumentException("invalid size (size = " + value + ")");
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ENTRY_SIZE;
        }
    }

    /**
     * This key can be used to indicate how long time an object should live. The time-to-live value
     * should be a long between 1 and {@link Long#MAX_VALUE} measured in nanoseconds. Use
     * {@link java.util.concurrent.TimeUnit} to convert between different time units.
     * 
     */
    static final class TimeToLiveAttribute extends DurationAttribute {
        /** The singleton instance of this attribute. */
        public static final TimeToLiveAttribute TIME_TO_LIVE = new TimeToLiveAttribute();

        /** The name of this attribute. */
        static final String NAME = "timeToLive";

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new TimeToLiveAttribute. */
        private TimeToLiveAttribute() {
            super(NAME);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return TIME_TO_LIVE;
        }
    }

    /**
     * The <tt>Hits</tt> attribute indicates the number of hits for a cache element. The mapped
     * value must be of a type <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     * 
     * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
     * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
     */
    static final class HitsAttribute extends LongAttribute {

        /** The default value of this attribute. */
        static final long DEFAULT_VALUE = 0;

        /** The singleton instance of this attribute. */
        public final static HitsAttribute INSTANCE = new HitsAttribute();

        /** The name of this attribute. */
        static final String NAME = "hits";

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        private HitsAttribute() {
            super(NAME, DEFAULT_VALUE);
        }

        /** {@inheritDoc} */
        @Override
        public void checkValid(long hits) {
            if (hits < 0) {
                throw new IllegalArgumentException("invalid hit count (hits = " + hits + ")");
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long hits) {
            return hits >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return INSTANCE;
        }
    }
}
