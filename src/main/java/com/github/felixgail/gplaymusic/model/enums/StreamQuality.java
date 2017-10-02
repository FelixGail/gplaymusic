package com.github.felixgail.gplaymusic.model.enums;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum StreamQuality implements Serializable {
  @SerializedName("lo")
  LOW,
  @SerializedName("med")
  MEDIUM,
  @SerializedName("hi")
  HIGH
}
