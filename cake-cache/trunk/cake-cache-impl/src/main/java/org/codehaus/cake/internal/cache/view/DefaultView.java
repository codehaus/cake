package org.codehaus.cake.internal.cache.view;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.cache.view.View;
import org.codehaus.cake.ops.Comparators;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.ops.Ops.Reducer;

@SuppressWarnings("unchecked")
public class DefaultView<T> extends AbstractView implements View<T> {

    DefaultView(AbstractView parent, Object command) {
        super(parent, command);
    }

    /** {@inheritDoc} */
    public T any() {
        return (T) execute(ViewCommands.ANY);
    }

    /** {@inheritDoc} */
    public void apply(Procedure<? super T> procedure) {
        if (procedure == null) {
            throw new NullPointerException("procedure is null");
        }
        execute(procedure);
    }

    /** {@inheritDoc} */
    public <E> View<E> map(Op<? super T, ? extends E> mapper) {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        return new DefaultView(this, mapper);
    }

    /** {@inheritDoc} */
    public T max() {
        return (T) execute(ObjectOps.MAX_REDUCER, null);
    }

    /** {@inheritDoc} */
    public T max(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return (T) execute(ViewCommands.MAX_COMPARATOR, comparator);
    }

    /** {@inheritDoc} */
    public T min() {
        return (T) execute(ObjectOps.MIN_REDUCER, null);
    }

    /** {@inheritDoc} */
    public T min(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return (T) execute(ViewCommands.MIN_COMPARATOR, comparator);
    }

    /** {@inheritDoc} */
    public View<T> orderBy(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        return new DefaultView(this, comparator);
    }

    /** {@inheritDoc} */
    public View<T> orderByMax() {
        return new DefaultView(this, Comparators.NATURAL_COMPARATOR);
    }

    /** {@inheritDoc} */
    public View<T> orderByMin() {
        return new DefaultView(this, ViewCommands.ORDER_BY_NATURAL_MIN);
    }

    /** {@inheritDoc} */
    public T reduce(Reducer<T> reducer, T base) {
        if (reducer == null) {
            throw new NullPointerException("reducer is null");
        }
        return (T) execute(reducer, base);
    }

    /** {@inheritDoc} */
    public View<T> setLimit(long limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be posive (>0)");
        }
        return new DefaultView(this, limit);
    }

    /** {@inheritDoc} */
    public Object[] toArray() {
        return (Object[]) execute(Array.class, null);
    }

    /** {@inheritDoc} */
    public <E> E[] toArray(E[] array) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        return (E[]) execute(Array.class, array);
    }

    /** {@inheritDoc} */
    public List<T> toList() {
        return (List) execute(List.class);
    }
}
