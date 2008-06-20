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
package org.codehaus.cake.cache.test.tck.core.values;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { Values.class, ValuesAdd.class, ValuesClear.class, ValuesContains.class,
        ValuesHashCodeEquals.class, ValuesIsEmpty.class, ValuesIterator.class, ValuesRemove.class, ValuesRetain.class,
        ValuesSize.class, ValuesToArray.class, ValuesToString.class })
public class ValuesSuite {

}
