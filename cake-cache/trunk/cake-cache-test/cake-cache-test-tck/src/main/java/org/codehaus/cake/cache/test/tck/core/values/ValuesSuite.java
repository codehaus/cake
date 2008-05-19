/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.values;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { Values.class, ValuesAdd.class, ValuesClear.class, ValuesContains.class,
        ValuesHashCodeEquals.class, ValuesIsEmpty.class, ValuesIterator.class, ValuesRemove.class, ValuesRetain.class,
        ValuesSize.class, ValuesToArray.class, ValuesToString.class })
public class ValuesSuite {

}
