package org.codehaus.cake.cache.test.tck.selection;

import static org.codehaus.cake.util.attribute.AtrStubs.B_3;
import static org.codehaus.cake.util.attribute.AtrStubs.B_FALSE;
import static org.codehaus.cake.util.attribute.AtrStubs.C_1;
import static org.codehaus.cake.util.attribute.AtrStubs.D_2;
import static org.codehaus.cake.util.attribute.AtrStubs.F_2;
import static org.codehaus.cake.util.attribute.AtrStubs.I_1;
import static org.codehaus.cake.util.attribute.AtrStubs.I_3;
import static org.codehaus.cake.util.attribute.AtrStubs.L_3;
import static org.codehaus.cake.util.attribute.AtrStubs.O_2;
import static org.codehaus.cake.util.attribute.AtrStubs.S_2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.ops.Predicates;
import org.codehaus.cake.util.ops.PrimitivePredicates;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.BytePredicate;
import org.codehaus.cake.util.ops.Ops.CharPredicate;
import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.ShortPredicate;
public enum SelectedCacheType {
    ObjectSelection(O_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.withCrud().write().put(1, "A", O_2.singleton("A"));
            cache.withCrud().write().put(2, "B", O_2.singleton("B"));
            cache.withCrud().write().put(3, "C", O_2.singleton("C"));
            cache.withCrud().write().put(4, "D", O_2.singleton("D"));
            cache.withCrud().write().put(5, "E", O_2.singleton("E"));
            return cache.filter().on(O_2, new Predicate() {
                public boolean op(Object a) {
                    return a.equals("A") || a.equals("C") || a.equals("E");
                }
            });
        }
    },
    BooleanSelection(B_FALSE) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.withCrud().write().put(1, "A", B_FALSE.singletonFalse());
            cache.withCrud().write().put(2, "B", B_FALSE.singletonTrue());
            cache.put(3, "C");
            cache.withCrud().write().put(4, "D", B_FALSE.singletonTrue());
            cache.withCrud().write().put(5, "E");
            return cache.filter().on(B_FALSE, false);
        }
    },
    ByteSelection(B_3) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            HashMap<Integer, String> map = new HashMap<Integer, String>();
            map.put(1, "A");
            map.put(3, "C");
            map.put(5, "E");
            cache.putAll(map);
            cache.withCrud().write().put(2, "B", B_3.singleton((byte) 11));
            cache.withCrud().write().put(4, "D", B_3.singleton((byte) 0));
            return cache.filter().on(B_3, new BytePredicate() {
                public boolean op(byte a) {
                    return a == B_3.getDefaultValue();
                }
            });
        }
    },
    CharSelection(C_1, I_1) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.withCrud().write().put(i, Character.toString((char) (64 + i)),
                        Attributes.from(I_1, i, C_1, (char) (64 + i)));
            }
            return cache.filter().on(I_1, PrimitivePredicates.lessThen((char) 6)).filter().on(C_1, new CharPredicate() {
                public boolean op(char a) {
                    return ((int) a) % 2 == 1;
                }
            });
        }
    },
    DoubleSelection(D_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.withCrud().write().put(1, "A", D_2.singleton(1.1f));
            cache.withCrud().write().put(3, "C", D_2.singleton(-1f));
            cache.withCrud().write().put(5, "E", D_2.singleton(8f));

            cache.withCrud().write().put(4, "D");
            cache.withCrud().write().put(2, "B", D_2.singleton(4.1f));
            return cache.filter().on(D_2, new DoublePredicate() {
                public boolean op(double a) {
                    return a < 2 || a > 5;
                }
            });
        }
    },
    FloatSelection(F_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.withCrud().write().put(1, "A", F_2.singleton(1.1f));
            cache.withCrud().write().put(3, "C", F_2.singleton(-1f));
            cache.withCrud().write().put(5, "E", F_2.singleton(8f));

            cache.withCrud().write().put(4, "D");
            cache.withCrud().write().put(2, "B", F_2.singleton(4.1f));
            return cache.filter().on(F_2, new FloatPredicate() {
                public boolean op(float a) {
                    return a < 2 || a > 5;
                }
            });
        }
    },
    IntSelection(I_3) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.withCrud().write().put(1, "A", I_3.singleton(1));
            cache.withCrud().write().put(2, "B", I_3.singleton(11));
            cache.withCrud().write().put(3, "C", I_3.singleton(2));
            cache.withCrud().write().put(4, "D", I_3.singleton(0));
            cache.withCrud().write().put(5, "E");
            return cache.filter().on(I_3, new IntPredicate() {
                public boolean op(int a) {
                    return a > 0 && a < 4;
                }
            });
        }
    },
    LongSelection(L_3) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.withCrud().write().put(1, "A", L_3.singleton(-1));
            cache.withCrud().write().put(3, "C", L_3.singleton(-2));
            cache.withCrud().write().put(5, "E", L_3.singleton(-3));
            return cache.filter().on(L_3, new LongPredicate() {
                public boolean op(long a) {
                    return a < 0;
                }
            });
        }
    },
    ShortSelection(S_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.withCrud().write().put(i, "A", S_2.singleton((short) i));
            }
            cache.withCrud().write().put(1, "A", S_2.singleton((short) -1));
            cache.withCrud().write().put(3, "C", S_2.singleton((short) -3));
            cache.withCrud().write().put(5, "E", S_2.singleton(Short.MIN_VALUE));
            return cache.filter().on(S_2, new ShortPredicate() {
                public boolean op(short a) {
                    return a < 0;
                }
            });
        }
    },
    KeySelection() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, "A");
            }
            cache.withCrud().write().put(1, "A");
            cache.withCrud().write().put(3, "C");
            cache.withCrud().write().put(5, "E");
            return cache.filter().onKey(new Predicate() {
                public boolean op(Object a) {
                    return a.equals(1) || a.equals(3) || a.equals(5);
                }
            });
        }
    },
    KeySelectionType() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(new Long(i), "A");
            }
            cache.withCrud().write().put(1, "A");
            cache.withCrud().write().put(3, "C");
            cache.withCrud().write().put(5, "E");
            return (Cache) cache.filter().onKeyType(Integer.class);
        }
    },
    ValueSelection() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, "B");
            }
            cache.withCrud().write().put(1, "A");
            cache.withCrud().write().put(3, "C");
            cache.withCrud().write().put(5, "E");
            return cache.filter().onValue((Predicate) Predicates.notEqualsTo("B"));
        }
    },
    ValueSelectionType() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, 'A');
            }
            cache.withCrud().write().put(1, "A");
            cache.withCrud().write().put(3, "C");
            cache.withCrud().write().put(5, "E");
            return (Cache) cache.filter().onValueType(String.class);
        }
    },
    BinarySelection() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, "C");
            }
            cache.withCrud().write().put(1, "A");
            cache.withCrud().write().put(5, "E");
            return (Cache) cache.filter().on(new BinaryPredicate() {
                public boolean op(Object a, Object b) {
                    return !b.equals("C") || a.equals(3);
                }
            });
        }
    },
    Selection() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, "C");
            }
            cache.withCrud().write().put(1, "A");
            cache.withCrud().write().put(5, "E");
            return (Cache) cache.filter().on(new Predicate<CacheEntry<Object, Object>>() {
                public boolean op(CacheEntry<Object, Object> e) {
                    return e.getKey().equals(3) || e.getValue().equals("A") || e.getValue().equals("E");
                }
            });
        }
    }

    ;

    private final Collection<Attribute> a = new ArrayList<Attribute>();

    SelectedCacheType(Attribute... attributes) {
        for (Attribute a : attributes) {
            this.a.add(a);
        }
    }

    public Collection<Attribute> getAttribute() {
        return a;
    }

    public abstract Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache);

}
