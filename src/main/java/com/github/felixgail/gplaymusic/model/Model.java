package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.api.GPlayMusic;

import javax.validation.constraints.NotNull;

public interface Model {
  @NotNull
  GPlayMusic getApi();

  void setApi(@NotNull GPlayMusic api);
}
