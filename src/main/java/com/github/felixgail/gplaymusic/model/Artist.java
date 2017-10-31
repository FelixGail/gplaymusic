package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.snippets.Attribution;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class Artist implements Result, Serializable {
  public final static ResultType RESULT_TYPE = ResultType.ARTIST;

  @Expose
  private String name;
  @Expose
  @SerializedName("artistArtRef")
  private String artistArtUrl;
  @Expose
  private List<ArtRef> artistArtRefs;
  @Expose
  private String artistId;
  @Expose
  @SerializedName("artist_bio_attribution")
  private Attribution artistBioAttribution;
  @Expose
  private String artistBio;
  @Expose
  @SerializedName("related_artists")
  private List<Artist> relatedArtists;
  @Expose
  @SerializedName("total_albums")
  private int totalAlbums;
  @Expose
  private List<Track> topTracks;
  @Expose
  private List<Album> albums;

  public Artist(@NotNull String name) {
    this.name = name;
  }

  public Optional<List<Artist>> getRelatedArtists() {
    return Optional.ofNullable(relatedArtists);
  }

  public OptionalInt getTotalAlbums() {
    return OptionalInt.of(totalAlbums);
  }

  public Optional<List<Track>> getTopTracks() {
    return Optional.ofNullable(topTracks);
  }

  public String getName() {
    return name;
  }

  public Optional<String> getArtistArtUrl() {
    return Optional.ofNullable(artistArtUrl);
  }

  public Optional<List<ArtRef>> getArtistArtRefs() {
    return Optional.ofNullable(artistArtRefs);
  }

  public Optional<String> getArtistId() {
    return Optional.ofNullable(artistId);
  }

  public Optional<Attribution> getArtistBioAttribution() {
    return Optional.ofNullable(artistBioAttribution);
  }

  public Optional<String> getArtistBio() {
    return Optional.ofNullable(artistBio);
  }

  public Optional<List<Album>> getAlbums() {
    return Optional.ofNullable(albums);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Artist) && ((Artist) o).getArtistId().equals(this.artistId);
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }
}
