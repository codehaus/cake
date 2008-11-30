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
package org.codehaus.cake.cache.test.operations;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;


/**
 * Used to invoke operations on {@link CacheLoadingService}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class LoadingOps {

    static final String PREFIX = "test.opr.loading.";

    public static class Load<K, V> extends AbstractOperation<K, V> {
        public static final String NAME = PREFIX + "load";

        /** {@inheritDoc} */
        public void run() {
            cache.with().loading().load(keyGenerator.op());
        }
    }

    /**
     * Invokes the {@link CacheLoadingService#forceLoad(Object)} method.
     */
    public static class ForceLoad<K, V> extends AbstractOperation<K, V> {
        /** The name of this operation. */
        public static final String NAME = PREFIX + "forceLoad";
        /** {@inheritDoc} */
        public void run() {
            cache.with().loadingForced().load(keyGenerator.op());
        }
    }
}
