package org.codehaus.cake.internal.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ClassEmitterThrowablesTest extends AbstractClassEmitterTest {

    @Test
    public void voidInvoke() throws Exception {
        emitter = new TestEmitter(Object.class, Runnable.class) {
            {
                withMethodPublic("run").create().throwUnsupportedOperationException("qwe");
            }
        };
        try {
            ((Runnable) newInstance()).run();
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("qwe", e.getMessage());
        }
    }
}
