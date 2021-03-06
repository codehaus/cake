/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.service.executor;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.junit.Test;

public class ExecutorsConfigurationTest {
    @Test
    public void domain() {
        ExecutorsManager em = new DummyExecutorsManager() {};
        ExecutorsConfiguration emb = new ExecutorsConfiguration();
        assertNull(emb.getExecutorManager());
        assertSame(emb, emb.setExecutorManager(em));
        assertSame(em, emb.getExecutorManager());
    }

    static class DummyExecutorsManager extends ExecutorsManager {

        public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
            return null;
        }

        public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
            return null;
        }

        public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
            return null;
        }

    }
}
