package org.codehaus.cake.service.spi;


public interface ClassLoaderManager {
    /**
     * @return whether or not this manager supports runtime definition of classes
     */
    boolean isClassDefiningSupported();

    /**
     * Whether or not a class with the the specified <a href="ClassLoader#name">binary name</a> exists
     * 
     * @param name
     *            the binary name of the class to test if it is available
     * @return <code>true</code> if the class is available, otherwise <code>false</code>
     */
    //Class.forname(..., initialize=false, Thread.currentLoader());
    boolean isClassAvailable(String name);

    /**
     * @param name
     *            The <a href="ClassLoader#name">binary name</a> of the class
     * @param b
     * @return the newly defined class
     * @throws UnsupportedOperationException
     *             is class defining is not supported
     */
    Class<?> defineClass(String name, byte[] b);

    /**
     * Loads the class with the specified <a href="ClassLoader#name">binary name</a>.
     * <p>
     * The default implementation of this method will invoke
     * <tt>Thread.currentThread().getContextClassLoader().loadClass(name);</tt>
     * </p>
     * 
     * @param name
     *            The <a href="ClassLoader#name">binary name</a> of the class
     * 
     * @return The resulting <tt>Class</tt> object
     * 
     * @throws ClassNotFoundException
     *             If the class was not found
     */
    Class<?> loadClass(String name);
}
