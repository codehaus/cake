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
package org.codehaus.cake.util.view;

import java.io.Serializable;
import java.math.BigDecimal;
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

    public static void main2(String[] args) {
        BigDecimal b;
        View<BigDecimal> numbers = null;
        BigDecimal sum = numbers.reduce(new Reducer<BigDecimal>() {
            public BigDecimal op(BigDecimal a, BigDecimal b) {
                return a.add(b);
            }
        }, BigDecimal.ZERO);
    }

    // Triple<Long,Long,Double> minMaxAverage(LongView view);
    // Pair<Long,Long> minMax(LongView view);
    // <T> Pair<T,T> minMax(View<T> view);
    // <T> List<T> top(View<T> view, int count);

    public static <T, V> View<T> mapToLong(View<V> v, String method) {
        return null;
    }
    public static <T, V> View<T> map(View<V> v, String method) {
        return null;
    }

    public static <T> List<T> top(View<T> view, int count) {
        return null;
    }

    public static void main(String[] args) {
        View<Integer> v = null;
        List<Integer> tops = top(v, 10);
        View<String> vv = map(v, "toString");
    }

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

        public T any(T base) {
            return base;
        }

        public T one() {
            throw new IllegalStateException("view is empty");
        }

        public T one(T base) {
            return base;
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

        public View<T> notNull() {
            return this;
        }
    }

}
