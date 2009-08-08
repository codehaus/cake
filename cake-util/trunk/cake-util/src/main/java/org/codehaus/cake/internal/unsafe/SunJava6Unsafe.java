package org.codehaus.cake.internal.unsafe;


class SunJava6Unsafe extends CakeUnsafe {
//    static SunJava6Unsafe UNSAFE;
//    static sun.misc.Unsafe THEUNSAFE;
//    public int addressSize() {
//        return THEUNSAFE.addressSize();
//    }
//    public Object allocateInstance(Class<?> arg0) throws InstantiationException {
//        return THEUNSAFE.allocateInstance(arg0);
//    }
//    public long allocateMemory(long arg0) {
//        return THEUNSAFE.allocateMemory(arg0);
//    }
//    public int arrayBaseOffset(Class<?> arg0) {
//        return THEUNSAFE.arrayBaseOffset(arg0);
//    }
//    public int arrayIndexScale(Class<?> arg0) {
//        return THEUNSAFE.arrayIndexScale(arg0);
//    }
//    public final boolean compareAndSwapInt(Object arg0, long arg1, int arg2, int arg3) {
//        return THEUNSAFE.compareAndSwapInt(arg0, arg1, arg2, arg3);
//    }
//    public final boolean compareAndSwapLong(Object arg0, long arg1, long arg2, long arg3) {
//        return THEUNSAFE.compareAndSwapLong(arg0, arg1, arg2, arg3);
//    }
//    public final boolean compareAndSwapObject(Object arg0, long arg1, Object arg2, Object arg3) {
//        return THEUNSAFE.compareAndSwapObject(arg0, arg1, arg2, arg3);
//    }
//    public void copyMemory(long arg0, long arg1, long arg2) {
//        THEUNSAFE.copyMemory(arg0, arg1, arg2);
//    }
//    public Class<?> defineClass(String arg0, byte[] arg1, int arg2, int arg3, ClassLoader arg4, ProtectionDomain arg5) {
//        return THEUNSAFE.defineClass(arg0, arg1, arg2, arg3, arg4, arg5);
//    }
//    public Class<?> defineClass(String arg0, byte[] arg1, int arg2, int arg3) {
//        return THEUNSAFE.defineClass(arg0, arg1, arg2, arg3);
//    }
//    public void ensureClassInitialized(Class<?> arg0) {
//        THEUNSAFE.ensureClassInitialized(arg0);
//    }
//    public boolean equals(Object obj) {
//        return THEUNSAFE.equals(obj);
//    }
//    public int fieldOffset(Field arg0) {
//        return THEUNSAFE.fieldOffset(arg0);
//    }
//    public void freeMemory(long arg0) {
//        THEUNSAFE.freeMemory(arg0);
//    }
//    public long getAddress(long arg0) {
//        return THEUNSAFE.getAddress(arg0);
//    }
//    public boolean getBoolean(Object arg0, int arg1) {
//        return THEUNSAFE.getBoolean(arg0, arg1);
//    }
//    public boolean getBoolean(Object arg0, long arg1) {
//        return THEUNSAFE.getBoolean(arg0, arg1);
//    }
//    public boolean getBooleanVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getBooleanVolatile(arg0, arg1);
//    }
//    public byte getByte(long arg0) {
//        return THEUNSAFE.getByte(arg0);
//    }
//
//    public byte getByte(Object arg0, long arg1) {
//        return THEUNSAFE.getByte(arg0, arg1);
//    }
//    public byte getByteVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getByteVolatile(arg0, arg1);
//    }
//    public char getChar(long arg0) {
//        return THEUNSAFE.getChar(arg0);
//    }
//    public char getChar(Object arg0, long arg1) {
//        return THEUNSAFE.getChar(arg0, arg1);
//    }
//    public char getCharVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getCharVolatile(arg0, arg1);
//    }
//    public double getDouble(long arg0) {
//        return THEUNSAFE.getDouble(arg0);
//    }
//    public double getDouble(Object arg0, int arg1) {
//        return THEUNSAFE.getDouble(arg0, arg1);
//    }
//    public double getDouble(Object arg0, long arg1) {
//        return THEUNSAFE.getDouble(arg0, arg1);
//    }
//    public double getDoubleVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getDoubleVolatile(arg0, arg1);
//    }
//    public float getFloat(long arg0) {
//        return THEUNSAFE.getFloat(arg0);
//    }
//    public float getFloat(Object arg0, int arg1) {
//        return THEUNSAFE.getFloat(arg0, arg1);
//    }
//    public float getFloat(Object arg0, long arg1) {
//        return THEUNSAFE.getFloat(arg0, arg1);
//    }
//    public float getFloatVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getFloatVolatile(arg0, arg1);
//    }
//    public int getInt(long arg0) {
//        return THEUNSAFE.getInt(arg0);
//    }
//    public int getInt(Object arg0, int arg1) {
//        return THEUNSAFE.getInt(arg0, arg1);
//    }
//    public int getInt(Object arg0, long arg1) {
//        return THEUNSAFE.getInt(arg0, arg1);
//    }
//    public int getIntVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getIntVolatile(arg0, arg1);
//    }
//    public long getLong(long arg0) {
//        return THEUNSAFE.getLong(arg0);
//    }
//    public long getLong(Object arg0, int arg1) {
//        return THEUNSAFE.getLong(arg0, arg1);
//    }
//    public long getLong(Object arg0, long arg1) {
//        return THEUNSAFE.getLong(arg0, arg1);
//    }
//    public long getLongVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getLongVolatile(arg0, arg1);
//    }
//    public Object getObject(Object arg0, int arg1) {
//        return THEUNSAFE.getObject(arg0, arg1);
//    }
//    public Object getObject(Object arg0, long arg1) {
//        return THEUNSAFE.getObject(arg0, arg1);
//    }
//    public Object getObjectVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getObjectVolatile(arg0, arg1);
//    }
//    public short getShort(long arg0) {
//        return THEUNSAFE.getShort(arg0);
//    }
//    public short getShort(Object arg0, int arg1) {
//        return THEUNSAFE.getShort(arg0, arg1);
//    }
//    public short getShort(Object arg0, long arg1) {
//        return THEUNSAFE.getShort(arg0, arg1);
//    }
//    public short getShortVolatile(Object arg0, long arg1) {
//        return THEUNSAFE.getShortVolatile(arg0, arg1);
//    }
//    public int hashCode() {
//        return THEUNSAFE.hashCode();
//    }
//    public void monitorEnter(Object arg0) {
//        THEUNSAFE.monitorEnter(arg0);
//    }
//    public void monitorExit(Object arg0) {
//        THEUNSAFE.monitorExit(arg0);
//    }
//    public long objectFieldOffset(Field arg0) {
//        return THEUNSAFE.objectFieldOffset(arg0);
//    }
//    public int pageSize() {
//        return THEUNSAFE.pageSize();
//    }
//    public void park(boolean arg0, long arg1) {
//        THEUNSAFE.park(arg0, arg1);
//    }
//    public void putAddress(long arg0, long arg1) {
//        THEUNSAFE.putAddress(arg0, arg1);
//    }
//    public void putBoolean(Object arg0, int arg1, boolean arg2) {
//        THEUNSAFE.putBoolean(arg0, arg1, arg2);
//    }
//    public void putBoolean(Object arg0, long arg1, boolean arg2) {
//        THEUNSAFE.putBoolean(arg0, arg1, arg2);
//    }
//    public void putBooleanVolatile(Object arg0, long arg1, boolean arg2) {
//        THEUNSAFE.putBooleanVolatile(arg0, arg1, arg2);
//    }
//    public void putByte(long arg0, byte arg1) {
//        THEUNSAFE.putByte(arg0, arg1);
//    }
//    public void putByte(Object arg0, int arg1, byte arg2) {
//        THEUNSAFE.putByte(arg0, arg1, arg2);
//    }
//    public void putByte(Object arg0, long arg1, byte arg2) {
//        THEUNSAFE.putByte(arg0, arg1, arg2);
//    }
//    public void putByteVolatile(Object arg0, long arg1, byte arg2) {
//        THEUNSAFE.putByteVolatile(arg0, arg1, arg2);
//    }
//    public void putChar(long arg0, char arg1) {
//        THEUNSAFE.putChar(arg0, arg1);
//    }
//    public void putChar(Object arg0, int arg1, char arg2) {
//        THEUNSAFE.putChar(arg0, arg1, arg2);
//    }
//    public void putChar(Object arg0, long arg1, char arg2) {
//        THEUNSAFE.putChar(arg0, arg1, arg2);
//    }
//    public void putCharVolatile(Object arg0, long arg1, char arg2) {
//        THEUNSAFE.putCharVolatile(arg0, arg1, arg2);
//    }
//    public void putDouble(long arg0, double arg1) {
//        THEUNSAFE.putDouble(arg0, arg1);
//    }
//    public void putDouble(Object arg0, int arg1, double arg2) {
//        THEUNSAFE.putDouble(arg0, arg1, arg2);
//    }
//    public void putDouble(Object arg0, long arg1, double arg2) {
//        THEUNSAFE.putDouble(arg0, arg1, arg2);
//    }
//    public void putDoubleVolatile(Object arg0, long arg1, double arg2) {
//        THEUNSAFE.putDoubleVolatile(arg0, arg1, arg2);
//    }
//    public void putFloat(long arg0, float arg1) {
//        THEUNSAFE.putFloat(arg0, arg1);
//    }
//    public void putFloat(Object arg0, int arg1, float arg2) {
//        THEUNSAFE.putFloat(arg0, arg1, arg2);
//    }
//    public void putFloat(Object arg0, long arg1, float arg2) {
//        THEUNSAFE.putFloat(arg0, arg1, arg2);
//    }
//    public void putFloatVolatile(Object arg0, long arg1, float arg2) {
//        THEUNSAFE.putFloatVolatile(arg0, arg1, arg2);
//    }
//    public void putInt(long arg0, int arg1) {
//        THEUNSAFE.putInt(arg0, arg1);
//    }
//    public void putInt(Object arg0, int arg1, int arg2) {
//        THEUNSAFE.putInt(arg0, arg1, arg2);
//    }
//    public void putInt(Object arg0, long arg1, int arg2) {
//        THEUNSAFE.putInt(arg0, arg1, arg2);
//    }
//    public void putIntVolatile(Object arg0, long arg1, int arg2) {
//        THEUNSAFE.putIntVolatile(arg0, arg1, arg2);
//    }
//    public void putLong(long arg0, long arg1) {
//        THEUNSAFE.putLong(arg0, arg1);
//    }
//    public void putLong(Object arg0, int arg1, long arg2) {
//        THEUNSAFE.putLong(arg0, arg1, arg2);
//    }
//    public void putLong(Object arg0, long arg1, long arg2) {
//        THEUNSAFE.putLong(arg0, arg1, arg2);
//    }
//    public void putLongVolatile(Object arg0, long arg1, long arg2) {
//        THEUNSAFE.putLongVolatile(arg0, arg1, arg2);
//    }
//    public void putObject(Object arg0, int arg1, Object arg2) {
//        THEUNSAFE.putObject(arg0, arg1, arg2);
//    }
//    public void putObject(Object arg0, long arg1, Object arg2) {
//        THEUNSAFE.putObject(arg0, arg1, arg2);
//    }
//    public void putObjectVolatile(Object arg0, long arg1, Object arg2) {
//        THEUNSAFE.putObjectVolatile(arg0, arg1, arg2);
//    }
//    public void putShort(long arg0, short arg1) {
//        THEUNSAFE.putShort(arg0, arg1);
//    }
//    public void putShort(Object arg0, int arg1, short arg2) {
//        THEUNSAFE.putShort(arg0, arg1, arg2);
//    }
//    public void putShort(Object arg0, long arg1, short arg2) {
//        THEUNSAFE.putShort(arg0, arg1, arg2);
//    }
//    public void putShortVolatile(Object arg0, long arg1, short arg2) {
//        THEUNSAFE.putShortVolatile(arg0, arg1, arg2);
//    }
//    public long reallocateMemory(long arg0, long arg1) {
//        return THEUNSAFE.reallocateMemory(arg0, arg1);
//    }
//    public void setMemory(long arg0, long arg1, byte arg2) {
//        THEUNSAFE.setMemory(arg0, arg1, arg2);
//    }
//    public Object staticFieldBase(Class arg0) {
//        return THEUNSAFE.staticFieldBase(arg0);
//    }
//    public Object staticFieldBase(Field arg0) {
//        return THEUNSAFE.staticFieldBase(arg0);
//    }
//    public long staticFieldOffset(Field arg0) {
//        return THEUNSAFE.staticFieldOffset(arg0);
//    }
//    public void throwException(Throwable arg0) {
//        THEUNSAFE.throwException(arg0);
//    }
//    public String toString() {
//        return THEUNSAFE.toString();
//    }
//    public void unpark(Object arg0) {
//        THEUNSAFE.unpark(arg0);
//    }
}
