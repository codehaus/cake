package org.codehaus.cake.internal.cache.processor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.cake.cache.CacheDataExtractor;
import org.codehaus.cake.internal.cache.memorystore.openadressing.OpenAdressingEntry;
import org.codehaus.cake.internal.cache.view.ViewCommands;
import org.codehaus.cake.internal.cache.view.util.QueryStack;
import org.codehaus.cake.util.Maps;
import org.codehaus.cake.util.concurrent.array.ParallelArray;
import org.codehaus.cake.util.ops.Comparators;
import org.codehaus.cake.util.ops.ObjectOps;
import org.codehaus.cake.util.ops.Ops.BinaryProcedure;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.Procedure;
import org.codehaus.cake.util.ops.Ops.Reducer;

@SuppressWarnings("unchecked")
public class HashEntryArrayProcessor<K, V> {

    boolean isCopy;
    Object[] a;

    ParallelArray array;

    public HashEntryArrayProcessor(OpenAdressingEntry<K, V>[] entries, Predicate<OpenAdressingEntry<K, V>> filter) {
        a = new Object[entries.length];
        for (int i = 0; i < entries.length; i++)
            a[i] = entries[i];

        array = ParallelArray.createUsingHandoff(a, ParallelArray.defaultExecutor());
        isCopy = true;
    }

    public Object execute(QueryStack commands) {
        Object result = processCacheView(commands);
        if (!commands.isEmpty()) {
            throw new IllegalStateException();
        }
        return result;
    }

    Object processCacheView(QueryStack commands) {
        Object first = commands.pop();
        if (first instanceof Comparator) {
            Comparator comp = (Comparator) first;
            array.sort(comp);
            return processCacheView(commands);
        } else if (first == ViewCommands.MAP_TO_KEYS) {
            return processView(commands, Maps.MAP_ENTRY_TO_KEY_OP);
        } else if (first == ViewCommands.MAP_TO_VALUES) {
            return processView(commands, Maps.MAP_ENTRY_TO_VALUE_OP);
        } else if (first == ViewCommands.MAP_TO_MAP_ENTRIES) {
            return processMapView(commands);
        } else if (first instanceof Op) {
            return processView(commands, (Op) first);
        }
        throw new IllegalStateException("Unknown command " + first);
    }

    Object processMapView(QueryStack commands) {
        Object o = commands.pop();
        if (o == ViewCommands.MAP_TO_KEYS) {
            return processView(commands, Maps.MAP_ENTRY_TO_KEY_OP);
        } else if (o == ViewCommands.MAP_TO_VALUES) {
            return processView(commands, Maps.MAP_ENTRY_TO_VALUE_OP);
        } else if (o == ViewCommands.MAP_TO_MAP_ENTRIES) {
            return processView(commands, CacheDataExtractor.WHOLE_ENTRY);
        } else if (o instanceof BinaryProcedure) {
            final BinaryProcedure procedure = (BinaryProcedure) o;
            array.apply(new Procedure<Map.Entry>() {
                public void op(Entry a) {
                    procedure.op(a.getKey(), a.getValue());
                }
            });
            return null;
        } else if (o == Map.class) {
            LinkedHashMap map = new LinkedHashMap();
            for (Object ee : a) {
                Map.Entry e = (Entry) ee;
                map.put(e.getKey(), e.getValue());
            }
            return map;
        }
        throw new IllegalStateException();
    }

    Object processView(QueryStack commands, Op mapper) {
        Object o = commands.pop();
        if (o instanceof Comparator) {
            if (mapper != ObjectOps.CONSTANT_OP) {
                array.replaceWithMapping(mapper);
            }
            array.sort((Comparator) o);
            return processView(commands, ObjectOps.CONSTANT_OP);
        } else if (o == Comparators.NATURAL_COMPARATOR) {
            if (mapper != ObjectOps.CONSTANT_OP) {
                array.replaceWithMapping(mapper);
            }
            array.sort(Comparators.NATURAL_REVERSE_COMPARATOR);
            return processView(commands, ObjectOps.CONSTANT_OP);
        } else if (o == ViewCommands.ORDER_BY_NATURAL_MIN) {
            if (mapper != ObjectOps.CONSTANT_OP) {
                array.replaceWithMapping(mapper);
            }
            array.sort();
            return processView(commands, ObjectOps.CONSTANT_OP);
        } else if (o instanceof Op) {
            return processView(commands, ObjectOps.compoundMapper(mapper, (Op) o));
        } else if (o == List.class) {
            return Arrays.asList(array.withMapping(mapper).all().getArray());
        } else if (o == ViewCommands.ANY) {
            return array.withMapping(mapper).any(commands.pop());
        } else if (o == Array.class) {
            o = commands.pop();
            if (o == null) {
                return array.withMapping(mapper).all().getArray();
            } else {
                return Arrays.asList(array.withMapping(mapper).all().getArray()).toArray((Object[]) o);
            }
        } else if (o == ViewCommands.MAX_COMPARATOR) {
            return array.withMapping(mapper).max((Comparator) commands.pop());
        } else if (o == ViewCommands.MIN_COMPARATOR) {
            return array.withMapping(mapper).min((Comparator) commands.pop());
        } else if (o instanceof Reducer) {
            return array.withMapping(mapper).reduce((Reducer) o, commands.pop());
        } else if (o instanceof Procedure) {
            array.withMapping(mapper).apply((Procedure) o);
            return null;
        } else if (o == Long.class) {
            return array.size();
        } else if (o instanceof Long) {
            array.setLimit(((Long) o).intValue());
            return processView(commands, mapper);
        }
        throw new IllegalStateException("Unknown command:" + o.getClass());
    }
}
