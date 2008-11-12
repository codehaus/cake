package org.codehaus.cake.cache.test.tck.selection;

import static org.codehaus.cake.cache.test.util.AtrStubs.*;
import static org.codehaus.cake.cache.test.util.AtrStubs.B_FALSE;
import static org.codehaus.cake.cache.test.util.AtrStubs.C_1;
import static org.codehaus.cake.cache.test.util.AtrStubs.D_2;
import static org.codehaus.cake.cache.test.util.AtrStubs.F_2;
import static org.codehaus.cake.cache.test.util.AtrStubs.I_1;
import static org.codehaus.cake.cache.test.util.AtrStubs.I_3;
import static org.codehaus.cake.cache.test.util.AtrStubs.L_3;
import static org.codehaus.cake.cache.test.util.AtrStubs.S_2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.IntPredicates;
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

public enum SelectedCacheType {
    ObjectSelection(O_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.crud().write().put(1, "A", O_2.singleton("A"));
            cache.crud().write().put(2, "B", O_2.singleton("B"));
            cache.crud().write().put(3, "C", O_2.singleton("C"));
            cache.crud().write().put(4, "D", O_2.singleton("D"));
            cache.crud().write().put(5, "E", O_2.singleton("E"));
            return cache.select().on(O_2, new Predicate() {

                public boolean op(Object a) {
                    return a.equals("A") || a.equals("C") || a.equals("E");
                }
            });
        }
    },
    BooleanSelection(B_FALSE) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.crud().write().put(1, "A", B_FALSE.singletonFalse());
            cache.crud().write().put(2, "B", B_FALSE.singletonTrue());
            cache.put(3, "C");
            cache.crud().write().put(4, "D", B_FALSE.singletonTrue());
            cache.crud().write().put(5, "E");
            return cache.select().on(B_FALSE, false);
        }
    },
    ByteSelection(B_3) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            HashMap<Integer, String> map = new HashMap<Integer, String>();
            map.put(1, "A");
            map.put(3, "C");
            map.put(5, "E");
            cache.putAll(map);
            cache.crud().write().put(2, "B", B_3.singleton((byte) 11));
            cache.crud().write().put(4, "D", B_3.singleton((byte) 0));
            return cache.select().on(B_3, new BytePredicate() {
                public boolean op(byte a) {
                    return a == B_3.getDefaultValue();
                }
            });
        }
    },
    CharSelection(C_1, I_1) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.crud().write().put(i, Character.toString((char) (64 + i)),
                        Attributes.from(I_1, i, C_1, (char) (64 + i)));
            }
            return cache.select().on(I_1, IntPredicates.lessThen(6)).select().on(C_1, new CharPredicate() {
                public boolean op(char a) {
                    return ((int) a) % 2 == 1;
                }
            });
        }
    },
    DoubleSelection(D_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.crud().write().put(1, "A", D_2.singleton(1.1f));
            cache.crud().write().put(3, "C", D_2.singleton(-1f));
            cache.crud().write().put(5, "E", D_2.singleton(8f));

            cache.crud().write().put(4, "D");
            cache.crud().write().put(2, "B", D_2.singleton(4.1f));
            return cache.select().on(D_2, new DoublePredicate() {
                public boolean op(double a) {
                    return a < 2 || a > 5;
                }
            });
        }
    },
    FloatSelection(F_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.crud().write().put(1, "A", F_2.singleton(1.1f));
            cache.crud().write().put(3, "C", F_2.singleton(-1f));
            cache.crud().write().put(5, "E", F_2.singleton(8f));

            cache.crud().write().put(4, "D");
            cache.crud().write().put(2, "B", F_2.singleton(4.1f));
            return cache.select().on(F_2, new FloatPredicate() {
                public boolean op(float a) {
                    return a < 2 || a > 5;
                }
            });
        }
    },
    IntSelection(I_3) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.crud().write().put(1, "A", I_3.singleton(1));
            cache.crud().write().put(2, "B", I_3.singleton(11));
            cache.crud().write().put(3, "C", I_3.singleton(2));
            cache.crud().write().put(4, "D", I_3.singleton(0));
            cache.crud().write().put(5, "E");
            return cache.select().on(I_3, new IntPredicate() {
                public boolean op(int a) {
                    return a > 0 && a < 4;
                }
            });
        }
    },
    LongSelection(L_3) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            cache.crud().write().put(1, "A", L_3.singleton(-1));
            cache.crud().write().put(3, "C", L_3.singleton(-2));
            cache.crud().write().put(5, "E", L_3.singleton(-3));
            return cache.select().on(L_3, new LongPredicate() {
                public boolean op(long a) {
                    return a < 0;
                }
            });
        }
    },
    ShortSelection(S_2) {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.crud().write().put(i, "A", S_2.singleton((short) i));
            }
            cache.crud().write().put(1, "A", S_2.singleton((short) -1));
            cache.crud().write().put(3, "C", S_2.singleton((short) -3));
            cache.crud().write().put(5, "E", S_2.singleton(Short.MIN_VALUE));
            return cache.select().on(S_2, new ShortPredicate() {
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
            cache.crud().write().put(1, "A");
            cache.crud().write().put(3, "C");
            cache.crud().write().put(5, "E");
            return cache.select().onKey(new Predicate() {
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
            cache.crud().write().put(1, "A");
            cache.crud().write().put(3, "C");
            cache.crud().write().put(5, "E");
            return (Cache) cache.select().onKeyType(Integer.class);
        }
    },
    ValueSelection() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, "B");
            }
            cache.crud().write().put(1, "A");
            cache.crud().write().put(3, "C");
            cache.crud().write().put(5, "E");
            return cache.select().onValue((Predicate) Predicates.notEqualsTo("B"));
        }
    },
    ValueSelectionType() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, 'A');
            }
            cache.crud().write().put(1, "A");
            cache.crud().write().put(3, "C");
            cache.crud().write().put(5, "E");
            return (Cache) cache.select().onValueType(String.class);
        }
    },
    BinarySelection() {
        public Cache<Object, Object> fillAndSelect(Cache<Object, Object> cache) {
            for (int i = 0; i < 100; i++) {
                cache.put(i, "C");
            }
            cache.crud().write().put(1, "A");
            cache.crud().write().put(5, "E");
            return (Cache) cache.select().on(new BinaryPredicate() {
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
            cache.crud().write().put(1, "A");
            cache.crud().write().put(5, "E");
            return (Cache) cache.select().on(new Predicate<CacheEntry<Object, Object>>() {
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
