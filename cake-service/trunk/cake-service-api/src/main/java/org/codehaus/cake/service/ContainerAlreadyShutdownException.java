package org.codehaus.cake.service;

/**
 * Thrown by a {@link Container} instance or a service registered in the container whenever the container has been
 * shutdown and the method invoked required a running container. No methods defined in the Container interface actually
 * throws this exception, however, subclasses of the interface might.
 * <p>
 * It is okay for service to have methods that both silently ignore invocations after a cache has been shutdown, and
 * methods that throw this exception. For example, the {@link org.codehaus.cake.cache.Cache#containsKey(Object)} method return <tt>false</tt>
 * for any argument whenever a cache has been shutdown. While the {@link org.codehaus.cake.cache.Cache#put(Object, Object)} method throws a
 * {@link ContainerAlreadyShutdownException} for any argument when the cache has been shutdown.
 * <p>
 * A container is normally shutdown by calling using {@link Container#shutdown()} or {@link Container#shutdownNow()}.
 * However, if a container failed to start up properly the
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheException.java 518 2007-12-20 17:21:32Z kasper $
 */
public class ContainerAlreadyShutdownException extends IllegalStateException {
    /** <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ContainerShutdownException with <code>null</code> as its detailed message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link Throwable#initCause}.
     */
    public ContainerAlreadyShutdownException() {
    }

    /**
     * Constructs a new ContainerShutdownException with the specified detailed message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link Throwable#initCause}.
     * 
     * @param message
     *            the detailed message. The detailed message is saved for later retrieval by the {@link #getMessage()}
     *            method.
     */
    public ContainerAlreadyShutdownException(String message) {
        super(message);
    }
}
