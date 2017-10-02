package com.github.felixgail.gplaymusic.util.serializer;

import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class StationSeedSerializer implements JsonSerializer<StationSeed> {
    private final static Gson gson = new Gson();

    @Override
    public JsonElement serialize(StationSeed seed, Type type, JsonSerializationContext jsc) {
        JsonObject seedJson = new JsonObject();
        seedJson.add("seedType", gson.toJsonTree(seed.getSeedType()));
        seedJson.add("metadataSeed", gson.toJsonTree(seed.getMetadataSeed()));
        seedJson.addProperty(seed.getSeedType().getKey(), seed.getSeed());
        return seedJson;
    }
}
