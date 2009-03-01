package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertEquals;

import org.codehaus.cake.internal.asm.Type;
import org.junit.Test;

public class ClassEmitterStaticInitializerTest extends AbstractClassEmitterTest {
    @Test
    public void assignInt() throws Exception {
        emitter = new TestEmitter() {
            {
                withFieldPublic("test").setStatic().createInt();
                withStaticInitializer().putStaticConst("test", 123);
            }
        };
        assertEquals(123, generate().getField("test").get(null));
    }

    @Test
    public void assignString() throws Exception {
        emitter = new TestEmitter() {
            {
                withFieldPublic("test").setStatic().createString();
                withStaticInitializer().putStaticConst("test", "ok");
            }
        };
        assertEquals("ok", generate().getField("test").get(null));
    }

    @Test
    public void assignIntWithLocalVariable() throws Exception {
        emitter = new TestEmitter() {
            {
                // public static int foo;
                // static {
                // int i = 5;
                // foo = i;
                // }
                withFieldPublic("test").setStatic().createInt();
                StaticInitializer si = withStaticInitializer();
                si.storeLocalConst(0, Type.INT_TYPE, 123);
                si.loadLocal(0);
                si.putStatic("test");
            }
        };
        assertEquals(123, generate().getField("test").get(null));
    }

    @Test
    public void assignFromIntArray() throws Exception {
        emitter = new TestEmitter() {
            {
                // public static int foo;
                // static {
                // int[] ints = TestHelper.getInts();
                // foo = ints[0];
                // }
                withFieldPublic("test").setStatic().createInt();
                StaticInitializer si = withStaticInitializer();
                si.invokeStaticAndStore(0, TestHelper.class, "getInts", (new int[0]).getClass());
                si.arrayLoadLocal(0, 0);
                si.putStatic("test");
            }
        };
        assertEquals(9, generate().getField("test").get(null));
    }

    @Test
    public void assignManyFromIntArray() throws Exception {
        emitter = new TestEmitter() {
            {
                // public static int foo1;
                // public static int foo2;
                // public static int foo3;
                // static {
                // int[] ints = TestHelper.getInts();
                // foo1 = ints[0];
                // foo2 = ints[1];
                // foo3 = ints[2];
                // }
                withFieldPublic("foo1").setStatic().createInt();
                withFieldPublic("foo2").setStatic().createInt();
                withFieldPublic("foo3").setStatic().createInt();
                StaticInitializer si = withStaticInitializer();
                si.invokeStaticAndStore(0, TestHelper.class, "getInts", (new int[0]).getClass());
                si.arrayLoadLocal(0, 0);
                si.putStatic("foo1");
                si.arrayLoadLocal(0, 1);
                si.putStatic("foo2");
                si.arrayLoadLocal(0, 2);
                si.putStatic("foo3");
            }
        };
        assertEquals(9, generate().getField("foo1").get(null));
        assertEquals(10, generate().getField("foo2").get(null));
        assertEquals(11, generate().getField("foo3").get(null));
    }

    @Test
    public void assignFromArrayAndCast() throws Exception {
        emitter = new TestEmitter() {
            {
                // public static String foo;
                // static {
                // Object[] o = TestHelper.getString("test");
                // foo = (String) o[0];
                // }
                withFieldPublic("foo").setStatic().createString();
                StaticInitializer si = withStaticInitializer();
                si.pushConst("test");
                si.invokeStaticAndStore(0, TestHelper.class, "getString", (new Object[0]).getClass(), String.class);
                si.arrayLoadLocal(0, 0);
                si.checkCast(String.class);
                si.putStatic("foo");
            }
        };
        assertEquals("testok", generate().getField("foo").get(null));
    }
}
