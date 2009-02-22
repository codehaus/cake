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
package org.codehaus.cake.stubber;

import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandlingConfiguration;
import org.codehaus.cake.stubber.bubber.BubberConfiguration;
import org.codehaus.cake.stubber.exceptionhandling.StubberExceptionHandler;

public class StubberConfiguration<T> extends ContainerConfiguration {
    public static <T> StubberConfiguration<T> newConfiguration() {
        return new StubberConfiguration<T>();
    }

    private String dada;

    public StubberConfiguration() {
        addConfiguration(new ExceptionHandlingConfiguration());
        addConfiguration(new ManagementConfiguration());
        addConfiguration(new BubberConfiguration());
    }

    private final StubberVerifier verifier = new StubberVerifier();

    public StubberVerifier verifier() {
        return verifier;
    }

    public String get() {
        return dada;
    }

    public StubberConfiguration<T> set(String dada) {
        this.dada = dada;
        return this;
    }

    public BubberConfiguration withBubber() {
        return getConfigurationOfType(BubberConfiguration.class);
    }

    public ManagementConfiguration withManagement() {
        return getConfigurationOfType(ManagementConfiguration.class);
    }

    public ExceptionHandlingConfiguration<StubberExceptionHandler<T>> withExceptions() {
        return getConfigurationOfType(ExceptionHandlingConfiguration.class);
    }
}
