/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.codehaus.cake.internal.picocontainer.defaults;

import org.codehaus.cake.internal.picocontainer.ComponentAdapter;
import org.codehaus.cake.internal.picocontainer.ComponentMonitor;
import org.codehaus.cake.internal.picocontainer.Parameter;
import org.codehaus.cake.internal.picocontainer.PicoIntrospectionException;
import org.codehaus.cake.internal.picocontainer.monitors.DefaultComponentMonitor;

/**
 * Creates instances of {@link ConstructorInjectionComponentAdapter} decorated by
 * {@link CachingComponentAdapter}.
 *
 * @author Jon Tirs&eacute;n
 * @author Aslak Helles&oslash;y
 * @version $Revision: 2779 $
 */
public class DefaultComponentAdapterFactory extends MonitoringComponentAdapterFactory {

    private final LifecycleStrategy lifecycleStrategy;

    public DefaultComponentAdapterFactory(ComponentMonitor monitor) {
        super(monitor);
        this.lifecycleStrategy = new DefaultLifecycleStrategy(monitor);
    }

    public DefaultComponentAdapterFactory(ComponentMonitor monitor, LifecycleStrategy lifecycleStrategy) {
        super(monitor);
        this.lifecycleStrategy = lifecycleStrategy;
    }

    public DefaultComponentAdapterFactory() {
        this.lifecycleStrategy = new DefaultLifecycleStrategy(new DefaultComponentMonitor());
    }

    public ComponentAdapter createComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters) throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException {
        return new CachingComponentAdapter(new ConstructorInjectionComponentAdapter(componentKey, componentImplementation, parameters, false, currentMonitor(), lifecycleStrategy));
    }

    public void changeMonitor(ComponentMonitor monitor) {
        super.changeMonitor(monitor);
        if (lifecycleStrategy instanceof ComponentMonitorStrategy) {
            ((ComponentMonitorStrategy) lifecycleStrategy).changeMonitor(monitor);
        }
    }

}
