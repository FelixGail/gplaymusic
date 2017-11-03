package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.cache.Cache;
import com.github.felixgail.gplaymusic.model.MutationResponse;
import com.github.felixgail.gplaymusic.model.PagingHandler;
import com.github.felixgail.gplaymusic.model.Playlist;
import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.model.requests.SharedPlaylistRequest;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutation;
import com.github.felixgail.gplaymusic.model.requests.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.responses.ListResult;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.felixgail.gplaymusic.model.Playlist.BATCH_URL;

public class PlaylistApi implements SubApi {
  private GPlayMusic mainApi;
  private PlaylistEntryApi entryApi;


  PlaylistApi(@NotNull GPlayMusic api, @NotNull PlaylistEntryApi entryAPI) {
    this.mainApi = api;
    this.entryApi = entryAPI;
  }

  /**
   * Creates a new playlist.
   *
   * @param name        Name of the playlist. <b>Doesn't</b> have to be unique
   * @param description Optional. A description for the playlist.
   * @param shareState  share state of the playlist. defaults to {@link Playlist.PlaylistShareState#PRIVATE} on null.
   * @return The newly created Playlist. Warning: Playlist is not filled yet and timestamps are not valid
   * (Systemtime@Request != Servertime@Creation)
   * @throws IOException
   */
  public Playlist create(String name, String description, Playlist.PlaylistShareState shareState)
      throws IOException {
    Mutator mutator = new Mutator(MutationFactory.getAddPlaylistMutation(name, description, shareState));
    MutationResponse response = mainApi.getService().makeBatchCall(BATCH_URL, mutator);
    String id = response.getItems().get(0).getId();
    return getPlaylist(id);
  }

  public Playlist getPlaylist(String id) throws IOException {
    Optional<Playlist> playlistOptional = listPlaylists()
        .stream().filter(p -> p.getId().equals(id)).findFirst();
    return playlistOptional.orElseThrow(() ->
        new IllegalArgumentException("This user is not subscribed to any playlist with that id."));
  }

  /**
   * deletes playlists and all contained {@link PlaylistEntry}
   *
   * @param playlists playlists to be deleted
   */
  public void deletePlaylists(Playlist... playlists)
      throws IOException {
    Mutator mutator = new Mutator();
    for (Playlist playlist : playlists) {
      mutator.addMutation(MutationFactory.getDeletePlaylistMutation(playlist));
    }
    mainApi.getService().makeBatchCall(Playlist.BATCH_URL, mutator);
  }

  public void addTracksToPlaylist(Playlist playlist, List<Track> tracks) throws IOException {
    Mutator mutator = new Mutator();
    UUID last = null;
    UUID current = UUID.randomUUID();
    UUID next = UUID.randomUUID();
    for (Track track : tracks) {
      Mutation currentMutation = MutationFactory.
          getAddPlaylistEntryMutation(playlist, track, last, current, next);
      mutator.addMutation(currentMutation);
      last = current;
      current = next;
      next = UUID.randomUUID();
    }
    mainApi.getService().makeBatchCall(PlaylistEntry.BATCH_URL, mutator);
    getCache().update();
  }

  /**
   * Returns the contents (as a list of PlaylistEntries) for this playlist.
   *
   * @param maxResults only applicable for shared playlist, otherwise ignored.
   *                   Sets the amount of entries that should be returned.
   *                   Valid range between 0 and 1000. Invalid values will default to 1000.
   * @throws IOException
   */
  public List<PlaylistEntry> getContents(@NotNull Playlist playlist, int maxResults)
      throws IOException {
    if (!playlist.getType().equals(Playlist.PlaylistType.SHARED)) {
      //python implementation suggests that this should also work for magic playlists
      return getContentsForUserGeneratedPlaylist(playlist, maxResults);
    }
    return getContentsForSharedPlaylist(playlist, maxResults);
  }

  private List<PlaylistEntry> getContentsForUserGeneratedPlaylist
      (@NotNull Playlist playlist, int maxResults)
      throws IOException {
    return getCache().getStream()
        .filter(entry -> entry.getPlaylistId().equals(playlist.getId()))
        .filter(entry -> !entry.isDeleted())
        .sorted(PlaylistEntry::compareTo)
        .limit(maxResults > 0 ? maxResults : Long.MAX_VALUE)
        .collect(Collectors.toList());
  }

  private List<PlaylistEntry> getContentsForSharedPlaylist
      (@NotNull Playlist playlist, int maxResults)
      throws IOException {
    SharedPlaylistRequest requestBody = new SharedPlaylistRequest(playlist, maxResults);
    return mainApi.getService()
        .listSharedPlaylistEntries(requestBody).execute().body().toList();
  }

  public List<Playlist> listPlaylists()
      throws IOException {
    return new PagingHandler<Playlist>() {
      @Override
      public ListResult<Playlist> getChunk(String nextPageToken) throws IOException {
        return mainApi.getService().listPlaylists(new PagingRequest(nextPageToken, -1)).execute().body();
      }
    }.getAll();
  }

  private Cache<PlaylistEntry> getCache() {
    return entryApi.getCache();
  }

  public PlaylistEntryApi getPlaylistEntryApi() {
    return entryApi;
  }

  @Override
  public GPlayMusic getMainApi() {
    return mainApi;
  }
}
