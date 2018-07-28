package com.github.felixgail.gplaymusic.model.snippets;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class UserPreference implements Serializable {

  @Expose
  private boolean autoDownload;
  @Expose
  private boolean notifyOnNewEpisode;
  @Expose
  private boolean subscribed;

  public boolean isAutoDownload() {
    return autoDownload;
  }

  public boolean isNotifyOnNewEpisode() {
    return notifyOnNewEpisode;
  }

  public boolean isSubscribed() {
    return subscribed;
  }
}
