/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.cache.attribute;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.AbstractCakeReplacementPolicy;
import org.codehaus.cake.cache.policy.AbstractCakeReplacementPolicy.ReadWriterGenerator;
import org.codehaus.cake.cache.policy.costsize.ReplaceCostliestPolicy;

/**
 * WARNING: This is an internal class. Ordinary users should never need to reference this class.
 * <p>
 * This interface contain various methods used by {@link AbstractCakeReplacementPolicy}.
 */
public interface InternalCacheAttributeService extends ReadWriterGenerator {
    /**
     * Attaches an attribute to the cache. An {@link AttributeMap} where the attribute can be set or read can later be
     * retrieved by calling {@link CacheEntry#getAttributes()}. <p/> NOTE: The value can only be read or set safely in
     * methods that are implicitly called by the cache. The methods include {@link ReplacementPolicy#add(CacheEntry)},
     * {@link ReplacementPolicy#clear()}, {@link ReplacementPolicy#evictNext()},
     * {@link ReplacementPolicy#remove(CacheEntry)}, {@link ReplacementPolicy#replace(CacheEntry, CacheEntry)} and
     * {@link ReplacementPolicy#touch(CacheEntry)}.
     * 
     * @param attribute
     *            the attribute to attach
     */

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link CacheAttributeConfiguration#add(Attribute...)} the attribute will be silently registered.
     * 
     * For example, LFU will depend on CacheEntry.HITS. This is a non-hard dependency because if the user has not
     * already defined it, it will just be added as a secret.
     * 
     * 
     * @param attribute
     *            the attribute to attach
     */
    void dependOnSoft(Attribute<?> attribute);

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link CacheAttributeConfiguration#add(Attribute...)} the cache will fail to startup. For example, it does not
     * make sense to use {@link ReplaceCostliestPolicy} if the user does not supply entries a {@link CacheEntry#COST}
     * attached.
     * 
     * @param attribute
     *            the attribute to attach
     */
    void dependOnHard(Attribute<?> attribute);
}
