package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.common.DurationAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;

/**
 * The attributes defined in this class a metadata that can be attached to each individual cache
 * entry.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
public class CacheAttributes {
    /* Entry attributes */

    /**
     * The <tt>Cost attribute</tt> can be used to indicate the cost of retrieving an entry. The
     * idea is that when memory is sparse the cache can choose to evict entries that are less costly
     * to retrieve again. Currently this attribute is not used by any of the build in replacement
     * policies. The default value is <tt>1.0</tt>.
     * <p>
     * While not required the unit of this attribute is most often <tt>time</tt>. For example,
     * how many milliseconds does it take to retrieve the entry.
     */
    public static final DoubleAttribute ENTRY_COST = new CostAttribute();

    /**
     * Currently not in use.
     */
    static final TimeInstanceAttribute ENTRY_DATE_ACCESSED = new DateCreatedAttribute();

    /**
     * The time between when the entry was created and midnight, January 1, 1970 UTC. This is also
     * the value returned by {@link System#currentTimeMillis()}. <p/> The
     * <li>
     * <ul>
     * If the entry is being loaded and the cache loader and this attribute has been set with a
     * positive value (>0). This value will be used to as the entrys creation time.
     * </ul>
     * <ul>
     * If a mapping already exists in the cache for the given key, the creation time is inherited
     * from this entry (the entry that is being replaced).
     * </ul>
     * <ul>
     * If no element is currently mapped for the key and a clock is set through
     * {@link CacheConfiguration#setClock(org.codehaus.cake.util.Clock)} a timestamp is obtained by
     * calling {@link org.codehaus.cake.util.Clock#timestamp()}.
     * </ul>
     * <ul>
     * Finally, if no clock was set when constructing the cache, System#currentTimeMillis() is used
     * for obtaining a timestamp.
     * </ul>
     * </li>
     * The creation time value is of type <tt>long</tt> and between 1 and {@link Long#MAX_VALUE}.
     */
    public static final TimeInstanceAttribute ENTRY_DATE_CREATED = new DateCreatedAttribute();

    /**
     * The time between when the entry was last modified and midnight, January 1, 1970 UTC. This is
     * also the value returned by {@link System#currentTimeMillis()}.
     * <p>
     * The mapped value must be of a type <tt>long</tt> between 1 and {@link Long#MAX_VALUE}.
     */
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
        /** The name of this attribute. */
        static final String NAME = "timeToLive";

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** The singleton instance of this attribute. */
        public static final TimeToLiveAttribute TIME_TO_LIVE = new TimeToLiveAttribute();

        /** Creates a new TimeToLiveAttribute. */
        private TimeToLiveAttribute() {
            super(NAME);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return TIME_TO_LIVE;
        }
    }
}
