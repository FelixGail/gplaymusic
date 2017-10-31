package com.github.felixgail.gplaymusic.model;

import com.fasterxml.uuid.Generators;
import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.PlaylistApi;
import com.github.felixgail.gplaymusic.cache.Cache;
import com.github.felixgail.gplaymusic.cache.PrivatePlaylistEntriesCache;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.requests.SharedPlaylistRequest;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutation;
import com.github.felixgail.gplaymusic.model.requests.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: Split into Public and Private Playlist. What to do about magic playlists?
public class Playlist implements Result, Serializable {
  public final static ResultType RESULT_TYPE = ResultType.PLAYLIST;
  public final static String BATCH_URL = "playlistbatch";
  private PlaylistApi api;

  @Expose
  private String name;
  @Expose
  private PlaylistType type;
  @Expose
  private String shareToken;
  @Expose
  private String description;
  @Expose
  private String ownerName;
  @Expose
  private String ownerProfilePhotoUrl;
  @Expose
  private String lastModifiedTimestamp;
  @Expose
  private String recentTimestamp;
  @Expose
  private boolean accessControlled;
  @Expose
  private boolean deleted;
  @Expose
  private String creationTimestamp;
  @Expose
  private String id;
  @Expose
  @SerializedName("albumArtRef")
  private List<ArtRef> artRef;
  @Expose
  private String explicitType;
  @Expose
  private String contentType;
  @Expose
  private PlaylistShareState shareState;

  public PlaylistApi getPlaylistAPI() {
    return api;
  }

  public String getName() {
    return name;
  }

  public PlaylistType getType() {
    if (type == null) {
      return PlaylistType.USER_GENERATED;
    }
    return type;
  }

  public String getShareToken() {
    return shareToken;
  }

  public String getDescription() {
    return description;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public String getOwnerProfilePhotoUrl() {
    return ownerProfilePhotoUrl;
  }

  public String getLastModifiedTimestamp() {
    return lastModifiedTimestamp;
  }

  public String getRecentTimestamp() {
    return recentTimestamp;
  }

  public boolean isAccessControlled() {
    return accessControlled;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public String getCreationTimestamp() {
    return creationTimestamp;
  }

  public String getId() {
    return id;
  }

  public List<ArtRef> getArtRef() {
    return artRef;
  }

  public String getExplicitType() {
    return explicitType;
  }

  public String getContentType() {
    return contentType;
  }

  public PlaylistShareState getShareState() {
    if (shareState == null) {
      return PlaylistShareState.PRIVATE;
    }
    return shareState;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Playlist) &&
        ((this.shareToken != null &&
            ((Playlist) o).shareToken != null &&
            this.shareToken.equals(((Playlist) o).getShareToken())) ||
            (this.id != null && ((Playlist) o).getId() != null &&
                this.getId().equals(((Playlist) o).getId())));
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }

  public void delete() throws IOException {
    api.deletePlaylists(this);
  }

  /**
   * Adds {@link Track}s to this playlist.
   *
   * @param tracks Array of tracks to be added
   * @throws IOException
   */
  public void addTracks(List<Track> tracks)
      throws IOException {
    api.addTracksToPlaylist(this, tracks);
  }

  /**
   * see javadoc at {@link #addTracks(List)}.
   */
  public void addTracks(Track... tracks) throws IOException {
    addTracks(Arrays.asList(tracks));
  }

  /**
   * Returns the contents (as a list of PlaylistEntries) for this playlist.
   *
   * @param maxResults only applicable for shared playlist, otherwise ignored.
   *                   Sets the amount of entries that should be returned.
   *                   Valid range between 0 and 1000. Invalid values will default to 1000.
   * @throws IOException
   */
  public List<PlaylistEntry> getContents(int maxResults)
      throws IOException {
    return api.getContents(this, maxResults);
  }

  public void removeEntries(List<PlaylistEntry> entries) throws IOException {
    api.getPlaylistEntryApi().deletePlaylistEntries(entries);
  }

  public void removeEntries(PlaylistEntry... entries) throws IOException {
    removeEntries(Arrays.asList(entries));
  }

  public PlaylistApi getApi() {
    return api;
  }

  public void setApi(PlaylistApi api) {
    this.api = api;
  }

  public enum PlaylistType implements Serializable {
    @SerializedName("SHARED")
    SHARED,
    //TODO: find out what a magic playlist is. last hint: i don't have a magic playlist. next idea: crawl python implementation
    @SerializedName("MAGIC")
    MAGIC,
    @SerializedName("USER_GENERATED")
    USER_GENERATED
  }

  public enum PlaylistShareState implements Serializable {
    @SerializedName("PRIVATE")
    PRIVATE,
    @SerializedName("PUBLIC")
    PUBLIC
  }
}
