package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class ConfigDeserializer implements JsonDeserializer<Config> {

  @Override
  public Config deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
      throws JsonParseException {
    JsonObject content = je.getAsJsonObject();
    Config config = new Config();
    JsonArray entries = content.getAsJsonObject("data").getAsJsonArray("entries");
    for (JsonElement entry : entries) {
      JsonObject o = entry.getAsJsonObject();
      config.put(o.get("key").getAsString(), o.get("value").getAsString());
    }
    return config;
  }
}
