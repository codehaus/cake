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
package org.codehaus.cake.internal.service.management;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanServer;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.exceptionhandling.InternalDebugService;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.management.DefaultManagedGroup;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.management.ManagedVisitor;
import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.management.Managements;
import org.codehaus.cake.service.OnShutdown;

/**
 * The default implementation of the {@link MapManagementService} interface. All methods exposed through the
 * DefaultManagementService interface can be invoked in a thread safe manner.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * <p>
 * This class is thread-safe.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: DefaultManagementService.java 227 2008-11-30 22:13:02Z kasper $
 */
public class DefaultManagementService extends DefaultManagedGroup implements CompositeService {

    /** Whether or not this service has been shutdown. */
    private volatile boolean isShutdown;

    /** Used to register all services. */
    private ManagedVisitor<?> registrant;

    private final String containerType;

    private final InternalDebugService debugService;

    /**
     * Creates a new DefaultManagementService.
     * 
     * @param conf
     *            the configuration of the Management service
     * @param name
     *            the name of the cache
     */
    public DefaultManagementService(Composer composer, ManagementConfiguration conf, InternalDebugService debugService) {
        super(composer.getContainerName(), "This group contains all managed services");
        this.debugService = debugService;
        containerType = composer.getContainerTypeName();
        /* Set Registrant */
        registrant = conf.getRegistrant();
        if (registrant == null) {
            MBeanServer server = conf.getMBeanServer();
            if (server == null) {
                server = ManagementFactory.getPlatformMBeanServer();
            }
            String domain = conf.getDomain();
            if (domain == null) {
                domain = composer.getDefaultJMXDomain();
            }
            registrant = Managements.hierarchicalRegistrant(server, domain, "name", "service", "group");
        }
    }

    public void register(Composer composer, Set objects) throws JMException {
        debugService.debug("  Manageable.manage()");
        ManagedGroup group = Managements.delegatedManagedGroup(this);
        for (Object o : objects) {
            if (o != null) {
                if (o instanceof Manageable) {
                    Manageable m = (Manageable) o;
                    debugService.debug("  Managing " + m);
                    m.manage(group);
                }
                ManagedObject mo = o.getClass().getAnnotation(ManagedObject.class);
                if (mo != null) {
                    String grp = mo.defaultValue();
                    String desc = mo.description();
                    ManagedGroup mg = group.getChild(grp);
                    if (mg == null) {
                        mg = group.addChild(grp, desc);
                    }
                    mg.add(o);
                }
            }
        }
        try {
            registrant.traverse(group);
        } finally {
            registrant = null;
        }
    }

    @Override
    protected synchronized void beforeMutableOperation() {
        if (registrant == null) {
            throw new IllegalStateException("cannot invoke this method, " + containerType + " has already been started");
        }
    }

    /** {@inheritDoc} */
    public Collection<?> getChildServices() {
        return Collections.singleton(registrant);
    }

    /** {@inheritDoc} */
    @OnShutdown
    public synchronized void stop() throws JMException {
        try {
            // TODO we should log any exceptions returned by traverse
            Managements.unregister().traverse(this);
        } finally {
            isShutdown = true;
        }
    }
}
