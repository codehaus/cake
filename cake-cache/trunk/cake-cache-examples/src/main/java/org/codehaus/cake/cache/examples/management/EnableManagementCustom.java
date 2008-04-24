/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.management;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.codehaus.cake.cache.CacheConfiguration;

public class EnableManagementCustom {
    public static void main(String[] args) {
        CacheConfiguration<String, String> conf = CacheConfiguration.newConfiguration();
        // START SNIPPET: setDomain
        conf.withManagement().setEnabled(true).setDomain("com.acme");
        // END SNIPPET: setDomain
        // START SNIPPET: setMBeanServer
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        conf.withManagement().setEnabled(true).setMBeanServer(server);
        // END SNIPPET: setMBeanServer
    }
}
