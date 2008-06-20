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

/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */

package org.codehaus.cake.forkjoin;
import java.util.concurrent.*;

/**
 * An object that executes {@link ForkJoinTask} computations.  This
 * interface does not expose lifecycle, status, or management methods
 * corresponding to implementations, so may be useful as a basis
 * for classes that must restrict access to such methods.
 *
 */
public interface ForkJoinExecutor {
    /**
     * Arranges for (asynchronous) execution of the given task.
     * @param task the task
     * @throws NullPointerException if task is null
     * @throws RejectedExecutionException if the executor is
     * not in a state that allows execution.
     */
    public <T> void execute(ForkJoinTask<T> task);

    /**
     * Performs the given task; returning its result upon completion
     * @param task the task
     * @return the task's result
     * @throws NullPointerException if task is null
     * @throws RejectedExecutionException if the executor is
     * not in a state that allows execution.
     */
    public <T> T invoke(ForkJoinTask<T> task);

    /**
     * Arranges for (asynchronous) execution of the given task,
     * returning a <tt>Future</tt> that may be used to obtain results
     * upon completion.
     * @param task the task
     * @return a Future that can be used to get the task's results.
     * @throws NullPointerException if task is null
     * @throws RejectedExecutionException if the executor is
     * not in a state that allows execution.
     */
    public <T> Future<T> submit(ForkJoinTask<T> task);

    /**
     * Returns an estimate of how many tasks (including subtasks)
     * may execute at once. This value normally corresponds to the
     * number of threads available for executing tasks by this
     * executor.
     * @return the parallelism level
     */
    public int getParallelismLevel();
}

