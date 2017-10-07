package com.github.felixgail.gplaymusic.model.requestbodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PagingRequest implements Serializable {
  @Expose
  @SerializedName("start-token")
  private String nextPageToken;
  @Expose
  @SerializedName("max-results")
  private String maxResults;

  public PagingRequest(String nextPageToken, int maxResults) {
    this.nextPageToken = nextPageToken;
    if (maxResults >= 0) {
      this.maxResults = String.valueOf(maxResults);
    }
  }
}
