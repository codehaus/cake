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
import org.codehaus.cake.internal.picocontainer.Parameter;
import org.codehaus.cake.internal.picocontainer.PicoIntrospectionException;

/**
 * @author Aslak Helles&oslash;y
 * @author <a href="Rafal.Krzewski">rafal@caltha.pl</a>
 * @version $Revision: 1651 $
 */
public class CachingComponentAdapterFactory extends DecoratingComponentAdapterFactory {
    public CachingComponentAdapterFactory() {
        this(null);
    }

    public CachingComponentAdapterFactory(ComponentAdapterFactory delegate) {
        super(delegate);
    }

    public ComponentAdapter createComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters)
            throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException {
        return new CachingComponentAdapter(super.createComponentAdapter(componentKey, componentImplementation, parameters));

    }
}
