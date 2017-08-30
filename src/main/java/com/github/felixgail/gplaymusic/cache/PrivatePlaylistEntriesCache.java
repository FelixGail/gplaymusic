package com.github.felixgail.gplaymusic.cache;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.interfaces.PagingHandler;
import com.github.felixgail.gplaymusic.model.requestbodies.PagingRequest;
import com.github.felixgail.gplaymusic.model.shema.ListResult;
import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;

import java.io.IOException;
import java.util.List;

public class PrivatePlaylistEntriesCache extends AbstractCache<PlaylistEntry> {

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
