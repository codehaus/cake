package org.codehaus.cake.cache.query;

import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.ops.Ops.Op;

public interface Query<T> extends Iterable<T> {

    /** @return the query result(s) as a {@link List} */
    List<T> asList();

    <E> Query<E> to(Op<? super T, ? extends E> mapper);

    /**
     * Creates a new query that uses reflection or bytecode g extract the value
     * 
     * Since the query isn't executed
     * 
     * The following snippet will create a cache and output all the values in the cache as an uppercased list.
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; c = new SynchronizedCache&lt;Integer, String&gt;();
     * c.put(1, &quot;hello&quot;);
     * c.put(2, &quot;world&quot;);
     * System.out.println(c.query().values().to(&quot;toUpperCase&quot;, String.class).asList());
     * Prints [HELLO, WORLD]
     * </pre>
     * 
     * @param <E>
     *            the type returned by the method
     * @param method
     *            the method to invoke
     * @param resultType
     *            the type returned by the method
     * @throws NullPointerException
     *             if the specified method or resultType is null
     * @return the new query
     */
    <E> Query<E> to(String method, Class<E> resultType);

    /**
     * Creates a new Query where all entries are ordered accordingly to the specified comparator.
     * 
     * @param comparator
     *            the comparator used for ordering
     * @throws NullPointerException
     *             if the specified comparator is null
     * @return the new query
     */
    Query<T> orderBy(Comparator<? super T> comparator);

    /**
     * Limits the
     * 
     * @param maxresults
     * @return
     */
    Query<T> setLimit(int maxresults);
}
