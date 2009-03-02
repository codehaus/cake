package org.codehaus.cake.util.collection;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.cake.util.ops.Ops.BinaryProcedure;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Procedure;
import org.codehaus.cake.util.ops.Ops.Reducer;

public class Views {

    public static final MapView EMPTY_MAP_VIEW = new EmptyMapView();

    public static final View EMPTY_VIEW = new EmptyView();

    public static final <E> View<E> emptyView() {
        return EMPTY_VIEW;
    }

    static class EmptyMapView<K, V> implements MapView<K, V>, Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        public void apply(BinaryProcedure<? super K, ? super V> procedure) {
        }

        public View<Entry<K, V>> entries() {
            return EMPTY_VIEW;
        }

        public View<K> keys() {
            return EMPTY_VIEW;
        }

        public long size() {
            return 0;
        }

        public Map<K, V> toMap() {
            return Collections.EMPTY_MAP;
        }

        public View<V> values() {
            return EMPTY_VIEW;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EMPTY_VIEW;
        }
    }

    static class EmptyView<T> implements View<T>, Serializable {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        public T any() {
            return null;
        }

        public void apply(Procedure<? super T> procedure) {
        }

        public boolean isEmpty() {
            return true;
        }

        public <E> View<E> map(Op<? super T, ? extends E> mapper) {
            return (View) this;
        }

        public T max() {
            return null;
        }

        public T max(Comparator<? super T> comparator) {
            return null;
        }

        public T min() {
            return null;
        }

        public T min(Comparator<? super T> comparator) {
            return null;
        }

        public View<T> orderBy(Comparator<? super T> comparator) {
            return this;
        }

        public View<T> orderByMax() {
            return this;
        }

        public View<T> orderByMin() {
            return this;
        }

        public T reduce(Reducer<T> reducer, T base) {
            return base;
        }

        public View<T> setLimit(long limit) {
            return this;
        }

        public long size() {
            return 0;
        }

        public Object[] toArray() {
            return Collections.EMPTY_LIST.toArray();
        }

        public <E> E[] toArray(E[] a) {
            return (E[]) Collections.EMPTY_LIST.toArray(a);
        }

        public List<T> toList() {
            return Collections.EMPTY_LIST;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EMPTY_VIEW;
        }
    }

}
