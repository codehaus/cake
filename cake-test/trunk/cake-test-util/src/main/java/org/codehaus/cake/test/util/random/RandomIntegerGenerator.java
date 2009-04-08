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
package org.codehaus.cake.test.util.random;

import org.codehaus.cake.test.util.LoopHelpers;

public class RandomIntegerGenerator {

    private final int start;

    private final int diff;

    private final LoopHelpers.SimpleRandom rng = new LoopHelpers.SimpleRandom();

    RandomIntegerGenerator(int startInclusive, int endExclusive) {
        this.start = startInclusive;
        this.diff = endExclusive - startInclusive;
    }

    public Integer nextKey() {
        return start + rng.nextInt(diff);
    }

    public static void main(String[] args) {
        RandomIntegerGenerator rig = new RandomIntegerGenerator(1, 4);
        for (int i = 0; i < 10; i++) {
            System.out.println(rig.nextKey());
        }
    }

}
