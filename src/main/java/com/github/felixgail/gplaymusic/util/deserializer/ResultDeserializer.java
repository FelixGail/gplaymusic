package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ResultDeserializer implements JsonDeserializer<Result> {
  private final static Gson gson = new Gson();

  @Override
  public Result deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
      throws JsonParseException {
    JsonObject content = je.getAsJsonObject();
    ResultType resultType = gson.fromJson(content.get("type"), ResultType.class);
    return new Gson().fromJson(content.get(resultType.getName()), resultType.getType());
  }
}