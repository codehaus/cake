package org.codehaus.cake.internal.cache.query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.GetAttributer;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.attribute.common.ComparableObjectAttribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.query.CacheQuery;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.ops.Comparators;
import org.codehaus.cake.ops.DoubleOps;
import org.codehaus.cake.ops.FloatOps;
import org.codehaus.cake.ops.IntOps;
import org.codehaus.cake.ops.LongOps;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.ByteComparator;
import org.codehaus.cake.ops.Ops.CharComparator;
import org.codehaus.cake.ops.Ops.DoubleComparator;
import org.codehaus.cake.ops.Ops.FloatComparator;
import org.codehaus.cake.ops.Ops.IntComparator;
import org.codehaus.cake.ops.Ops.LongComparator;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.ShortComparator;

public class DefaultQuery<K, V> implements CacheQuery<K, V> {
    private final List<Comparator<? super CacheEntry<K, V>>> comparators = new ArrayList<Comparator<? super CacheEntry<K, V>>>();

    private int limit = Integer.MAX_VALUE;

    private final CacheProcessor<K, V> processor;
    private final Predicate<CacheEntry<K, V>> filter;

    public DefaultQuery(CacheProcessor<K, V> processor, Predicate<CacheEntry<K, V>> filter) {
        this.processor = processor;
        this.filter = filter;
    }

    public Map<K, V> entries() {
        return entries(ObjectOps.CONSTANT_OP, ObjectOps.CONSTANT_OP);
    }

    public <K1, V1> Map<K1, V1> entries(Op<K, K1> keyTransformer, Op<V, V1> valueTransformer) {
        if (keyTransformer == null) {
            throw new NullPointerException("keyTransformer is null");
        } else if (valueTransformer == null) {
            throw new NullPointerException("valueTransformer is null");
        }
        LinkedHashMap<K1, V1> map = new LinkedHashMap<K1, V1>();
        for (CacheEntry<K, V> e : this) {
            map.put(keyTransformer.op(e.getKey()), valueTransformer.op(e.getValue()));
        }
        return map;
    }

    public Iterator<CacheEntry<K, V>> iterator() {
        return all().iterator();
    }

    public List<K> keys() {
        return new ArrayList<K>(entries().keySet());
    }

    public CacheQuery<K, V> orderBy(final BooleanAttribute attribute, final boolean falseHighest) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                boolean thisVal = o1.get(attribute);
                boolean anotherVal = o2.get(attribute);
                // FIX
                return (thisVal && !anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
            }
        });
    }

    public CacheQuery<K, V> orderBy(Comparator<? super CacheEntry<K, V>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        comparators.add(comparator);
        return this;
    }

    public CacheQuery<K, V> orderBy(final ByteAttribute attribute, final ByteComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                byte thisVal = o1.get(attribute);
                byte anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderBy(final CharAttribute attribute, final CharComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                char thisVal = o1.get(attribute);
                char anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderBy(final ShortAttribute attribute, final ShortComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                short thisVal = o1.get(attribute);
                short anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderBy(final DoubleAttribute attribute, final DoubleComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                double thisVal = o1.get(attribute);
                double anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderBy(final FloatAttribute attribute, final FloatComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                float thisVal = o1.get(attribute);
                float anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderBy(final IntAttribute attribute, final IntComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                int thisVal = o1.get(attribute);
                int anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderBy(final LongAttribute attribute, final LongComparator comparator) {
        if (attribute==null) {
            throw new NullPointerException("attribute is null");
        } else if (comparator==null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<GetAttributer>() {
            public int compare(GetAttributer o1, GetAttributer o2) {
                long thisVal = o1.get(attribute);
                long anotherVal = o2.get(attribute);
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderByKeysMin() {
        return orderByKeys(Comparators.NATURAL_COMPARATOR);
    }

    public CacheQuery<K, V> orderByKeys(final Comparator<K> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<CacheEntry<K, V>>() {
            public int compare(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
                K thisVal = o1.getKey();
                K anotherVal = o2.getKey();
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public CacheQuery<K, V> orderByValuesMin() {
        return orderByValues(Comparators.NATURAL_COMPARATOR);
    }

    public CacheQuery<K, V> orderByValues(final Comparator<V> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return orderBy(new Comparator<CacheEntry<K, V>>() {
            public int compare(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
                V thisVal = o1.getValue();
                V anotherVal = o2.getValue();
                return comparator.compare(thisVal, anotherVal);
            }
        });
    }

    public void putInto(Cache<K, V> cache) {
        putInto(cache, ObjectOps.CONSTANT_OP);
    }

    public <K1, V1> void putInto(Cache<K1, V1> cache, Op<CacheEntry<K, V>, CacheEntry<K1, V1>> transformer) {
        if (cache == null) {
            throw new NullPointerException("cache is null");
        } else if (transformer == null) {
            throw new NullPointerException("transformer is null");
        }
        CrudWriter<K1, V1, Void> cw = cache.withCrud().write();
        for (CacheEntry<K, V> e : all()) {
            CacheEntry<K1, V1> conv = transformer.op(e);
            cw.put(conv.getKey(), conv.getValue(), conv);
        }
    }

    public CacheQuery<K, V> setLimit(int maxresults) {
        if (maxresults <= 0) {
            throw new IllegalArgumentException("maxresults must be posive (>0)");
        }
        this.limit = maxresults;
        return this;
    }

    public Comparator<? super CacheEntry<K, V>> getSortComparator() {
        return Comparators.stack(comparators);
    }

    public boolean isOrdered() {
        return comparators.size() > 0;
    }

    public int getLimit() {
        return limit;
    }

    public List<CacheEntry<K, V>> all() {
        return processor.process(filter, this);
    }

    public List<V> values() {
        return new ArrayList<V>(entries().values());
    }

    public CacheQuery<K, V> orderByKeysMax() {
        return orderByKeys(Comparators.NATURAL_REVERSE_COMPARATOR);
    }

    public CacheQuery<K, V> orderByValuesMax() {
        return orderByValues(Comparators.NATURAL_REVERSE_COMPARATOR);
    }

    public CacheQuery<K, V> orderByMax(Attribute<?> attribute) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        if (attribute instanceof BooleanAttribute) {
            return orderBy((BooleanAttribute) attribute, true);
        } else if (attribute instanceof ByteAttribute) {
            throw new IllegalArgumentException();
        } else if (attribute instanceof CharAttribute) {
            throw new IllegalArgumentException();
        } else if (attribute instanceof DoubleAttribute) {
            return orderBy((DoubleAttribute) attribute, DoubleOps.REVERSE_COMPARATOR);
        } else if (attribute instanceof FloatAttribute) {
            return orderBy((FloatAttribute) attribute, FloatOps.REVERSE_COMPARATOR);
        } else if (attribute instanceof IntAttribute) {
            return orderBy((IntAttribute) attribute, IntOps.REVERSE_COMPARATOR);
        } else if (attribute instanceof LongAttribute) {
            return orderBy((LongAttribute) attribute, LongOps.REVERSE_COMPARATOR);
        } else if (attribute instanceof ComparableObjectAttribute) {
            return orderBy(Comparators.reverseOrder((ComparableObjectAttribute) attribute));
        } else if (attribute instanceof ShortAttribute) {
            throw new IllegalArgumentException();
        } else {
            throw new IllegalArgumentException(
                    "ObjectAttribute cannot be used for sorting, Attribute must extend ComparableObjectAttribute");
        }

    }

    public CacheQuery<K, V> orderByMin(Attribute<?> attribute) {
        if (attribute == null) {
            throw new NullPointerException("attribute is null");
        }
        if (attribute instanceof BooleanAttribute) {
            return orderBy((BooleanAttribute) attribute, true);
        } else if (attribute instanceof ByteAttribute) {
            return orderBy((ByteAttribute) attribute);
        } else if (attribute instanceof CharAttribute) {
            return orderBy((CharAttribute) attribute);
        } else if (attribute instanceof DoubleAttribute) {
            return orderBy((DoubleAttribute) attribute, DoubleOps.COMPARATOR);
        } else if (attribute instanceof FloatAttribute) {
            return orderBy((FloatAttribute) attribute, FloatOps.COMPARATOR);
        } else if (attribute instanceof IntAttribute) {
            return orderBy((IntAttribute) attribute, IntOps.COMPARATOR);
        } else if (attribute instanceof LongAttribute) {
            return orderBy((LongAttribute) attribute, LongOps.COMPARATOR);
        } else if (attribute instanceof ComparableObjectAttribute) {
            return orderBy((ComparableObjectAttribute) attribute);
        } else if (attribute instanceof ShortAttribute) {
            return orderBy((ShortAttribute) attribute);
        } else {
            throw new IllegalArgumentException(
                    "ObjectAttribute cannot be used for sorting, Attribute must extend ComparableObjectAttribute");
        }
    }
}
