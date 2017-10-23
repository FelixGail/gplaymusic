package com.github.felixgail.gplaymusic.cache;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.PagingHandler;
import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.model.responses.ListResult;

import java.io.IOException;
import java.util.List;

public class PrivatePlaylistEntriesCache extends Cache<PlaylistEntry> {

  @Override
  public void update() throws IOException {
    List<PlaylistEntry> allEntries = new PagingHandler<PlaylistEntry>() {

      @Override
      public ListResult<PlaylistEntry> getChunk(String nextPageToken) throws IOException {
        return GPlayMusic.getApiInstance().getService().listPrivatePlaylistEntries(
            new PagingRequest(nextPageToken, -1)
        ).execute().body();
      }
    }.getAll();
    setCache(allEntries);
  }
}
