package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.cake.internal.codegen.ClassEmitter;
import org.junit.Test;

public class ClassEmitterHeaderTest extends AbstractClassEmitterTest {

    @Test
    public void simpel() throws Exception {
        emitter = new ClassEmitter() {
            public void defineHeader() {
                withClass().setPublic().create(anyName());
            }
        };
        Class c = generate();
        assertEquals(1, c.getConstructors().length);
        Object o = c.newInstance();
    }

    @Test
    public void superClass() throws Exception {
        emitter = new ClassEmitter() {
            public void defineHeader() {
                withClass().setPublic().setSuper(Object.class).create(anyName());
            }
        };
        assertTrue(generate().newInstance() instanceof Object);
    }

    @Test
    public void superClassDate() throws Exception {
        emitter = new ClassEmitter() {
            public void defineHeader() {
                withClass().setPublic().setSuper(type(Date.class)).create(anyName());
            }
        };
        assertTrue(generate().newInstance() instanceof Date);
    }

    @Test
    public void interfaces() throws Exception {
        emitter = new ClassEmitter() {
            public void defineHeader() {
                withClass().setPublic().addInterfaces(Serializable.class).create(anyName());
            }
        };
        assertTrue(generate().newInstance() instanceof Serializable);
    }
}
