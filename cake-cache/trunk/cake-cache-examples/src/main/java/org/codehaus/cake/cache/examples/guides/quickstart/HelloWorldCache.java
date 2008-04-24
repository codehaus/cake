/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.guides.quickstart;

//START SNIPPET: helloworld
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.defaults.UnsynchronizedCache;

public class HelloWorldCache {
    public static void main(String[] args) {
        Cache<Integer, String> cache = new UnsynchronizedCache<Integer, String>();
        cache.put(5, "helloworld");
        System.out.println(cache.get(5)); // prints helloworld
    }
}
// END SNIPPET: helloworld
