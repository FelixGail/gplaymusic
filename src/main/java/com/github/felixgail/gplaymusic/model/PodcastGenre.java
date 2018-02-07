package com.github.felixgail.gplaymusic.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PodcastGenre implements Serializable {

  @Expose
  private String id;
  @Expose
  private String displayName;
  @Expose
  private List<PodcastGenre> subgroups = new LinkedList<>();

  public String getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Optional<List<PodcastGenre>> getSubgroups() {
    return Optional.ofNullable(subgroups);
  }
}
