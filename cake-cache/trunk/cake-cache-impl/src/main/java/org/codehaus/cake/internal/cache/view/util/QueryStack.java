package org.codehaus.cake.internal.cache.view.util;

public class QueryStack {
    private final Object[] entries;
    private int pointer = -1;

    public QueryStack(int size) {
        entries = new Object[size];
    }

    public Object peek() {
        return entries[pointer];
    }

    public Object pop() {
        return entries[pointer--];
    }

    public QueryStack push(Object o) {
        entries[++pointer] = o;
        return this;
    }

    public QueryStack pushAll(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            push(array[i]);
        }
        return this;
    }

    public boolean isEmpty() {
        return pointer == -1;
    }
}
