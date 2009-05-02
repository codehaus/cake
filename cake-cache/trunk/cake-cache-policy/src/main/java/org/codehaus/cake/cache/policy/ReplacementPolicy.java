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
package org.codehaus.cake.cache.policy;

/**
 * A (cache) replacement policy determines which data item(s) should be evicted (deleted) from the cache when the free
 * space is insufficient for accommodating a new item to be cached. Normally users should not need to implement this
 * interface, only if they want to implement a custom replacement polices.
 * <p>
 * A replacement policy does not control when or how many elements should be evicted only which element should be
 * evicted the next time, based on insert and access patterns.
 * <p>
 * All general-purpose replacement policy implementation classes should provide atleast one of the two following
 * constructors: a void (no arguments) constructor, and a constructor with a single argument of type
 * {@link PolicyContext}, this context can be used to attach data that the replacement policy needs to each element.
 * This will, for most cases, have a lower memory footprint then if the replacement policy needed to keep this data 
 * separately in a list or map that needs to looked up each time.
 * <p>
 * Normally instances of this interface are used together with a {@link org.codehaus.cake.cache.Cache}, but most of the
 * implementations in this package are generally purpose replacement policies that can easily be used within another
 * context.
 * <p>
 * This library comes with a number of predefined replacement policies, see
 * {@link org.codehaus.cake.cache.policy.Policies} for the most commonly used policies.
 * <p>
 * For performance reasons instances of ReplacementPolicy are not expected to be thread-safe. Instead, the cache using
 * the ReplacementPolicy instance must maintain thread safety.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id$
 * @param <T>
 *            the type of elements being cached
 */
public interface ReplacementPolicy<T> {

    /**
     * A cache calls this method whenever a new element is first inserted to a cache.
     * 
     * @param element
     *            the element to add to this policy
     */
    void add(T element);

    /**
     * A cache calls this method whenever it removes references to all elements that are cached. Calling this method
     * should have the same effect as calling {@link #remove(Object)} for each individual element added to the policy.
     * However, removing all elements in one operation is most likely faster.
     */
    void clear();

    /**
     * Called by the cache when insufficient space is available for a new element to be added to the cache. This method
     * should return the element that should be evicted next accordingly to the replacement policy that is implemented.
     * <p>
     * Ir order to avoid memory leaks it is important that all references to the returned element is removed from the
     * policy.
     * 
     * @return the element that should be evicted from the cache or <code>null</code> if the policy does not contain
     *         any elements
     */
    T evictNext();

    /**
     * Called whenever an element is removed by an external action in the cache. For example, if the user chooses to
     * explicitly remove the element from the cache.
     * <p>
     * Ir order to avoid memory leaks it is important that all references to the returned element is removed from the
     * policy.
     * 
     * @param element
     *            the element that was removed
     */
    void remove(T element);

    /**
     * The specified <tt>previous</tt> element was updated with a new value§.
     * <p>
     * In order to avoid memory leaks it is important that all references to the previous element is removed from the
     * policy.
     * 
     * @see #add(Object)
     */
    void replace(T oldElement, T newElement);

    /**
     * Called by the cache whenever an element is accessed.
     * <p>
     * If this replacement policy is used from a {@link org.codehaus.cake.cache.Cache}. An entry is accessed whenever
     * {@link org.codehaus.cake.cache.Cache#get(Object)}, {@link org.codehaus.cake.cache.Cache#getEntry(Object)},
     * {@link org.codehaus.cake.cache.Cache#getAllOld(java.util.Collection)} or any of the get methods in
     * {@link org.codehaus.cake.cache.service.crud.CrudReader} are called. Accessing an entry while using an iterator
     * returned by {@link org.codehaus.cake.cache.Cache#entrySet()}, {@link org.codehaus.cake.cache.Cache#keySet()} or
     * {@link org.codehaus.cake.cache.Cache#values()} will not result in this method being invoked. Neither will entries
     * accessed via any of the views defined in org.codehaus.cake.cache.view.
     * 
     * @param element
     *            the element that was accessed
     */
    void touch(T element);
}
