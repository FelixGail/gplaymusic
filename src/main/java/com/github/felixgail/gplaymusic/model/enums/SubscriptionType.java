package com.github.felixgail.gplaymusic.model.enums;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum SubscriptionType implements Serializable{
    @SerializedName("aa")
    SUBSCRIBED,
    @SerializedName("fr")
    FREE
}
