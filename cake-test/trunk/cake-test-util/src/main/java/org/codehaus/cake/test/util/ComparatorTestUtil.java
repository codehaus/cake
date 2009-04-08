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
package org.codehaus.cake.test.util;

import java.io.Serializable;
import java.util.Comparator;

public class ComparatorTestUtil {
    public static final class Dummy implements Serializable {
        public static final Dummy D1 = new Dummy(1);

        public static final Dummy D2 = new Dummy(2);

        public static final Dummy D3 = new Dummy(3);

        public static final Dummy D4 = new Dummy(4);

        public static final Dummy D5 = new Dummy(5);

        final int i;

        private Dummy(int i) {
            this.i = i;
        }
    }

    public static final class DummyComparator implements Comparator<Dummy>, Serializable {
        public int compare(Dummy o1, Dummy o2) {
            return (o1.i < o2.i ? -1 : (o1.i == o2.i ? 0 : 1));
        }
    }
}
