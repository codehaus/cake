/* Copyright 2004 - 2007 Kasper Nielsen <kasper@codehaus.org> Licensed under
 * the Apache 2.0 License, see http://coconut.codehaus.org/license.
 */
package org.codehaus.cake.internal.cache.service.entry;

public interface InternalCacheEntryService<K, V> {

    boolean isDisabled();

    void setDisabled(boolean isDisabled);
}
