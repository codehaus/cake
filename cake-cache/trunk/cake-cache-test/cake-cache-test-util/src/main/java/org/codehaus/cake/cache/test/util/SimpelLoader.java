/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.util;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class SimpelLoader implements SimpleCacheLoader {
    public Object load(Object key, AttributeMap attributes) throws Exception {
        return key;
    }

}
