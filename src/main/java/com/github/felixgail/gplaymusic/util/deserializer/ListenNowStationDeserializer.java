package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.listennow.ListenNowStation;
import com.github.felixgail.gplaymusic.model.snippets.StationSeed;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class ListenNowStationDeserializer implements JsonDeserializer<ListenNowStation> {

  private final static Gson gson = new Gson();

  @Override
  public ListenNowStation deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
      throws JsonParseException {
    JsonObject content = je.getAsJsonObject();
    ListenNowStation station = gson.fromJson(je, ListenNowStation.class);
    JsonArray seeds = content.getAsJsonObject("id").getAsJsonArray("seeds");
    for (JsonElement seed : seeds) {
      station.addSeed(jdc.deserialize(seed, StationSeed.class));
    }
    return station;
  }
}
