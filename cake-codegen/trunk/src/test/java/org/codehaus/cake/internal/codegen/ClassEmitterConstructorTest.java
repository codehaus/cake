package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class ClassEmitterConstructorTest extends AbstractClassEmitterTest {

    @Test
    public void defaultConstructor() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().create(anyName());
            }
        };
        Class c = generate();
        // assertEquals(1, c.getFields().length);
        // assertSame(Integer.TYPE, c.getField("foo1").getType());
        // Object o = c.newInstance();
    }

    @Test
    public void delegatingConstructor() throws Exception {
        emitter = new TestEmitter(Date.class) {
            {
                withConstructor().setPublic().createDelegating(Long.TYPE);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(Long.TYPE).newInstance(123L);
        assertTrue(o instanceof Date);
        assertEquals(123, ((Date) o).getTime());
    }

    /**
     * Delegate the first of the arguments (long) to the parent constructor.
     */
    @Test
    public void delegatingConstructorPartly() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().setSuper(Date.class).create(anyName());
                withConstructor().setPublic().create(Long.TYPE, String.class).loadAndInvokeSuperWithArgs(1);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(Long.TYPE, String.class).newInstance(123, "ignore");
        assertTrue(o instanceof Date);
        assertEquals(123, ((Date) o).getTime());
    }

    /**
     * Assigns the constructor argument to a field.
     * 
     * <pre>
     * public class Test3 {
     *     public int foo;
     * 
     *     public Test3(int foo) {
     *         this.foo = foo;
     *     }
     * }
     * </pre>
     */
    @Test
    public void constructorAndAssignIntField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().setSuper(Object.class).create(anyName());
                withFieldPublic("foo").createInt();
                withConstructor().setPublic().create(Integer.TYPE).invokeEmptySuper().putFieldArg("foo", 0);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(Integer.TYPE).newInstance(321);

        assertEquals(321, c.getField("foo").getInt(o));
    }

    /**
     * Assigns the constructor argument to a field.
     * 
     * <pre>
     * public class Test3 {
     *     public String foo;
     * 
     *     public Test3(String foo) {
     *         this.foo = foo;
     *     }
     * }
     * </pre>
     */
    @Test
    public void constructorAndAssignStringField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().setSuper(Object.class).create(anyName());
                withFieldPublic("foo").create(String.class);
                withConstructor().setPublic().create(String.class).invokeEmptySuper().putFieldArg("foo", 0);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(String.class).newInstance("hello");

        assertEquals("hello", c.getField("foo").get(o));
    }

    /**
     * Assigns the constructor argument to a field.
     * 
     * <pre>
     * public class Test3 {
     *     public Number foo;
     * 
     *     public Test3(Integer foo) {
     *         this.foo = foo;
     *     }
     * }
     * </pre>
     */
    @Test
    public void constructorAndAssignCastedNumberField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().setSuper(Object.class).create(anyName());
                withFieldPublic("foo").create(Number.class);
                withConstructor().setPublic().create(Integer.class).invokeEmptySuper().putFieldArg("foo", 0);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(Integer.class).newInstance(231);

        assertEquals(231, c.getField("foo").get(o));
    }

    /**
     * Assigns the constructor argument to a field.
     * 
     * <pre>
     * public class Test3 {
     *     public Integer foo;
     * 
     *     public Test3(int foo) {
     *         this.foo = foo;
     *     }
     * }
     * </pre>
     */
    @Test
    public void constructorAndAssignBoxed() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().setSuper(Object.class).create(anyName());
                withFieldPublic("foo").create(Integer.class);
                withConstructor().setPublic().create(Integer.TYPE).invokeEmptySuper().putFieldArg("foo", 0);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(Integer.TYPE).newInstance(231);

        assertEquals(231, c.getField("foo").get(o));
    }

    /**
     * Deletegate to subclass, Assigns the constructor argument to a field.
     * 
     * <pre>
     * public class Test3 extends java.util.Date {
     *     public String abc;
     *     public volatile Double d;
     * 
     *     public Test3(long foo, String abc, Double d) {
     *         super(foo);
     *         this.foo = foo;
     *     }
     * }
     * </pre>
     */
    @Test
    public void constructorDelegateAndAssignCastedNumberField() throws Exception {
        emitter = new ClassEmitter() {
            public void define() {
                withClass().setPublic().setSuper(Object.class).create(anyName());
                withFieldPublic("foo").create(Number.class);
                withConstructor().setPublic().create(Integer.class).invokeEmptySuper().putFieldArg("foo", 0);
            }
        };
        Class c = generate();
        Object o = c.getConstructor(Integer.class).newInstance(231);

        assertEquals(231, c.getField("foo").get(o));
    }
}
