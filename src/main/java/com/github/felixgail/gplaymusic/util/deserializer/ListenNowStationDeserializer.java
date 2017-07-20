package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowStation;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ListenNowStationDeserializer implements JsonDeserializer<ListenNowStation> {
    private final static Gson gson = new Gson();

    @Override
    public ListenNowStation deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject content = je.getAsJsonObject();
        ListenNowStation station = gson.fromJson(je, ListenNowStation.class);
        JsonArray seeds = content.getAsJsonObject("id").getAsJsonArray("seeds");
        for (JsonElement seed : seeds) {
            station.addSeed(jdc.deserialize(seed, StationSeed.class));
        }
        return station;
    }
}
