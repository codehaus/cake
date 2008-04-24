package org.codehaus.cake.internal.cache.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.codehaus.cake.cache.Cache;

public class CacheInternals {

    private static final String BUNDLE_NAME = Cache.class.getPackage().getName() + ".messagesimpl";//$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /** Cannot instantiate. */
    // /CLOVER:OFF
    private CacheInternals() {}

    // /CLOVER:ON

    /**
     * Looksup a message in the default bundle.
     * 
     * @param c
     *            the class looking up the value
     * @param key
     *            the message key
     * @param o
     *            additional parameters
     * @return a message from the default bundle.
     */
    public static String lookup(Class<?> c, String key, Object... o) {
        String k = key.replace(' ', '_');
        return lookupKey(RESOURCE_BUNDLE, c.getSimpleName() + "." + k, o);
    }

    static String lookupKey(ResourceBundle bundle, String key, Object... o) {
        String lookup = bundle.getString(key);
        if (o != null && o.length > 0) {
            MessageFormat mf = new MessageFormat(lookup, Locale.US);
            return mf.format(o);
        } else {
            return lookup;
        }
    }
}
