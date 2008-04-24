/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.codehaus.cake.internal.picocontainer.alternatives;

import org.codehaus.cake.internal.picocontainer.defaults.ComponentAdapterFactory;


/**
 * @author Aslak Helles&oslash;y
 * @see org.codehaus.cake.internal.picocontainer.gems.HotSwappingComponentAdapterFactory for a more feature-rich version of the class
 * @since 1.1
 * @deprecated since 1.2, moved to package {@link org.codehaus.cake.internal.picocontainer.defaults}
 */
public class ImplementationHidingComponentAdapterFactory extends
        org.codehaus.cake.internal.picocontainer.defaults.ImplementationHidingComponentAdapterFactory {

    /**
     * @deprecated since 1.2, moved to package {@link org.codehaus.cake.internal.picocontainer.defaults}
     */
    ImplementationHidingComponentAdapterFactory() {
        super();
    }

    /**
     * @deprecated since 1.2, moved to package {@link org.codehaus.cake.internal.picocontainer.defaults}
     */
    public ImplementationHidingComponentAdapterFactory(ComponentAdapterFactory delegate, boolean strict) {
        super(delegate, strict);
    }

    /**
     * @deprecated since 1.2, moved to package {@link org.codehaus.cake.internal.picocontainer.defaults}
     */
    public ImplementationHidingComponentAdapterFactory(ComponentAdapterFactory delegate) {
        super(delegate);
    }

}
