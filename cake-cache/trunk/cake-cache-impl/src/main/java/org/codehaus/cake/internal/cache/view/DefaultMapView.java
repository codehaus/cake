package org.codehaus.cake.internal.cache.view;

import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.cake.util.ops.Ops.BinaryProcedure;
import org.codehaus.cake.util.view.MapView;
import org.codehaus.cake.util.view.View;

public class DefaultMapView<K, V> extends AbstractView implements MapView<K, V> {

    public DefaultMapView(AbstractView parent, Object command) {
        super(parent, command);
    }

    /** {@inheritDoc} */
    public void apply(BinaryProcedure<? super K, ? super V> procedure) {
        execute(procedure);
    }

    /** {@inheritDoc} */
    public View<Entry<K, V>> entries() {
        return new DefaultView<Entry<K, V>>(this, ViewCommands.MAP_TO_MAP_ENTRIES);
    }

    /** {@inheritDoc} */
    public View<K> keys() {
        return new DefaultView<K>(this, ViewCommands.MAP_TO_KEYS);
    }

    /** {@inheritDoc} */
    public Map<K, V> toMap() {
        return (Map) execute(Map.class);
    }

    /** {@inheritDoc} */
    public View<V> values() {
        return new DefaultView<V>(this, ViewCommands.MAP_TO_VALUES);
    }
}
