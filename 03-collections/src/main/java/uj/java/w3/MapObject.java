package uj.java.w3;

import java.util.List;
import java.util.Map;


public record MapObject(Object value, ValueType valueType) {

    public enum ValueType {
        STRING, NUMBER, BOOLEAN, LIST, MAP
    }

    public MapObject(Object value) {
        this(value, assertValueType(value));
    }

    public static ValueType assertValueType(Object value) {

        if (value instanceof String) {
            return ValueType.STRING;
        } else if (value instanceof Boolean) {
            return ValueType.BOOLEAN;
        } else if (value instanceof List) {
            return ValueType.LIST;
        } else if (value instanceof Map) {
            return ValueType.MAP;
        } else {
            return ValueType.NUMBER;
        }
    }
}
