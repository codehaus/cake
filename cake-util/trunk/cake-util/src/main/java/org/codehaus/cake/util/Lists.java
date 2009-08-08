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
package org.codehaus.cake.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    /**
     * Creates a new list with the specified element as the first element in the list, and the elements in the specified
     * list as the following elements.
     * 
     * @param <E>
     * @param element
     * @param appendLast
     * @return
     */
    public static <E> List<E> join(E element, List<E> appendLast) {
        ArrayList<E> list = new ArrayList<E>();
        list.add(element);
        list.addAll(appendLast);
        return list;
    }
}
