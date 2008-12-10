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

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * This class is used to configure how a container can be remotely monitored and managed using JMX.
 * <p>
 * Remote management (JMX) is turned off by default and you need to call {@link #setEnabled(boolean)} to enable it.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ManagementConfiguration.java 225 2008-11-30 20:53:08Z kasper $
 */
public class ManagementConfiguration {

    /** The domain to register managed beans under. */
    private String domain;

    /** Whether or not JMX management is enabled. */
    private boolean enabled;

    /** The MBeanServer to register the managed beans under. */
    private MBeanServer mBeanServer;

    /** The visitor to use for registration of the managed beans. */
    private ManagedVisitor<?> registrant;

    /**
     * Returns the default domain to register all managed beans under.
     * 
     * @return the domain to register all managed beans under
     * @see #setDomain(String)
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @return the configured MBeanServer
     * @see #setMBeanServer(MBeanServer)
     */
    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    /**
     * Returns the configured registrant.
     * 
     * @return the configured registrant
     * @see #setRegistrant(ManagedVisitor)
     */
    public ManagedVisitor<?> getRegistrant() {
        return registrant;
    }

    /**
     * Returns true if management is enabled, otherwise false.
     * <p>
     * The default setting is <tt>false</tt>.
     * 
     * @return <tt>true</tt> if management is enabled, otherwise <tt>false</tt>
     * @see #setEnabled(boolean)
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the specific domain that MBeans should register under. If no domain is specified the container will use a
     * default name. For example, {@link org.codehaus.cake.cache.CacheMXBean} is registered under
     * {@link org.codehaus.cake.cache.CacheMXBean#DEFAULT_JMX_DOMAIN}.
     * 
     * @param domain
     *            the domain name
     * @return this configuration
     * @throws IllegalArgumentException
     *             if the specified domain is not valid domain name
     */
    public ManagementConfiguration setDomain(String domain) {
        if (domain != null) {
            try {
                new ObjectName(domain + ":type=foo");
            } catch (MalformedObjectNameException e) {
                throw new IllegalArgumentException("The specified domain is not a valid domain name, " + e.getMessage());
            }
        }
        this.domain = domain;
        return this;
    }

    /**
     * Sets whether or not management is enabled. The default value is <tt>false</tt>.
     * 
     * @param enabled
     *            whether or not management should be enabled
     * @return this configuration
     * @see #isEnabled()
     */
    public ManagementConfiguration setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Sets the {@link MBeanServer} that MBeans should register with. If no MBeanServer is set and this service is
     * enabled; the {@link java.lang.management.ManagementFactory#getPlatformMBeanServer() platform MBeanServer} will be
     * used.
     * 
     * @param server
     *            the server that MBeans should register with
     * @return this configuration
     * @see #getMBeanServer()
     */
    public ManagementConfiguration setMBeanServer(MBeanServer server) {
        mBeanServer = server;
        return this;
    }

    /**
     * Sets a ManagedVisitor that will used to register all managed objects. Normal users will seldom need to use this
     * method. But if you need some kind of non standard naming of {@link javax.management.ObjectName ObjectNames},
     * wants to only register a specific service or another hierarchy then the one used by default by the container.
     * This method can be used specify a special registrant.
     * <p>
     * If no registrant is specified the containers default registrant will be used.
     * 
     * @param registrant
     *            the registrant
     * @return this configuration
     * @see #getRegistrant()
     */
    public ManagementConfiguration setRegistrant(ManagedVisitor<?> registrant) {
        this.registrant = registrant;
        return this;
    }
}
