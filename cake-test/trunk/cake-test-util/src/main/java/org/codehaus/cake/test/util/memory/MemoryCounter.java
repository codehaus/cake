/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.test.util.memory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

public final class MemoryCounter {
    private static final MemorySizes sizes = new MemorySizes();

    private final Map visited = new IdentityHashMap();

    private final Stack stack = new Stack();

    private final Map<Class, Long> statistics = new HashMap<Class, Long>();

    public static long calc(Object obj) {
        return new MemoryCounter().op(obj);
    }

    public void printStatistics() {
        for (Map.Entry<Class, Long> e : statistics.entrySet()) {
            System.out.println(e.getKey() + " " +e.getValue());
        }
    }
    public synchronized long op(Object obj) {
        assert visited.isEmpty();
        assert stack.isEmpty();
        long result = _estimate(obj);
        while (!stack.isEmpty()) {
            result += _estimate(stack.pop());
        }
        visited.clear();
        //statistics.clear();
        return result;
    }

    private boolean skipObject(Object obj) {
        if (obj instanceof String) {
            // this will not cause a memory leak since
            // unused interned Strings will be thrown away
            if (obj == ((String) obj).intern()) {
                return true;
            }
        }
        if (obj instanceof ThreadGroup) {
            return true;
        }
        if (obj instanceof Thread) {
            return true;
        }
        if (obj instanceof IgnoreMemoryUsage) {
            return true;
        }
        return obj == null || visited.containsKey(obj);
    }

    private long _estimate(Object obj) {
        if (skipObject(obj)) {
            return 0;
        }
        visited.put(obj, null);
        long result = 0;
        Class clazz = obj.getClass();
        if (clazz.isArray()) {
            return _estimateArray(obj);
        }
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (!Modifier.isStatic(fields[i].getModifiers())) {
                    if (fields[i].getType().isPrimitive()) {
                        result += sizes.getPrimitiveFieldSize(fields[i].getType());
                    } else {
                        result += sizes.getPointerSize();
                        fields[i].setAccessible(true);
                        try {
                            Object toBeDone = fields[i].get(obj);
                            if (toBeDone != null) {
                                stack.add(toBeDone);
                            }
                        } catch (IllegalAccessException ex) {
                            assert false;
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        result += sizes.getClassSize();
        Long current = statistics.get(obj.getClass());
        statistics.put(obj.getClass(), current == null ? result : result + current);
        return roundUpToNearestEightBytes(result);
    }

    private long roundUpToNearestEightBytes(long result) {
        if (result % 8 != 0) {
            result += 8 - result % 8;
        }
        return result;
    }

    protected long _estimateArray(Object obj) {
        long result = 16;
        int length = Array.getLength(obj);
        if (length != 0) {
            Class arrayElementClazz = obj.getClass().getComponentType();
            if (arrayElementClazz.isPrimitive()) {
                result += length * sizes.getPrimitiveArrayElementSize(arrayElementClazz);
            } else {
                for (int i = 0; i < length; i++) {
                    result += sizes.getPointerSize() + _estimate(Array.get(obj, i));
                }
            }
        }
        Long current = statistics.get(obj.getClass());
        statistics.put(obj.getClass(), current == null ? result : result + current);
        return result;
    }

}
