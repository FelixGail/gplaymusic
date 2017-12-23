package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ResultDeserializer implements JsonDeserializer<Result> {

  @Override
  public Result deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
      throws JsonParseException {
    JsonObject content = je.getAsJsonObject();
    ResultType resultType = jdc.deserialize(content.get("type"), ResultType.class);
    return jdc.deserialize(content.get(resultType.getName()), resultType.getType());
  }
}