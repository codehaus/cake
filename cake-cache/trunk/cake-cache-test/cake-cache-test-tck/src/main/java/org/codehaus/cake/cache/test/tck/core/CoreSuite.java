/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.test.tck.core.entryset.EntrySetSuite;
import org.codehaus.cake.cache.test.tck.core.keyset.KeySetSuite;
import org.codehaus.cake.cache.test.tck.core.values.ValuesSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { Clear.class, Constructors.class, ContainsKey.class, ContainsValue.class,
        EntrySetSuite.class, EqualsHashcode.class, Get.class, GetAll.class, GetEntry.class,
        IsEmpty.class, KeySetSuite.class, Lifecycle.class, Peek.class, PeekEntry.class, Put.class,
        PutAll.class, PutIfAbsent.class, Remove.class, RemoveAll.class, Replace.class, Size.class,
        ToString.class, ValuesSuite.class })
public class CoreSuite {

}
