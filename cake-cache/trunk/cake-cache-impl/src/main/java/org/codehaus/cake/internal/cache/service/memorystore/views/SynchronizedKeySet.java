/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;

import org.codehaus.cake.cache.Cache;

final class SynchronizedKeySet<K, V> extends KeySet<K, V> {
    private final Object mutex;

    SynchronizedKeySet(Object mutex, Cache<K, V> cache) {
        super(cache);
        this.mutex = mutex;
    }

    public boolean containsAll(Collection<?> c) {
        synchronized (mutex) {
            return super.containsAll(c);
        }
    }

    @Override
    public boolean equals(Object o) {
        synchronized (mutex) {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        synchronized (mutex) {
            return super.hashCode();
        }
    }

    @Override
    public Object[] toArray() {
        synchronized (mutex) {
            return super.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (mutex) {
            return super.toArray(a);
        }
    }

    public boolean removeAll(Collection<?> c) {
        synchronized (mutex) {
            return super.removeAll(c);
        }
    }

    public boolean retainAll(Collection<?> c) {
        synchronized (mutex) {
            return super.retainAll(c);
        }
    }

    @Override
    public String toString() {
        synchronized (mutex) {
            return super.toString();
        }
    }
}
