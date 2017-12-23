package com.github.felixgail.gplaymusic.model.listennow;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.Model;
import com.github.felixgail.gplaymusic.model.Station;
import com.github.felixgail.gplaymusic.model.snippets.ProfileImage;
import com.github.felixgail.gplaymusic.model.snippets.StationSeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ListenNowStation extends ListenNowItem implements Model {
  @Expose
  @SerializedName("highlight_color")
  private String highlightColor;
  @Expose
  private List<StationSeed> seeds = new LinkedList<>();
  @Expose
  @SerializedName("profile_image")
  private ProfileImage profileImage;
  @Expose
  private String title;

  private GPlayMusic mainApi;

  public Optional<String> getHighlightColor() {
    return Optional.ofNullable(highlightColor);
  }

  public List<StationSeed> getSeeds() {
    return seeds;
  }

  public void setSeeds(List<StationSeed> seeds) {
    this.seeds = seeds;
  }

  public void addSeed(StationSeed seed) {
    if (seeds == null) {
      seeds = new LinkedList<>();
    }
    seeds.add(seed);
  }

  public Optional<ProfileImage> getProfileImage() {
    return Optional.ofNullable(profileImage);
  }

  public String getTitle() {
    return title;
  }

  public Station getStation(boolean includeTracks) throws IOException {
    return mainApi.getStationApi().create(getSeeds().get(0), getTitle(), includeTracks);
  }

  @Override
  public GPlayMusic getApi() {
    return mainApi;
  }

  @Override
  public void setApi(GPlayMusic api) {
    this.mainApi = api;
  }
}
