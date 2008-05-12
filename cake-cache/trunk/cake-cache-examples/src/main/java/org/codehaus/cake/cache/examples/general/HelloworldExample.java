/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.general;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.UnsynchronizedCache;

// START SNIPPET: class

public class HelloworldExample {
    public static void main(String[] args) {
        Cache<String, String> c = new UnsynchronizedCache<String, String>();
        c.put("key", "Hello world!");
        System.out.println(c.get("key"));
    }
}
// END SNIPPET: class
