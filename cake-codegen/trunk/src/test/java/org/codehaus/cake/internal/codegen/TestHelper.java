package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertSame;

public abstract class TestHelper {

    public static int[] getInts() {
        return new int[] { 9, 10, 11 };
    }

    public static Object[] getString(String test) {
        return new Object[] { test + "ok" };
    }

    public static void pushClass(Class<?> c) {
        assertSame(c, String.class);
    }

    
    public static interface IntToInt {
        int get(int i);
    }
}
