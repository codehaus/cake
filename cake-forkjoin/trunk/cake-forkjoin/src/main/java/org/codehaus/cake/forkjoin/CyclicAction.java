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

/**
 * A computation that is broken into a series of task executions, each
 * separated by a TaskBarrier arrival.  Concrete subclasses must
 * define method <tt>compute</tt>, that performs the action occurring
 * at each step of the barrier.  Upon invocation of this task, the
 * <tt>compute</tt> method is repeatedly invoked until the barrier
 * <tt>isTerminated</tt> or until its execution throws an exception.
 *
 * <p> <b>Sample Usage.</b> Here is a sketch of a set of CyclicActions
 * that each perform 500 iterations of an imagined image smoothing
 * operation. Note that the aggregate ImageSmoother task itself is not
 * a CyclicTask.
 * 
 * <pre>
 * class ImageSmoother extends RecursiveAction {
 *   protected void compute() {
 *     TaskBarrier b = new TaskBarrier() {
 *       protected boolean terminate(int cycle, int registeredParties) {
 *          return registeredParties &lt;= 0 || cycle &gt;= 500;
 *       }
 *     }
 *     int n = pool.getPoolSize();
 *     CyclicAction[] actions = new CyclicAction[n];
 *     for (int i = 0; i &lt; n; ++i) {
 *       action[i] = new CyclicAction(b) {
 *         protected void compute() {
 *           smoothImagePart(i);
 *         }
 *       }
 *     }
 *     for (int i = 0; i &lt; n; ++i) 
 *       actions[i].fork();
 *     for (int i = 0; i &lt; n; ++i) 
 *       actions[i].join();
 *   }
 * }
 * </pre>
 */
public abstract class CyclicAction extends ForkJoinTask<Void> {
    final TaskBarrier barrier;
    int phase = -1;

    /**
     * Constructs a new CyclicAction using the supplied barrier,
     * registering for this barrier upon construction.
     * @param barrier the barrier
     */
    public CyclicAction(TaskBarrier barrier) {
        this.barrier = barrier;
        barrier.register();
    }

    /**
     * The computation performed by this task on each cycle of the
     * barrier.  While you must define this method, you should not in
     * general call it directly.
     */
    protected abstract void compute();

    /**
     * Returns the barrier
     */
    public final TaskBarrier getBarrier() { 
        return barrier; 
    }

    /**
     * Returns the current cycle of the barrier
     */
    public final int getCycle() { 
        return barrier.getCycle(); 
    }

    /**
     * Always returns null.
     * @return null
     */
    public final Void rawResult() { 
        return null; 
    }

    public final Throwable exec() {
        TaskBarrier b = barrier;
        if (isDone()) {
            b.arriveAndDeregister();
            return getException();
        }
        if (phase < 0)
            phase = b.getCycle();
        else
            phase = b.awaitCycleAdvance(phase);
        if (phase < 0)
            return setDone();
        try {
            compute();
        } catch (Throwable rex) {
            b.arriveAndDeregister();
            return setDoneExceptionally(rex);
        }
        b.arrive();
        this.fork();
        return null;
    }


    public final Void forkJoin() {
        exec();
        return join();
    }

    /**
     * Equivalent to <tt>finish(null)</tt>.
     */
    public final void finish() { 
        setDone();
    }

    public final void finish(Void result) { 
        setDone();
    }

    public final void finishExceptionally(Throwable ex) {
        setDoneExceptionally(ex);
    }

}
