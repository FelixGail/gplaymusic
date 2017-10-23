package com.github.felixgail.gplaymusic.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
  @Expose
  private String id;
  @Expose
  private String friendlyName;
  @Expose
  private String type;
  @Expose
  private String lastAccessedTimeMs;

  public String getId() {
    if (id.matches("\\A0x\\S{16}\\z")) {
      id = id.substring(2);
    }
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFriendlyName() {
    return friendlyName;
  }

  public void setFriendlyName(String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLastAccessedTimeMs() {
    return lastAccessedTimeMs;
  }

  public void setLastAccessedTimeMs(String lastAccessedTimeMs) {
    this.lastAccessedTimeMs = lastAccessedTimeMs;
  }
}
