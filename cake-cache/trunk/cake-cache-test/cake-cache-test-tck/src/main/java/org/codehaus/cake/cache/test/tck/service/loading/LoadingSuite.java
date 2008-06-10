/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Predicate;
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
@Suite.SuiteClasses( { ExplicitNeedsReload.class, ImplicitLoading.class, Load.class, LoadAll.class,
        LoadAllIterable.class, LoadAllIterableForced.class, LoadAllMap.class, LoadForced.class, LoadingErroneous.class,
        LoadingLifecycle.class, LoadingManagement.class, LoadingNPE.class, LoadingService.class })
public class LoadingSuite {

    static class VolatilePredicate implements Predicate<CacheEntry<Integer, String>> {
        volatile boolean result;
        CacheEntry<Integer, String> latest;

        public boolean op(CacheEntry<Integer, String> a) {
            latest = a;
            return result && (a.getKey() % 2 == 1);
        }
    }

}
