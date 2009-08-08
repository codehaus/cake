package org.codehaus.cake.internal.unsafe;

public abstract class CakeUnsafe {

    final static CakeUnsafe UNSAFE;
    static {
        CakeUnsafe cu = null;
        try {
            cu = SunJava6Unsafe.UNSAFE;
        } catch (Throwable t) {}
        if (cu == null) {
            try {
                cu = SunJava5Unsafe.UNSAFE;
            } catch (Throwable t) {}
        }
        UNSAFE = cu;
    }

    public static CakeUnsafe getUnsafe() {
        return null;
    }

}
