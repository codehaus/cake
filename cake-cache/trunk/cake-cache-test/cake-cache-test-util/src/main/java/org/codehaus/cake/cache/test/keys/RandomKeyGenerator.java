/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.test.keys;

import org.codehaus.cake.test.util.LoopHelpers;
import org.codehaus.cake.util.ops.Ops.Generator;

public class RandomKeyGenerator implements Generator<Integer> {

    final LoopHelpers.SimpleRandom rng = new LoopHelpers.SimpleRandom();

    public Integer op() {
        return rng.next() % 1000;
    }
}
