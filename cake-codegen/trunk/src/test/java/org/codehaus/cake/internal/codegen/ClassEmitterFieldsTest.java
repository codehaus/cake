package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Date;

import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassEmitter;
import org.junit.Test;

public class ClassEmitterFieldsTest extends AbstractClassEmitterTest {

    @Test
    public void simpelPublic() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().create(anyName());
                withFieldPublic("foo1").createInt();
            }
        };
        Class c = generate();
        assertEquals(1, c.getFields().length);
        assertSame(Integer.TYPE, c.getField("foo1").getType());
        Object o = c.newInstance();
    }

    @Test
    public void staticField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().create(anyName());
                withFieldPublic("foo1").createInt();
                withFieldPublic("foo2").setStatic().createInt();
            }
        };
        Class c = generate();
        assertFalse(Modifier.isStatic(c.getField("foo1").getModifiers()));
        assertTrue(Modifier.isStatic(c.getField("foo2").getModifiers()));
    }

    @Test
    public void finalField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().create(anyName());
                withFieldPublic("foo1").createInt();
                withFieldPublic("foo2").setFinal().createInt();
            }
        };
        Class c = generate();
        assertFalse(Modifier.isFinal(c.getField("foo1").getModifiers()));
        assertTrue(Modifier.isFinal(c.getField("foo2").getModifiers()));
    }

    @Test
    public void volatileField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().create(anyName());
                withFieldPublic("foo1").createInt();
                withFieldPublic("foo2").setVolatile().createInt();
            }
        };
        Class c = generate();
        assertFalse(Modifier.isVolatile(c.getField("foo1").getModifiers()));
        assertTrue(Modifier.isVolatile(c.getField("foo2").getModifiers()));
    }

    @Test
    public void typeOf() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().create(anyName());
                withFieldPublic("foo1").createBoolean();
                withFieldPublic("foo2").createByte();
                withFieldPublic("foo3").createChar();
                withFieldPublic("foo4").createDouble();
                withFieldPublic("foo5").createFloat();
                withFieldPublic("foo6").createInt();
                withFieldPublic("foo7").createLong();
                withFieldPublic("foo8").createShort();
                withFieldPublic("foo9").create(Date.class);
                withFieldPublic("foo10").create(Type.getType(Integer.class));
                withFieldPublic("foo11").create(Type.getType(Object.class));
            }
        };
        Class c = generate();
        assertEquals(11, c.getFields().length);
        assertSame(Boolean.TYPE, c.getField("foo1").getType());
        assertSame(Byte.TYPE, c.getField("foo2").getType());
        assertSame(Character.TYPE, c.getField("foo3").getType());
        assertSame(Double.TYPE, c.getField("foo4").getType());
        assertSame(Float.TYPE, c.getField("foo5").getType());
        assertSame(Integer.TYPE, c.getField("foo6").getType());
        assertSame(Long.TYPE, c.getField("foo7").getType());
        assertSame(Short.TYPE, c.getField("foo8").getType());
        assertSame(Date.class, c.getField("foo9").getType());
        assertSame(Integer.class, c.getField("foo10").getType());
        assertSame(Object.class, c.getField("foo11").getType());
        Object o = c.newInstance();
    }
}
