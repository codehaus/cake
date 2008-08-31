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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.configuration.ConfigurableService;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubber1Service;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubberService;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.stubber.StubberConfiguration;
import org.codehaus.cake.stubber.bubber.BubberService;

public class DefaultBubberService<T> implements BubberService<T>, ConfigurableService {

    public static final Object C = new String("DefaultBubberService_constructor");
    int foofoo;

    public DefaultBubberService(StubberConfiguration conf, DefaultTubberService s, DefaultTubber1Service s1) {
        conf.verifier().hasHappenend(DefaultTubber1Service.c);
        conf.verifier().constructor(C);
        assertEquals("ok", s.get());
    }

    @Startable
    public void register(ServiceRegistrant registrant) {
        registrant.registerService(BubberService.class, this);
    }

    public void processUpdate(AttributeMap attributes) {
        this.foofoo = attributes.get(BubberService.FOOFOO);
    }

    public int getFooFoo() {
        return foofoo;
    }
}
