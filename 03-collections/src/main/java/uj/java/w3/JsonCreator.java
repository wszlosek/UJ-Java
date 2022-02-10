package uj.java.w3;

import java.util.*;

public class JsonCreator implements JsonMapper {

    private final String[] EMPTY_ARRAY = {"[", "]"};
    private final String[] EMPTY_JSON_OBJECT = {"{", "}"};
    private final String SEPARATOR_SYMBOL = ",";

    @Override
    public String toJson(Map<String, ?> map) {
        StringBuilder json = new StringBuilder();
        if (map == null || map.isEmpty()) {
            json.append(EMPTY_JSON_OBJECT[0]).append(EMPTY_JSON_OBJECT[1]);
            return json.toString();
        }
        return jsonFromNonEmptyMap(map);
    }

    private StringBuilder mapObjectTypeToJson(MapObject mapObject) {
        StringBuilder subJson = new StringBuilder();
        return switch (mapObject.valueType()) {
            case STRING -> subJson.append("\"")
                    .append((mapObject.value()).toString().replace("\"","\\\""))
                    .append("\"");
            case NUMBER, BOOLEAN -> subJson.append(mapObject.value());
            case LIST -> subJson.append(listToJson((List) mapObject.value()));
            case MAP -> subJson.append(toJson((Map<String, ?>) mapObject.value()));
        };
    }

    private StringBuilder listToJson(List list) {
        StringBuilder subJson = new StringBuilder();
        subJson.append(EMPTY_ARRAY[0]);

        for (Object element : list) {
            MapObject m = new MapObject(element);
            subJson.append(mapObjectTypeToJson(m));
            subJson.append(",");
        }
        if (subJson.substring(subJson.length() - 1).equals(SEPARATOR_SYMBOL))
            subJson.deleteCharAt(subJson.length()-1);

        subJson.append(EMPTY_ARRAY[1]);
        return subJson;
    }

    private String jsonFromNonEmptyMap(Map<String, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append(EMPTY_JSON_OBJECT[0]);

        for (Map.Entry<String, ?> mapObject : map.entrySet()) {
            json.append("\"").append(mapObject.getKey()).append("\": ");
            MapObject m = new MapObject(mapObject.getValue());
            json.append(mapObjectTypeToJson(m));
            json.append(",");
        }
        if (json.substring(json.length() - 1).equals(SEPARATOR_SYMBOL))
            json.deleteCharAt(json.length()-1);

        json.append(EMPTY_JSON_OBJECT[1]);
        return json.toString();
    }
}
