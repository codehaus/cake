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


/**
 * This class is used to configure the executor manager service prior to usage.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheWorkerConfiguration.java 537 2007-12-30 19:21:20Z kasper $
 * @see ExecutorsService
 */
public class ExecutorsConfiguration {

    /** The executor manager to use. */
    private ExecutorsManager executeManager;

    /**
     * Returns the {@link ExecutorsManager} or <code>null</code> if no executor manager
     * has been set.
     * 
     * @return the {@link ExecutorsManager} or <code>null</code> if no executor manager
     *         has been set
     * @see #setExecutorManager(ExecutorsManager)
     */
    public ExecutorsManager getExecutorManager() {
        return executeManager;
    }

    /**
     * Sets the ExecutorManager that should be used. If no ExecutorManager is set one will
     * be created automatically if needed.
     * 
     * @param executeManager
     *            the executor manager to use
     * @return this configuration
     * @see #getExecutorManager()
     */
    public ExecutorsConfiguration setExecutorManager(ExecutorsManager executeManager) {
        this.executeManager = executeManager;
        return this;
    }
}
