package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class AutogenEnumDeserializer implements JsonDeserializer<ArtRef.Autogen> {

  @Override
  public ArtRef.Autogen deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (json.getAsBoolean()) {
      return ArtRef.Autogen.TRUE;
    }
    return ArtRef.Autogen.FALSE;
  }
}
