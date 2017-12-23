package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.cache.Cache;
import com.github.felixgail.gplaymusic.cache.PrivatePlaylistEntriesCache;
import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.requests.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requests.mutations.Mutator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.github.felixgail.gplaymusic.model.PlaylistEntry.BATCH_URL;

public class PlaylistEntryApi implements SubApi {
  private GPlayMusic mainAPI;
  private PrivatePlaylistEntriesCache cache;

  PlaylistEntryApi(GPlayMusic api) {
    this.mainAPI = api;
    cache = new PrivatePlaylistEntriesCache(api);
  }

  /**
   * Deletes playlist entries. They can be from multiple playlists.
   *
   * @param entries playlist entries to be deleted
   */
  public void deletePlaylistEntries(PlaylistEntry... entries)
      throws IOException {
    deletePlaylistEntries(Arrays.asList(entries));
  }

  /**
   * Deletes playlist entries. They can be from multiple playlists.
   *
   * @param entries list of playlist entries to be deleted
   */
  public void deletePlaylistEntries(Collection<PlaylistEntry> entries) throws IOException {
    Mutator mutator = new Mutator();
    entries.forEach(e -> mutator.addMutation(MutationFactory.getDeletePlaylistEntryMutation(e)));
    mainAPI.getService().makeBatchCall(BATCH_URL, mutator);
    getCache().remove(entries);
  }

  /**
   * Moves the position of this entry in the playlist.
   * Leaving preceding/following empty, implies that the element will be this first/last entry.
   * Leaving a parameter empty, while not aiming for the first/last element of the playlist is undefined - as well as
   * using entries not present in the playlist.
   *
   * @param entry     entry in question
   * @param preceding the entry that will be before the moved entry, or null if moved entry will be first
   * @param following the entry that will be after the moved entry, or null if moved entry will be the last
   */
  public void move(PlaylistEntry entry, PlaylistEntry preceding, PlaylistEntry following)
      throws IOException {
    Mutator mutator = new Mutator(MutationFactory.
        getReorderPlaylistEntryMutation(entry, preceding, following));
    mainAPI.getService().makeBatchCall(BATCH_URL, mutator);
    String tmp = entry.getAbsolutePosition();
    if (preceding != null && entry.compareTo(preceding) < 0) {
      entry.setAbsolutePosition(preceding.getAbsolutePosition());
      preceding.setAbsolutePosition(tmp);
    }
    if (following != null && entry.compareTo(following) > 0) {
      entry.setAbsolutePosition(following.getAbsolutePosition());
      following.setAbsolutePosition(tmp);
    }
  }

  /**
   * {@link PlaylistEntry}s from private Playlists can only be fetched as whole.
   * To shorten wait times, collected entries are cached.
   * Please consider updating the cache (asynchronously) when using the api over a long period of time, or when
   * new entries could be added to the playlists during runtime.
   * <br>
   * If outside access to the library is expected during runtime, disabling caching via {@link #setUseCache(boolean)}
   * should also be considered.
   */
  public void updateCache() throws IOException {
    cache.update();
  }

  /**
   * Enables/Disables caching of {@link PlaylistEntry}s from private playlists.
   */
  public void setUseCache(boolean useCache) {
    cache.setUseCache(useCache);
  }

  public Cache<PlaylistEntry> getCache() {
    return cache;
  }

  @Override
  public GPlayMusic getMainApi() {
    return mainAPI;
  }
}
