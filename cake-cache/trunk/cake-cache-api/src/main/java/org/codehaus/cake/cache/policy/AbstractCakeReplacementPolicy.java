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
package org.codehaus.cake.cache.policy;


/**
 * An abstract implementation of a {@link ReplacementPolicy} that is intended for policies that need to attach
 * attributes to cache entries.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AbstractReplacementPolicy.java 229 2008-12-10 19:53:58Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
public abstract class AbstractCakeReplacementPolicy<T> implements ReplacementPolicy<T> {

    public void clear() {
        while (evictNext() != null)
            ;
    }

    public void replace(T oldObject, T newObject) {
        remove(oldObject);
        add(newObject);
    }

    public void touch(T entry) {

    }
}
