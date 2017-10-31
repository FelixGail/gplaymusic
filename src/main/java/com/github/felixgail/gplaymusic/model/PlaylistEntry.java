package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.PlaylistEntryApi;
import com.github.felixgail.gplaymusic.model.requests.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.io.Serializable;

public class PlaylistEntry implements Serializable {
  public final static String BATCH_URL = "plentriesbatch";
  private final static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
  private PlaylistEntryApi api;

  @Expose
  private String id;
  @Expose
  private String clientId;
  @Expose
  private String playlistId;
  @Expose
  private String absolutePosition;
  @Expose
  private String trackId;
  @Expose
  private String creationTimestamp;
  @Expose
  private String lastModifiedTimestamp;
  @Expose
  private boolean deleted;
  @Expose
  private String source;
  @Expose
  private Track track;

  PlaylistEntry(String id, String clientId, String playlistId, Track track, String creationTimestamp,
                String lastModifiedTimestamp, String source, boolean deleted) {
    this.id = id;
    this.clientId = clientId;
    this.playlistId = playlistId;
    this.track = track;
    this.lastModifiedTimestamp = lastModifiedTimestamp;
    this.creationTimestamp = creationTimestamp;
    this.lastModifiedTimestamp = lastModifiedTimestamp;
    this.source = source;
    this.deleted = deleted;
  }

  public String getId() {
    return id;
  }

  public String getClientId() {
    return clientId;
  }

  public String getPlaylistId() {
    return playlistId;
  }

  public String getAbsolutePosition() {
    return absolutePosition;
  }

  public void setAbsolutePosition(String position) {
    this.absolutePosition = position;
  }

  public String getTrackId() {
    return trackId;
  }

  public String getCreationTiestamp() {
    return creationTimestamp;
  }

  public String getLastModifiedTimestamp() {
    return lastModifiedTimestamp;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public String getSource() {
    return source;
  }

  public Track getTrack() throws IOException {
    if (track != null) {
      return track;
    }
    return Track.getTrack(getTrackId());
  }

  public void delete()
      throws IOException {
    api.deletePlaylistEntries(this);
  }

  /**
   * Moves the position of this entry in the playlist.
   * Leaving preceding/following empty, implies that the element will be this first/last entry.
   * Leaving a parameter empty, while not aiming for the first/last element of the playlist is undefined - as well as
   * using entries not present in the playlist.
   *
   * @param preceding the entry that will be before the moved entry, or null if moved entry will be first
   * @param following the entry that will be after the moved entry, or null if moved entry will be the last
   * @throws IOException
   */
  public void move(PlaylistEntry preceding, PlaylistEntry following)
      throws IOException {
    api.move(this, preceding, following);
  }

  public int compareTo(PlaylistEntry entry) {
    return getAbsolutePosition().compareTo(entry.getAbsolutePosition());
  }

  public String string() {
    return prettyGson.toJson(this) + System.lineSeparator();
  }

  public PlaylistEntryApi getApi() {
    return api;
  }

  public void setApi(PlaylistEntryApi api) {
    this.api = api;
  }
}
