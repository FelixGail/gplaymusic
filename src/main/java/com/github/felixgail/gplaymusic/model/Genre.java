package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Genre implements Serializable {

  @Expose
  private String id;
  @Expose
  private String name;
  @Expose
  private List<String> children;
  @Expose
  private String parentId;
  @Expose
  private List<ArtRef> images;

  /**
   * Returns a list of the base genres
   */
  public static List<Genre> get() throws IOException {
    return get(null);
  }

  /**
   * @param parent a parent genre, for a list of the base genres.
   */
  public static List<Genre> get(Genre parent) throws IOException {
    String id = "";
    if (parent != null) {
      id = parent.getId();
    }
    return GPlayMusic.getApiInstance().getService()
        .getGenres(id).execute().body().getGenres().orElse(Collections.emptyList());
  }

  /**
   * Returns the identification of this genre. Genre ids readable and uppercase, e.g. "ROCK".
   */
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  /**
   * Returns a list of IDs of the child genres (if present) as {@link Optional}. For details see {@link #getId()}.
   * To get the child genres directly use {@link #getChildren()}.
   */
  public Optional<List<String>> getChildrenIDs() {
    return Optional.ofNullable(children);
  }

  public Optional<List<Genre>> getChildren() throws IOException {
    if (children != null) {
      return Optional.of(get(this));
    }
    return Optional.empty();
  }

  /**
   * Returns the ID of the parent genre (if present) as {@link Optional}. For details see {@link #getId()}.
   */
  public Optional<String> getParentID() {
    return Optional.ofNullable(parentId);
  }

  public Optional<List<ArtRef>> getImages() {
    return Optional.ofNullable(images);
  }

}
