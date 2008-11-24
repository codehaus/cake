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
package org.codehaus.cake.cache.service.attribute;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;

/**
 * This configurator is used for registering which attributes the cache will attach to each {@link CacheEntry}. The
 * attributes can be some of the attributes defined in {@link CacheEntry} which are handled specially by the cache. For
 * example, the {@link CacheEntry#TIME_CREATED} attribute which will make sure the cache records the insertion time of
 * all entries. The following example, shows how to configure the cache to use this attribute, and how to get a hold of
 * the creation time of the entry
 * 
 * <pre>
 * CacheConfiguration&lt;Integer, String&gt; conf = CacheConfiguration.newConfiguration();
 * conf.withAttributes().add(CacheEntry.TIME_CREATED);
 * Cache&lt;Integer, String&gt; cache = SynchronizedCache.from(conf);
 * // inserting an entry and getting hold of the creatation time
 * cache.put(1, &quot;hello&quot;);
 * CacheEntry&lt;Integer, String&gt; e = cache.getEntry(1);
 * System.out.println(CacheEntry.TIME_CREATED.get(e));
 * </pre>
 * 
 * Or they can be custom-defined attributes that only have a meaning when interpreted by the user of the cache.
 * 
 * 
 */
public class CacheAttributeConfiguration {

    /** The attributes that can be attached to each cache entry. */
    private LinkedHashSet<Attribute<?>> attributes = new LinkedHashSet<Attribute<?>>();

    /**
     * Adds the specified attribute(s).
     * 
     * @param a
     *            the attribute(s) to add
     * @return this configuration
     */
    public CacheAttributeConfiguration add(Attribute<?>... a) {
        for (Attribute<?> aa : a) {
            if (attributes.contains(aa)) {
                throw new IllegalArgumentException("Attribute has already been added [Attribute =" + aa + "");
            }
        }
        for (Attribute<?> aa : a) {
            attributes.add(aa);
        }
        return this;
    }

    /** @return a list of all the attributes that has been added through calls to {@link #add(Attribute...)} */
    public List<Attribute<?>> getAllAttributes() {
        return new ArrayList<Attribute<?>>(attributes);
    }

    /** {@inheritDoc} */
    public String toString() {
        return attributes.toString();
    }
}
