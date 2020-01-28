package com.github.felixgail.gplaymusic.model.enums;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public enum StreamQuality implements Serializable {
  @SerializedName("lo")
  LOW("lo"),
  @SerializedName("med")
  MEDIUM("med"),
  @SerializedName("hi")
  HIGH("hi");

  private String value;

  StreamQuality(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}