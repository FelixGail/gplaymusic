package com.github.felixgail.gplaymusic.model.enums;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum SubscriptionType implements Serializable {
  @SerializedName("aa")
  ALL_ACCESS("aa"),
  @SerializedName("fr")
  FREE("fr");

  private String value;

  SubscriptionType(String v) {
    this.value = v;
  }

  public String getValue() {
    return this.value;
  }
}
