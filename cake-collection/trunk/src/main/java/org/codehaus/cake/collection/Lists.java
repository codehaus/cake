package org.codehaus.cake.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lists {
    public static <E> List<E> join(E element, Collection<E> appendLast) {
        ArrayList<E> list = new ArrayList<E>();
        list.add(element);
        list.addAll(appendLast);
        return list;
    }
}
