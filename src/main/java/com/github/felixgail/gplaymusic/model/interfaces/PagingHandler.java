package com.github.felixgail.gplaymusic.model.interfaces;

import com.github.felixgail.gplaymusic.model.shema.ListResult;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class PagingHandler<T> {
  private ListResult<T> current;
  private boolean hasNext = true;

  public abstract ListResult<T> getChunk(String nextPageToken) throws IOException;

  public boolean hasNext() {
    return hasNext;
  }

  public List<T> next() throws IOException {
    String nextPageToken = null;
    if (current != null) {
      nextPageToken = current.getNextPageToken();
    }
    ListResult<T> chunk = getChunk(nextPageToken);
    if (chunk.getNextPageToken() == null || chunk.getNextPageToken().equals(nextPageToken)) {
      hasNext = false;
    }
    current = chunk;
    return chunk.toList();
  }

  public List<T> getAll() throws IOException {
    LinkedList<T> completeList = new LinkedList<>();
    while (hasNext()) {
      completeList.addAll(next());
    }
    return completeList;
  }

  public void reset() {
    current = null;
    hasNext = true;
  }
}
