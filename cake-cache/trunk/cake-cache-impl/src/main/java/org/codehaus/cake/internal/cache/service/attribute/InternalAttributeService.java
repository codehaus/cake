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
package org.codehaus.cake.internal.cache.service.attribute;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public interface InternalAttributeService<K, V> {

    MutableAttributeMap create(K key, V value, AttributeMap params);

    MutableAttributeMap update(K key, V value, AttributeMap params, AttributeMap previous);

    void initialize();
    void access(AttributeMap map);
}
