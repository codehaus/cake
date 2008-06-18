/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite consisting of all loading service test classes.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LoadingSuite.java 522 2007-12-24 11:24:35Z kasper $
 */
@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { MemoryStoreCachingDisabled.class, MemoryStoreEvictor.class, MemoryStoreGeneral.class,
        MemoryStoreIsCacheable.class, MemoryStoreManagement.class, MemoryStoreReplacementPolicy.class,
        MemoryStoreReplacementPolicyAttributes.class, MemoryStoreReplacementPolicyLRU.class,
        MemoryStoreReplacementPolicyNone.class, MemoryStoreTrim.class, MemoryStoreTrimComparator.class,
        MemoryStoreVolume.class })
public class SuiteMemoryStore {}
