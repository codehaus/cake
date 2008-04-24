/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.codehaus.cake.internal.picocontainer.defaults;

import org.codehaus.cake.internal.picocontainer.ComponentAdapter;
import org.codehaus.cake.internal.picocontainer.Parameter;
import org.codehaus.cake.internal.picocontainer.PicoIntrospectionException;

/**
 * @author Aslak Helles&oslash;y
 * @see org.codehaus.cake.internal.picocontainer.gems.HotSwappingComponentAdapterFactory for a more feature-rich version of the class
 * @since 1.2, moved from package {@link org.codehaus.cake.internal.picocontainer.alternatives}
 */
public class ImplementationHidingComponentAdapterFactory extends DecoratingComponentAdapterFactory {
    private final boolean strict;

    /**
     * For serialisation only. Do not use this constructor explicitly.
     */
    public ImplementationHidingComponentAdapterFactory() {
        this(null);
    }

    public ImplementationHidingComponentAdapterFactory(ComponentAdapterFactory delegate, boolean strict) {
        super(delegate);
        this.strict = strict;
    }

    public ImplementationHidingComponentAdapterFactory(ComponentAdapterFactory delegate) {
        this(delegate, true);
    }

    public ComponentAdapter createComponentAdapter(Object componentKey, Class componentImplementation, Parameter[] parameters) throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException {
        return new ImplementationHidingComponentAdapter(super.createComponentAdapter(componentKey, componentImplementation, parameters), strict);
    }
}
