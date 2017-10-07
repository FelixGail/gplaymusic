package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.github.felixgail.gplaymusic.model.enums.StationSeedType;
import com.github.felixgail.gplaymusic.model.shema.Album;
import com.github.felixgail.gplaymusic.model.shema.Artist;
import com.github.felixgail.gplaymusic.model.shema.Genre;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.serializer.StationSeedSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonAdapter(StationSeedSerializer.class)
public class StationSeed implements Serializable {

  @Expose
  private StationSeedType seedType;
  @Expose
  @SerializedName(value = "albumId", alternate = {"artistId", "genreId", "trackId",
      "curatedStationId", "playlistShareToken"})
  private String seedId;
  @Expose
  private MetadataSeed metadataSeed;

  public StationSeed() {
  }

  public StationSeed(Album album) {
    this.seedType = StationSeedType.ALBUM;
    this.seedId = album.getAlbumId();
  }

  public StationSeed(Artist artist) {
    this.seedType = StationSeedType.ARTIST;
    this.seedId = artist.getArtistId()
        .orElseThrow(() -> new IllegalArgumentException("ArtistID not present. Unable to create seed"));
  }

  public StationSeed(Playlist playlist) {
    this.seedType = StationSeedType.PLAYLIST;
    this.seedId = playlist.getShareToken();
  }

  public StationSeed(Genre genre) {
    this.seedType = StationSeedType.GENRE;
    this.seedId = genre.getId();
  }

  /**
   * Creates a StationSeed from a Station created by a Curated Station. Use this with care, as this will only work on
   * stations that meet the initial description.
   * Deprecated since you should use the {@link Station#getSeed()} method to retrieve the seed from a curated station
   * instead of creating a new one.
   */
  @Deprecated
  public StationSeed(Station curatedStation) {
    this.seedType = StationSeedType.CURATED_STATION;
    this.seedId = curatedStation.getClientId()
        .orElseThrow(() -> new IllegalArgumentException("Station does not contain a clientID"));
  }

  public StationSeed(Track track) {
    this.seedType = StationSeedType.TRACK;
    this.seedId = track.getID();
  }

  public StationSeedType getSeedType() {
    return seedType;
  }

  public MetadataSeed getMetadataSeed() {
    return metadataSeed;
  }

  public void setMetadataSeed(MetadataSeed metadataSeed) {
    this.metadataSeed = metadataSeed;
  }

  public String getSeed() {
    return seedId;
  }
}
