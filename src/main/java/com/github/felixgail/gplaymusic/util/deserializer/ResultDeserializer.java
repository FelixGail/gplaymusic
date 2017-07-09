package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.shema.Result;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ResultDeserializer implements JsonDeserializer<Result> {

    @Override
    public Result deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonObject content = je.getAsJsonObject();
        ResultType resultType = new Gson().fromJson(content.get("type"), ResultType.class);
        return new Gson().fromJson(content.get(resultType.getName()), resultType.getType());
    }
}