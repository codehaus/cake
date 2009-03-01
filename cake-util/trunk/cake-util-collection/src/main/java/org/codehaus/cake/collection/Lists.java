package org.codehaus.cake.collection;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    /**
     * Creates a new list with the specified element as the first element in the list, and the elements in the specified
     * list as the following elements.
     * 
     * @param <E>
     * @param element
     * @param appendLast
     * @return
     */
    public static <E> List<E> join(E element, List<E> appendLast) {
        ArrayList<E> list = new ArrayList<E>();
        list.add(element);
        list.addAll(appendLast);
        return list;
    }
}
