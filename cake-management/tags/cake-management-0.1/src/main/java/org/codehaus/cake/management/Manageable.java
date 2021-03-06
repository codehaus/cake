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
package org.codehaus.cake.management;

/**
 * A mix-in style interface for marking objects that can be registered with a {@link ManagedGroup}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ManagedLifecycle.java 504 2007-12-05 17:49:24Z kasper $
 */
public interface Manageable {

    /**
     * Registers this object with the specified {@link ManagedGroup}.
     * 
     * @param parent
     *            the group to register with
     */
    void manage(ManagedGroup parent);
}
