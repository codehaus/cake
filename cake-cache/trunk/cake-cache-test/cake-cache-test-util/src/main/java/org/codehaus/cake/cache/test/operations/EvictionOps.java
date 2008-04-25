/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.operations;

import org.codehaus.cake.ops.Ops;

public abstract class EvictionOps {
  //  @HarnessTest("eviction.trimToSize")
    public static class TrimToSize<K, V> implements Ops.Procedure<CacheHarnessContext<K, V>> {
        /** {@inheritDoc} */
        public void op(CacheHarnessContext<K, V> context) {
            int size = context.cache().size();
            context.eviction().trimToSize(context.randomInt(size));
        }
    }

}
