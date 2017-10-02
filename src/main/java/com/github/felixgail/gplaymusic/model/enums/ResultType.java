package com.github.felixgail.gplaymusic.model.enums;

import com.github.felixgail.gplaymusic.model.shema.Album;
import com.github.felixgail.gplaymusic.model.shema.Artist;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PodcastSeries;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.model.shema.Video;
import com.github.felixgail.gplaymusic.model.shema.listennow.Situation;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;

public enum ResultType implements Serializable {
  @SerializedName("1")
  TRACK(1, "track", Track.class),
  @SerializedName("2")
  ARTIST(2, "artist", Artist.class),
  @SerializedName("3")
  ALBUM(3, "album", Album.class),
  @SerializedName("4")
  PLAYLIST(4, "playlist", Playlist.class),
  @SerializedName("6")
  STATION(6, "station", Station.class),
  @SerializedName("7")
  SITUATION(7, "situation", Situation.class),
  @SerializedName("8")
  VIDEO(8, "video", Video.class),
  @SerializedName("9")
  PODCAST_SERIES(9, "podcast", PodcastSeries.class); //Not tested, needs US vpn

  private final int value;
  private final String name;
  private final Type type;

  ResultType(final int value, final String name, final Type type) {
    this.value = value;
    this.name = name;
    this.type = type;
  }

  public int getValue() {
    return value;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
