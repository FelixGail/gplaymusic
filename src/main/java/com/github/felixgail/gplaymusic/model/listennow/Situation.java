package com.github.felixgail.gplaymusic.model.listennow;

import com.github.felixgail.gplaymusic.model.Station;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Contains a list of Stations ({@link #getStations()}) or wraps more Situations ({@link #getSituations()}).
 */
public class Situation implements Result, Serializable {
  public final static ResultType RESULT_TYPE = ResultType.SITUATION;

  @Expose
  private String description;
  @Expose
  private String id;
  @Expose
  private String imageUrl;
  @Expose
  private String title;
  @Expose
  private String wideImageUrl;
  @Expose
  private List<Station> stations;
  @Expose
  private List<Situation> situations;

  public String getDescription() {
    return description;
  }

  public String getId() {
    return id;
  }

  public Optional<String> getImageUrl() {
    return Optional.ofNullable(imageUrl);
  }

  public String getTitle() {
    return title;
  }

  public Optional<String> getWideImageUrl() {
    return Optional.ofNullable(wideImageUrl);
  }

  public Optional<List<Station>> getStations() {
    return Optional.ofNullable(stations);
  }

  public Optional<List<Situation>> getSituations() {
    return Optional.ofNullable(situations);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Situation) && ((Situation) o).getId().equals(this.id);
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }
}
