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

import java.util.Arrays;

/**
 * An OperationKey is used to represent an operation as a key in a map.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
class OperationKey {

    /** The hashcode of this object. */
    private int hashCode;

    /** The name of the operation. */
    private final String methodName;

    /** The signature of the operation. */
    private final String[] signature;

    /**
     * Creates a new OperationKey.
     * 
     * @param methodName
     *            the name of the method
     * @param signature
     *            the signature of the method
     */
    OperationKey(String methodName, String... signature) {
        this.methodName = methodName;
        if (signature == null) {
            signature = new String[0];
        }
        this.signature = signature.clone();
        hashCode = methodName.hashCode() ^ Arrays.hashCode(signature);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof OperationKey && equals((OperationKey) obj);
    }

    /**
     * As {@link #equals(Object)} except taking an OperationKey.
     * 
     * @param obj
     *            the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise.
     */
    public boolean equals(OperationKey obj) {
        return methodName.equals(obj.methodName) && Arrays.equals(signature, obj.signature);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return hashCode;
    }
}
