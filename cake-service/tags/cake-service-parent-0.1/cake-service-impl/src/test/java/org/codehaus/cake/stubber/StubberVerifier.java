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
package org.codehaus.cake.stubber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

public class StubberVerifier {

    private static final int STEP_0_UNINITIALIZE = 1 << 0;

    private static final int STEP_1_COMPOSED = 1 << 1;

    private static final int STEP_2_INITIALIZE = 1 << 2;

    private static final int STEP_3_START = 1 << 3;

    private static final int STEP_4_MANAGED = 1 << 4;

    private static final int STEP_5_STARTED = 1 << 5;

    private static final int STEP_6_SHUTDOWN = 1 << 6;

    private static final int STEP_7_SHUTDOWNNOW = 1 << 7;

    private static final int STEP_8_TERMINATE = 1 << 8;

    private static final int STEP_9_NONE = 1 << 9;

    private final Set happenings = new HashSet();
    int step = STEP_0_UNINITIALIZE;

    public synchronized void constructor(Object happening) {
        assertFalse(happenings.contains(happening));
        assertTrue(step == STEP_0_UNINITIALIZE);
        happenings.add(happening);
    }

    public synchronized void hasHappenend(Object happening) {
        assertTrue(happening.toString(), happenings.contains(happening));
    }
}
