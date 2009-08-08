/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.codehaus.cake.internal.service.ServiceList;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Loggers.Commons;
import org.codehaus.cake.util.Loggers.JDK;
import org.codehaus.cake.util.Loggers.Log4j;

/**
 * This class is the abstract base used for representing the configuration of a container implementation.
 * <p>
 * This class is not meant to be directly instantiated, instead it should be overridden with a configuration object for
 * a concrete container type.
 * <p>
 * All general-purpose <tt>Container</tt> implementation classes should have a constructor with a single argument
 * taking a concrete class extending ContainerConfiguration.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <T>
 *            The type of container instantiated from this configuration
 */
public class ContainerConfiguration {
    /** A Map of additional properties. */
    private final Map<String, String> additionalProperties = new HashMap<String, String>();

    /** The default clock. */
    private Clock clock = Clock.DEFAULT_CLOCK;

    /** A collection of instantiated service configuration objects. */
    private final Map<Object, Object> configurations = new HashMap<Object, Object>();

    /** The default logger. */
    private Logger defaultLogger;

    /** The name of the container. */
    private String name;

    /** Additional configuration objects. */
    private final ServiceList serviceList = new ServiceList();

    private final Map<Object, Class<?>[]> services = new LinkedHashMap<Object, Class<?>[]>();

    // /** The type of container that should be created. */
    // private Class<? extends T> type;

    /**
     * Adds an instantiated configuration object. Available by calling {@link #getConfigurationOfType(Class)} where
     * class is {@link Class#getClass()} of the specified configuration object
     * 
     * @param <T>
     *            this type of configuration
     * @param configuration
     *            the configuration object that should be registered
     * @return the specified configuration object
     * @throws NullPointerException
     *             if the specified configuration object is null
     * @throws IllegalArgumentException
     *             if another configuration of the same type is already registered
     */
    protected final ContainerConfiguration addConfiguration(Object configuration) {
        if (configuration == null) {
            throw new NullPointerException("configuration is null");
        }
        if (configurations.containsKey(configuration.getClass())) {
            throw new IllegalArgumentException("A configuration of type " + configuration.getClass()
                    + " has already been added");
        }
        configurations.put(configuration.getClass(), configuration);
        return this;
    }

    /**
     * @param key
     *            the key under which the service should be available through calls to
     *            {@link Container#getService(Class)}
     * @param service
     *            the service whose lifecycle should be run, and which should be available to calls to
     *            {@link Container#getService(Class)}
     * @return this configuration
     */
    public <S> ContainerConfiguration addService(Class<? extends S> key, S service) {
        serviceList.add(key, service);
        return this;
    }

    /**
     * Adds the specified service factory to the container. Whenever {@link Container#getService(Class)} is called with
     * a class matching the key specified for this method the
     * {@link ServiceProvider#lookup(org.codehaus.cake.service.ServiceProvider.ServiceFactoryContext)} method is called to
     * create a new service (
     * 
     * <p>
     * The specified service factory can be annotated with any of the 4 standard lifecycle annotations
     * {@link RunAfter}. However, annotations on
     * objects returned by {@link ServiceProvider#lookup(org.codehaus.cake.service.ServiceProvider.ServiceFactoryContext)}
     * are ignored.
     * 
     * @param <S>
     *            the type of service
     * @param key
     *            the key under which the specified service provider should be registered.
     * @param factory
     *            the service provider
     * @return this configuration
     */
    public <S> ContainerConfiguration addService(Class<? extends S> key, ServiceProvider<S> factory) {
        serviceList.add(key, factory);
        return this;
    }

    /**
     * Adds a service to to the container.
     * <p>
     * If the object is annotated with the {@link ExportAsService} annotation the object can then later be retrieved by
     * calling {@link org.codehaus.cake.container.Container#getService(Class)}
     * 
     * Registers a object for the container. Only objects of type {@link MapLifecycle} or {@link Manageable}, are
     * valid. If the object is of type {@link MapLifecycle} the container will invoke the respective lifecycle methods
     * on the object. If the object is of type {@link Manageable} and management is enabled for the container (see
     * {@link MapManagementConfiguration#setEnabled(boolean)}). It can be registered with a {@link ManagedGroup}.
     * <p>
     * Attaches the specified instance to the service map of the container. This object can then later be retrieved by
     * calling {@link org.codehaus.cake.container.Container#getService(Class)}.
     * 
     * <pre>
     * ContainerServiceManagerConfiguration csmc;
     * csmc.add(&quot;fooboo&quot;);
     * 
     * ...later..
     * Container&lt;?,?&gt; c;
     * assert &quot;fooboo&quot; = c.getService(String.class);
     * </pre>
     * 
     * If the service is exported with a key that conflicts with the key-type of any of the build-in services. The
     * registered service will replace the build-in service when trying to retrive using
     * {@link Container#getService(Class)} or
     * {@link Container#getService(Class, org.codehaus.cake.attribute.AttributeMap)}.
     * 
     * @param o
     *            the object to register
     * @return this configuration
     * @throws IllegalArgumentException
     *             in case of an argument of invalid type or if the object has already been registered.
     */
    public ContainerConfiguration addService(Object service, Class<?>... exportAs) {
        if (service == null) {
            throw new NullPointerException("service is null");
        } else if (exportAs == null) {
            throw new NullPointerException("exportAs is null");
        } else if (services.containsKey(service)) {
            throw new IllegalArgumentException("Service has already been registered");
        }
        serviceList.addLifecycle(service);
        services.put(service, exportAs);
        return this;
    }

    /**
     * Returns the {@link org.codehaus.cake.util.Clock} that the container should use.
     * 
     * @return the Clock that the container should use
     * @see #setClock(Clock)
     */
    public final Clock getClock() {
        return clock;
    }

    /**
     * Returns a configuration object of the specified type.
     * 
     * @param configurationType
     *            the type of the configuration
     * @return a configuration objects of the specified type
     * @throws IllegalArgumentException
     *             if no configuration object of the specified type exists
     * @param <U>
     *            the type of the configuration
     */
    @SuppressWarnings("unchecked")
    protected <U> U getConfigurationOfType(Class<U> configurationType) {
        Object o = configurations.get(configurationType);
        if (o == null) {
            throw new IllegalArgumentException("Unknown service configuration [ type = " + configurationType + "]");
        }

        return (U) o;
    }

    /**
     * Returns a collection of all service configuration objects registered using {@link #addConfiguration(Object)}.
     * 
     * @return a collection of all service configuration objects
     */
    public final Set<Object> getConfigurations() {
        return new HashSet<Object>(configurations.values());
    }

    /**
     * Returns the default logger configured for this container or <tt>null</tt> if no default logger has been
     * configured.
     * 
     * @return the default logger configured for this container or null if no default logger has been configured
     * @see #setDefaultLogger(Logger)
     */
    public Logger getDefaultLogger() {
        return defaultLogger;
    }

    /**
     * Returns the name of the container.
     * 
     * @return the name of the container, or <tt>null</tt> if no name has been set.
     * @see #setName(String)
     * @see Container#getName()
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns a map of additional properties for the container.
     * 
     * @return a map of additional properties for the container.
     * @see #setProperty(String, String)
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(new HashMap<String, String>(additionalProperties));
    }

    /**
     * Returns the property value for the specified key or <tt>null</tt> if no such property exists. A <tt>null</tt>
     * can also indicate that the key was explicitly mapped to <tt>null</tt>.
     * 
     * @param key
     *            the key for which to retrieve the value
     * @return the value of the property or <tt>null</tt> if no such property exists
     * @throws NullPointerException
     *             if key is <tt>null</tt>
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Returns the property value for the specified key or the specified default value if no such property exists. A
     * property does not exists if it is mapped to <tt>null</tt> either explicitly or because no such entry exists.
     * 
     * @param key
     *            the key for which to retrieve the value
     * @param defaultValue
     *            the default value to return if the property does not exist
     * @return the value of the property or the specified default value if the property does not exist
     * @throws NullPointerException
     *             if key is <tt>null</tt>
     */
    public String getProperty(String key, String defaultValue) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        String result = additionalProperties.get(key);
        return result == null ? defaultValue : result;
    }

    /**
     * Returns the objects that have been registered through {@link #addService(Object)}. The service will be returned
     * in the same order as the they have been added.
     * 
     * @return the objects that have been registered
     */
    public Iterable<Object> getServices() {
        return serviceList.getServices();
    }

    /**
     * Creates a new container instance of the specified type using this configuration.
     * <p>
     * The behavior of this operation is undefined if this configuration is modified while the operation is in progress.
     * 
     * @param type
     *            the type of container that should be created
     * @return a new Container instance
     * @throws IllegalArgumentException
     *             if a container of the specified type could not be created
     * @throws NullPointerException
     *             if the specified type is <tt>null</tt>
     * @param <S>
     *            the type of container to create
     */
    public <S> S newInstance(Class<S> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        Constructor<S> c = null;
        Class<?> clazz = getClass();
        while (!clazz.equals(ContainerConfiguration.class)) {
            try {
                c = (Constructor<S>) type.getDeclaredConstructor(clazz);
            } catch (NoSuchMethodException e) {/* Should never happen */
            }
            clazz = clazz.getSuperclass();
        }
        if (c == null) {
            throw new IllegalArgumentException("Could not create container instance, no public contructor "
                    + "taking a single ContainerConfiguration instance for the specified class [class = " + type + "]");
        }
        try {
            c = (Constructor<S>) type.getDeclaredConstructor(getClass());
        } catch (NoSuchMethodException e) {/* Should never happen */
        }
        try {
            return (S) c.newInstance(this);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Could not create container instance, specified clazz [class = " + type
                    + "] is an interface or an abstract class", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Could not create instance of " + type, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new IllegalArgumentException("Constructor threw exception", cause);
        }
    }

    /**
     * Sets the {@link org.codehaus.cake.util.Clock} that the container should use. Normally users should not need to
     * set this, only if they want to provide another timing mechanism then the built-in
     * {@link java.lang.System#currentTimeMillis()} and {@link java.lang.System#nanoTime()}. For example, a custom NTP
     * protocol.
     * <p>
     * Setting a custom clock can also be useful for introducing determinism while testing.
     * <p>
     * 
     * @param clock
     *            the Clock that the container should use
     * @return this configuration
     * @throws NullPointerException
     *             if the specified clock is <tt>null</tt>
     */
    public ContainerConfiguration setClock(Clock clock) {
        if (clock == null) {
            throw new NullPointerException("clock is null");
        }
        this.clock = clock;
        return this;
    }

    /**
     * Sets the default logger for this container. If for some reason the container or one of its services needs to
     * notify users of some kind of events this logger should be used. Some services might allow to set a special
     * logger. For example, for logging timing informations, auditing, ... etc. In this case this special logger will
     * take precedence over this specified logger when logging for the service.
     * <p>
     * All available containers in Cake strives to be very conservative about what is logged, logging as little as
     * possible. That is, we actually recommend running with log level set at
     * {@link org.codehaus.cake.util.Logger.Level#Info} even in production.
     * 
     * @param logger
     *            the logger to use
     * @return this configuration
     * @see #getDefaultLogger()
     * @see JDK
     * @see Commons
     * @see Log4j
     */
    public ContainerConfiguration setDefaultLogger(Logger logger) {
        this.defaultLogger = logger;
        return this;
    }

    /**
     * Sets the name of the container. The name should, but is not required to, be unique among other configured
     * containers. The name must consists only of alphanumeric characters and '_' or '-'.
     * <p>
     * If no name is set using this method, any container implementation must generate a name an instatiation time.
     * Exactly how the name is generated is implementation specific. But the recommended way is to use
     * {@link UUID#randomUUID()} or a similar mechanism to generate it.
     * 
     * @param name
     *            the name of the container
     * @return this configuration
     * @throws IllegalArgumentException
     *             if the specified name is the empty string or if the name contains other characters then alphanumeric
     *             characters and '_' or '-'
     * @see #getName()
     * @see Container#getName()
     */
    public ContainerConfiguration setName(String name) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("cannot set the empty string as name");
        } else if (name != null) {
            if (!Pattern.matches("[\\da-zA-Z\\x5F\\x2D]+", name)) {
                throw new IllegalArgumentException(
                        "not a valid name, must only contain alphanumeric characters and '_' or '-', was " + name);
            }
        }
        this.name = name;
        return this;
    }

    /**
     * A container implementations might allow additional string based properties to be set then those defined by this
     * class. This method can be used to set these additional properties.
     * 
     * @param key
     *            the key of the property
     * @param value
     *            the value of the property
     * @return this configuration
     * @see #getProperties()
     * @see #getProperty(String)
     * @see #getProperty(String, String)
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     */
    public ContainerConfiguration setProperty(String key, String value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        additionalProperties.put(key, value);
        return this;
    }
}
