package com.github.felixgail.gplaymusic.model.shema.listennow;

import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.snippets.ProfileImage;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ListenNowStation extends ListenNowItem {
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
    return Station.create(getSeeds().get(0), getTitle(), includeTracks);
  }
}
