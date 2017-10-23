package com.github.felixgail.gplaymusic.cache;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.PagingHandler;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.model.responses.ListResult;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibraryTrackCache extends Cache<Track> {
  private PagingHandler<Track> pagingHandler;

  public LibraryTrackCache() {
    pagingHandler = new PagingHandler<Track>() {
      @Override
      public ListResult<Track> getChunk(String nextPageToken) throws IOException {
        return GPlayMusic.getApiInstance().getService()
            .listTracks(new PagingRequest(nextPageToken, -1)).execute().body();
      }

      @Override
      public List<Track> next() throws IOException {
        return super.next().stream()
            .filter(t -> !t.getStoreId().isPresent() && t.getUuid().isPresent()).collect(Collectors.toList());
      }
    };
  }

  @Override
  public void update() throws IOException {
    List<Track> trackList = pagingHandler.getAll();
    setCache(trackList);
  }

  public Optional<Track> find(String trackID) throws IOException {
    Optional<Track> trackOptional;
    if (!isUseCache()) {
      pagingHandler.reset();
    } else {
      trackOptional = getCurrentCache().stream().filter(t -> t.getID().equals(trackID)).findFirst();
      if (trackOptional.isPresent()) {
        return trackOptional;
      }
    }
    while (pagingHandler.hasNext()) {
      List<Track> tracks = pagingHandler.next();
      if (tracks.size() > 0) {
        if (isUseCache()) {
          add(tracks);
        }
        trackOptional = tracks.stream().filter(t -> t.getID().equals(trackID)).findFirst();
        if (trackOptional.isPresent()) {
          return trackOptional;
        }
      }
    }
    return Optional.empty();
  }
}
