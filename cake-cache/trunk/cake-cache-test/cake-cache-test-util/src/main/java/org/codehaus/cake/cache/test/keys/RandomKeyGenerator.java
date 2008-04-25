/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.keys;

import org.codehaus.cake.ops.Ops.Generator;
import org.codehaus.cake.test.util.LoopHelpers;

public class RandomKeyGenerator implements Generator<Integer> {

    final LoopHelpers.SimpleRandom rng = new LoopHelpers.SimpleRandom();

    public Integer op() {
        return rng.next() % 1000;
    }
}
