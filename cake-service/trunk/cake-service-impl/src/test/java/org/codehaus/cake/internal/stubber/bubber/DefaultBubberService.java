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
package org.codehaus.cake.internal.stubber.bubber;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.configuration.RuntimeConfigurableService;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubber1Service;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubberService;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.stubber.StubberConfiguration;
import org.codehaus.cake.stubber.bubber.BubberService;

@ExportAsService(BubberService.class)
public class DefaultBubberService<T> implements BubberService<T>, RuntimeConfigurableService {

    public static final Object C = new String("DefaultBubberService_constructor");
    int foofoo;

    public DefaultBubberService(StubberConfiguration conf, DefaultTubberService s, DefaultTubber1Service s1) {
        conf.verifier().hasHappenend(DefaultTubber1Service.c);
        conf.verifier().constructor(C);
        assertEquals("ok", s.get());
    }
    
    public void updateConfiguration(AttributeMap attributes) {
        this.foofoo = attributes.get(BubberService.FOOFOO);
    }

    public int getFooFoo() {
        return foofoo;
    }

    public Set<Attribute<?>> getRuntimeConfigurableAttributes() {
        return new HashSet<Attribute<?>>(Arrays.asList(BubberService.FOOFOO));
    }
    public Set<Attribute<?>> getRuntimeConfigurableAttribute2s() {
        return new HashSet<Attribute<?>>(Arrays.asList(BubberService.FOOFOO));
    }
}
