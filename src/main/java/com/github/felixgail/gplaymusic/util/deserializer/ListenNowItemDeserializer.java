package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowAlbum;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowItem;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowStation;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ListenNowItemDeserializer implements JsonDeserializer<ListenNowItem> {
    @Override
    public ListenNowItem deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject content = je.getAsJsonObject();
        if (content.has("album")) {
            addSubEntries(content, content.getAsJsonObject("album"));
            return jdc.deserialize(content, ListenNowAlbum.class);
        }
        if (content.has("radio_station")){
            addSubEntries(content, content.getAsJsonObject("radio_station"));
            return jdc.deserialize(content, ListenNowStation.class);
        }
        throw new JsonParseException("Unknown ListenNowItem type." +
                "Missing key album or radio_station!");
    }

    private void addSubEntries(JsonObject content, JsonObject lower) {
        for (Map.Entry<String, JsonElement> entry : lower.entrySet()) {
            content.add(entry.getKey(), entry.getValue());
        }
    }
}
