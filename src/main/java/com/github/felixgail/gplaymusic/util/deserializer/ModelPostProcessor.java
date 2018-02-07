package com.github.felixgail.gplaymusic.util.deserializer;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.Model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.gsonfire.PostProcessor;


public class ModelPostProcessor implements PostProcessor<Model> {

  private GPlayMusic mainApi;

  @Override
  public void postDeserialize(Model o, JsonElement jsonElement, Gson gson) {
    o.setApi(mainApi);
  }

  @Override
  public void postSerialize(JsonElement jsonElement, Model o, Gson gson) {
  }

  public void setApi(GPlayMusic api) {
    mainApi = api;
  }
}