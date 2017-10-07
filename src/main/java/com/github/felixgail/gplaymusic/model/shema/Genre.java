package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
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

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Optional<List<Genre>> getChildren() throws IOException {
    if (children != null) {
      return Optional.of(get(this));
    }
    return Optional.empty();
  }
  
  public Optional<String> getParentID() {
    return Optional.ofNullable(parentId);
  }

  public Optional<List<ArtRef>> getImages() {
    return Optional.ofNullable(images);
  }

}
