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
package org.codehaus.cake.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with OnTermination on objects that are registered using
 * {@link ContainerConfiguration#addService(Object)}, {@link ContainerConfiguration#addService(Class, Object)} or
 * {@link ContainerConfiguration#addService(Class, ServiceProvider)} will be invoked after the container has been fully
 * shutdown when the container has terminated. This method is invoked as the last method in the lifecycle of a
 * container. This method is also called if the container failed to initialize or start. But only if the service was
 * succesfully initialized was run without failing.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: OnTermination.java 225 2008-11-30 20:53:08Z kasper $
 */
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnTermination {}
