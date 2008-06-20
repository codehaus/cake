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
package org.codehaus.cake.cache.test.tck.core.keyset;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: KeySetSuite.java 489 2007-11-29 11:35:31Z kasper $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { KeySet.class, KeySetAdd.class, KeySetClear.class, KeySetContains.class,
        KeySetHashCodeEquals.class, KeySetIsEmpty.class, KeySetIterator.class, KeySetRemove.class, KeySetRetain.class,
        KeySetSize.class, KeySetToArray.class, KeySetToString.class })
public class KeySetSuite {

}
