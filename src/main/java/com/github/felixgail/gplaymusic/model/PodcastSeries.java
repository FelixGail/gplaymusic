package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.snippets.UserPreference;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class PodcastSeries implements Result, Serializable {

  public final static ResultType RESULT_TYPE = ResultType.PODCAST_SERIES;

  @Expose
  private List<ArtRef> art;
  @Expose
  private String author;
  @Expose
  private String continuationToken;
  @Expose
  private String copyright;
  @Expose
  private String description;
  @Expose
  private List<PodcastEpisode> episodes;
  @Expose
  private String explicitType;
  @Expose
  private String link;
  @Expose
  private String seriesId;
  @Expose
  private String title;
  @Expose
  private int totalNumEpisodes;
  @Expose
  private UserPreference userPreferences;

  public Optional<List<ArtRef>> getArt() {
    return Optional.ofNullable(art);
  }

  public String getAuthor() {
    return author;
  }

  public Optional<String> getContinuationToken() {
    return Optional.ofNullable(continuationToken);
  }

  public Optional<String> getCopyright() {
    return Optional.ofNullable(continuationToken);
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public Optional<List<PodcastEpisode>> getEpisodes() {
    return Optional.ofNullable(episodes);
  }

  public String getExplicitType() {
    return explicitType;
  }

  public Optional<String> getLink() {
    return Optional.ofNullable(link);
  }

  public String getSeriesId() {
    return seriesId;
  }

  public String getTitle() {
    return title;
  }

  public int getTotalNumEpisodes() {
    return totalNumEpisodes;
  }

  public Optional<UserPreference> getUserPreferences() {
    return Optional.ofNullable(userPreferences);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof PodcastSeries) && ((PodcastSeries) o).getSeriesId().equals(this.seriesId);
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }
}
