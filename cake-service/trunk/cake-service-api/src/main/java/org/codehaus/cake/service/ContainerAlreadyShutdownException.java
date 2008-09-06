package org.codehaus.cake.service;

/**
 * <code>ContainerShutdownException</code> is the exception thrown by a Container when one of its method has been invoked while the container 
 * afruntime exception thrown by Coconut Cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheException.java 518 2007-12-20 17:21:32Z kasper $
 * @see org.coconut.cache.service.exceptionhandling.CacheExceptionHandler
 */
public class ContainerAlreadyShutdownException extends IllegalStateException {
    /** <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 1L;

    /**
     * Construc ts a new ContainerShsutdownException with <code>null</code> as its detailed message. The cause is not
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
