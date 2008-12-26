package org.codehaus.cake.cache.query;

import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.ops.Ops.Op;

public interface Query<T> extends Iterable<T> {

    /** @return the query result(s) as a {@link List} */
    List<T> asList();

    <E> Query<E> to(Op<T, E> transformer);
    
    <E> Query<E> to(String method, Class<E> resultType);
    
    Query<T> orderBy(Comparator<? super T> comparator);

    /**
     * Limits the
     * 
     * @param maxresults
     * @return
     */
    Query<T> setLimit(int maxresults);

}
