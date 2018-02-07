package com.github.felixgail.gplaymusic.model.responses;

import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.google.gson.annotations.Expose;
import java.util.List;

public class SharedPlaylistEntryListResult extends ListResult<PlaylistEntry> {

  @Expose
  private List<InnerData<PlaylistEntry>> entries;

  @Override
  public List<PlaylistEntry> toList() {
    return toList(0);
  }

  public List<PlaylistEntry> toList(int index) {
    return entries.get(index).getItems();
  }
}
