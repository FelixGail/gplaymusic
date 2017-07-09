package com.github.felixgail.gplaymusic.util.Deserializer;

import com.github.felixgail.gplaymusic.model.config.ConfigResponse;
import com.github.felixgail.gplaymusic.model.shema.Result;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigDeserializer  implements JsonDeserializer<ConfigResponse> {

    @Override
    public ConfigResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonObject content = je.getAsJsonObject();
        ConfigResponse configResponse = new ConfigResponse();
        JsonArray entries = content.getAsJsonObject("data").getAsJsonArray("entries");
        for (JsonElement entry : entries) {
            JsonObject o = entry.getAsJsonObject();
            configResponse.put(o.get("key").getAsString(), o.get("value").getAsString());
        }
        return configResponse;
    }
}
