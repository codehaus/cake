/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.keys;

import java.util.Collection;

public interface KeyCollectionGenerator<K> {
    Collection<? extends K> nextKeys();

}
