package com.github.felixgail.gplaymusic.util.serializer;

import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutation;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MutationSerializer implements JsonSerializer<Mutation> {

    private Gson gson = new Gson();

    @Override
    public JsonElement serialize(Mutation mutation, Type type, JsonSerializationContext jsonSerializationContext) {
        String elementName = mutation.getSerializedAttributeName();
        JsonObject mutationJSON = new JsonObject();
        mutationJSON.add(elementName, gson.toJsonTree(mutation.getMutation()));
        return mutationJSON;
    }
}
