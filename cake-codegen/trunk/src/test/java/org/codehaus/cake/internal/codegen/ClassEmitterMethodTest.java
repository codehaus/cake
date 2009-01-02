package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

import org.codehaus.cake.internal.codegen.TestHelper.IntToInt;
import org.junit.Test;

public class ClassEmitterMethodTest extends AbstractClassEmitterTest {

    @Test
    public void voidInvoke() throws Exception {
        emitter = new TestEmitter(Object.class, Runnable.class) {
            {
                withMethodPublic("run").create();
            }
        };
        assertTrue(newInstance() instanceof Runnable);
        ((Runnable) newInstance()).run();
    }

    @Test
    public void returnField() throws Exception {
        emitter = new TestEmitter(Object.class, Callable.class) {
            {
                withField("foo").setStatic().createString("test");
                withMethodPublic("call").setReturnType(Object.class).create().getStatic("foo");
            }
        };
        assertEquals("test", ((Callable) newInstance()).call());
    }

    @Test
    public void returnParameter() throws Exception {
        emitter = new TestEmitter(Object.class, IntToInt.class) {
            {
                withMethodPublic("get").setReturnType(Integer.TYPE).create(Integer.TYPE).loadArg(0);
            }
        };
        assertEquals(1234567, ((IntToInt) newInstance()).get(1234567));
    }
}
