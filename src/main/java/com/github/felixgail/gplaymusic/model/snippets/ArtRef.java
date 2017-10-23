package com.github.felixgail.gplaymusic.model.snippets;

import com.github.felixgail.gplaymusic.util.deserializer.AutogenEnumDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;

import java.util.Optional;

public class ArtRef {
  @Expose
  private String url;
  @Expose
  private String aspectRatio;
  @Expose
  private Autogen autogen;
  @Expose
  private ColorStyles colorStyles;

  public Optional<ColorStyles> getColorStyles() {
    return Optional.ofNullable(colorStyles);
  }

  public String getUrl() {
    return url;
  }

  public Optional<String> getAspectRatio() {
    return Optional.ofNullable(aspectRatio);
  }

  public Optional<Autogen> isAutogen() {
    return Optional.ofNullable(autogen);
  }

  @JsonAdapter(AutogenEnumDeserializer.class)
  public enum Autogen {
    TRUE,
    FALSE
  }

}
