package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.snippets.Attribution;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class Album implements Result, Serializable {
  public final static ResultType RESULT_TYPE = ResultType.ALBUM;

  @Expose
  private String name;
  @Expose
  private String albumArtist;
  @Expose
  @SerializedName("albumArtRef")
  private String albumArtUrl;
  @Expose
  private String albumId;
  @Expose
  private String artist;
  @Expose
  private List<String> artistId;
  @Expose
  private int year;
  @Expose
  private String explicitType;
  @Expose
  @SerializedName("description_attribution")
  private Attribution descriptionAttribution;
  @Expose
  private String description;
  @Expose
  private String contentType;
  @Expose
  private List<Track> tracks;

  public Album(@NotNull String name, @NotNull String albumArtist, @NotNull String albumId, @NotNull String artist,
               @NotNull List<String> artistId) {
    this.name = name;
    this.albumArtist = albumArtist;
    this.albumId = albumId;
    this.artist = artist;
    this.artistId = artistId;
  }

  public String getName() {
    return name;
  }

  public String getAlbumArtist() {
    return albumArtist;
  }

  public Optional<String> getAlbumArtUrl() {
    return Optional.ofNullable(albumArtUrl);
  }

  public String getAlbumId() {
    return albumId;
  }

  public String getArtist() {
    return artist;
  }

  public List<String> getArtistId() {
    return artistId;
  }

  public OptionalInt getYear() {
    return OptionalInt.of(year);
  }

  public Optional<String> getExplicitType() {
    return Optional.ofNullable(explicitType);
  }

  public Optional<Attribution> getDescriptionAttribution() {
    return Optional.ofNullable(descriptionAttribution);
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public Optional<String> getContentType() {
    return Optional.ofNullable(contentType);
  }

  public Optional<List<Track>> getTracks() {
    return Optional.ofNullable(tracks);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Album) && ((Album) o).getAlbumId().equals(this.albumId);
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }
}
