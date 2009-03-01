package org.codehaus.cake.internal.util.attribute;

import java.security.PrivilegedAction;

public class SecurityTools {

    
    public static Object doPrivileged(PrivilegedAction action) {
        SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return(action.run());
        } else {
            return java.security.AccessController.doPrivileged(action);
        }
    }

}
