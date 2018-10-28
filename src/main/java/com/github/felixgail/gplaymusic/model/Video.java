package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.snippets.Thumbnail;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class Video implements Result, Serializable {

  public final static ResultType RESULT_TYPE = ResultType.VIDEO;

  @Expose
  private String id;
  @Expose
  private String title;
  @Expose
  private List<Thumbnail> thumbnails;
  @Expose
  private double score;

  /**
   * Returns the youtube id of a video. Can be used like this: https://www.youtube.com/watch?v={ID}
   * <em>see {@link #getURL()}</em>
   */
  public String getId() {
    return id;
  }

  public Optional<String> getTitle() {
    return Optional.ofNullable(title);
  }

  public Optional<List<Thumbnail>> getThumbnails() {
    return Optional.ofNullable(thumbnails);
  }

  /**
   * Score can be >1 (Probably between 0-100). Might score the trust in the result.
   */
  public double getScore() {
    return score;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Video) && ((Video) o).getId().equals(this.id);
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }

  /**
   * Returns the URL to the video on the youtube website.<br>
   * <b>Not embedded, nor a direct link!</b>
   */
  public URL getURL() {
    try {
      return new URL("https://www.youtube.com/watch?v=" + getId());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
