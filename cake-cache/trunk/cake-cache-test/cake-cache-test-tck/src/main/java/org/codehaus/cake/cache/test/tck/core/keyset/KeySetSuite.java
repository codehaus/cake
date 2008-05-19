/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
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
