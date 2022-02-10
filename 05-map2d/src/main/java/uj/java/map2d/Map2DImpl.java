package uj.java.map2d;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Map2DImpl<R, C, V> implements Map2D<R, C, V> {

    Map<Coordinates<R, C>, V> map = new HashMap<>();

    @Override
    public V put(R rowKey, C columnKey, V value) {
        if (rowKey == null || columnKey == null) {
            throw new NullPointerException();
        } else {
            V result = map.get(new Coordinates<>(rowKey, columnKey));
            map.put(new Coordinates<>(rowKey, columnKey), value);
            return result;
        }
    }

    @Override
    public V get(R rowKey, C columnKey) {
        return map.get(new Coordinates<>(rowKey, columnKey));
    }

    @Override
    public V getOrDefault(R rowKey, C columnKey, V defaultValue) {
        return map.getOrDefault(new Coordinates<>(rowKey, columnKey), defaultValue);
    }

    @Override
    public V remove(R rowKey, C columnKey) {
        return map.remove(new Coordinates<>(rowKey, columnKey));
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean nonEmpty() {
        return !isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Map<C, V> rowView(R rowKey) {
        Map<C, V> result = new HashMap<>();
        for (var el : map.entrySet()) {
            var key = el.getKey();
            if (key.row().equals(rowKey)) {
                result.put(key.column(), el.getValue());
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<R, V> columnView(C columnKey) {
        Map<R, V> result = new HashMap<>();
        for (var el : map.entrySet()) {
            var key = el.getKey();
            if (key.column().equals(columnKey)) {
                result.put(key.row(), el.getValue());
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public boolean hasValue(V value) {
        return map.containsValue(value);
    }

    @Override
    public boolean hasKey(R rowKey, C columnKey) {
        return map.containsKey(new Coordinates<>(rowKey, columnKey));
    }

    @Override
    public boolean hasRow(R rowKey) {
        for (var el : map.entrySet()) {
            if (el.getKey().row().equals(rowKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasColumn(C columnKey) {
        for (var el : map.entrySet()) {
            if (el.getKey().column().equals(columnKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<R, Map<C, V>> rowMapView() {
        Map<R, Map<C, V>> result = new HashMap<>();
        for (var el : map.entrySet()) {
            var row = el.getKey().row();
            result.put(row, rowView(row));
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<C, Map<R, V>> columnMapView() {
        Map<C, Map<R, V>> result = new HashMap<>();
        for (var el : map.entrySet()) {
            var column = el.getKey().column();
            result.put(column, columnView(column));
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey) {
        for (var el : map.entrySet()) {
            var key = el.getKey();
            if (key.row().equals(rowKey)) {
                target.put(key.column(), el.getValue());
            }
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey) {
        for (var el : map.entrySet()) {
            var key = el.getKey();
            if (key.column().equals(columnKey)) {
                target.put(key.row(), el.getValue());
            }
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> putAll(Map2D<? extends R, ? extends C, ? extends V> source) {
        var mapView = source.rowMapView();
        for (var row : mapView.keySet()) {
            for (var column : mapView.get(row).keySet()) {
                this.put(row, column, mapView.get(row).get(column));
            }
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToRow(Map<? extends C, ? extends V> source, R rowKey) {
        for (var column : source.keySet()) {
            this.put(rowKey, column, source.get(column));
        }
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToColumn(Map<? extends R, ? extends V> source, C columnKey) {
        for (var row : source.keySet()) {
            this.put(row, columnKey, source.get(row));
        }
        return this;
    }

    @Override
    public <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction
    ) {

        Map2D<R2, C2, V2> result = Map2D.createInstance();
        for (var el : map.entrySet()) {
            var key = el.getKey();
            result.put(rowFunction.apply(key.row()), columnFunction.apply(key.column()),
                    valueFunction.apply(el.getValue()));
        }
        return result;
    }

    @Override
    public String toString() {
        return "map=" + map;
    }

}
