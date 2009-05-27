package org.codehaus.cake.internal.cache;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.util.ops.Ops.Procedure;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class RunAfterCacheStartProcedureTest {

    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    @Test(expected = NullPointerException.class)
    public void npe() {
        new RunAfterCacheStartProcedure(null);
    }

    @Test
    public void testRun() throws Exception {
        final Cache cache = dummy(Cache.class);
        final Procedure p = context.mock(Procedure.class);
        context.checking(new Expectations() {
            {
                one(p).op(cache);
            }
        });
        RunAfterCacheStartProcedure run = new RunAfterCacheStartProcedure(p);
        for (Method m : run.getClass().getMethods()) {
            RunAfter ra=m.getAnnotation(RunAfter.class);
            
            if (ra!=null && Arrays.asList(ra.value()).contains(State.RUNNING)) {
                m.invoke(run, cache);
            }
        }
    }
}
