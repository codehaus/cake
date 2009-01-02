package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.BinaryPredicate;
import org.codehaus.cake.ops.Ops.BytePredicate;
import org.codehaus.cake.ops.Ops.CharPredicate;
import org.codehaus.cake.ops.Ops.DoublePredicate;
import org.codehaus.cake.ops.Ops.FloatPredicate;
import org.codehaus.cake.ops.Ops.IntPredicate;
import org.codehaus.cake.ops.Ops.LongPredicate;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.ShortPredicate;

/**
 * An abstract implementation of {@link CacheSelector} where only {@link CacheSelector#on(Predicate)} needs to be
 * overridden.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys
 * @param <V>
 *            the type of mapped values
 */
abstract class AbstractCacheSelector<K, V> implements CacheSelector<K, V> {

    /** {@inheritDoc} */
    public <T> Cache<K, V> on(final Attribute<T> attribute, final Predicate<T> filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                T t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final BinaryPredicate<? super K, ? super V> filter) {
        if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                K key = a.getKey();
                V value = a.getValue();
                return filter.op(key, value);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final BooleanAttribute attribute, final boolean value) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                boolean t = a.get(attribute);
                return t == value;
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final ByteAttribute attribute, final BytePredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                byte t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final CharAttribute attribute, final CharPredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                char t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final DoubleAttribute attribute, final DoublePredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                double t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final FloatAttribute attribute, final FloatPredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                float t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final IntAttribute attribute, final IntPredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                int t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final LongAttribute attribute, final LongPredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                long t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> on(final ShortAttribute attribute, final ShortPredicate filter) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        } else if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                short t = a.get(attribute);
                return filter.op(t);
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> onKey(final Predicate<? super K> filter) {
        if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                K k = a.getKey();
                return filter.op(k);
            }
        });
    }

    /** {@inheritDoc} */
    public <T extends K> Cache<T, V> onKeyType(final Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return (Cache) on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                K k = a.getKey();
                return clazz.isAssignableFrom(k.getClass());
            }
        });
    }

    /** {@inheritDoc} */
    public Cache<K, V> onValue(final Predicate<? super V> filter) {
        if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        return on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                V v = a.getValue();
                return filter.op(v);
            }
        });
    }

    /** {@inheritDoc} */
    public <T extends V> Cache<K, T> onValueType(final Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return (Cache) on(new Predicate<CacheEntry<K, V>>() {
            public boolean op(CacheEntry<K, V> a) {
                V v = a.getValue();
                return clazz.isAssignableFrom(v.getClass());
            }
        });
    }

}
