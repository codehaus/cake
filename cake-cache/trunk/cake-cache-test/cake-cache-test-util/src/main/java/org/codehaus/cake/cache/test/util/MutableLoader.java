/* Copyright 2004 - 2007 Kasper Nielsen <kasper@codehaus.org> Licensed under
 * the Apache 2.0 License, see http://coconut.codehaus.org/license.
 */

package org.codehaus.cake.cache.test.util;

import java.util.concurrent.atomic.AtomicReference;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

/**
 * A simple cache loader used for testing. Will return 1->A, 2->B, 3->C, 4->D, 5->E and
 * <code>null</code> for any other key.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: MutableLoader.java 544 2008-01-05 01:19:03Z kasper $
 */
public class MutableLoader implements SimpleCacheLoader<Integer, String> {

    private final AtomicReference<String> ref = new AtomicReference<String>();

    private volatile Integer lastKey;

    private volatile AttributeMap lastAttributeMap;

    public String load(Integer key, AttributeMap attributes) {
        this.lastKey = key;
        this.lastAttributeMap = attributes;
        return ref.get();
    }

    public void setLoadNext(String loadNext) {
        ref.set(loadNext);
    }

    public Integer getLastKey() {
        return lastKey;
    }

    public AttributeMap getLastAttributeMap() {
        return lastAttributeMap;
    }
}
