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
package org.codehaus.cake.cache.test.tck.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { Clear.class, Constructors.class, ContainsKey.class, ContainsValue.class, 
        EqualsHashcode.class, Get.class, GetAll.class, GetEntry.class, IsEmpty.class, 
        Lifecycle.class, Peek.class, PeekEntry.class, Put.class, PutAll.class, PutIfAbsent.class, Remove.class,
        Replace.class, Size.class, ToString.class, WithServices.class })
public class CoreSuite {}
