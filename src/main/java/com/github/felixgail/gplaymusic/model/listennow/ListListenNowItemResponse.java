package com.github.felixgail.gplaymusic.model.listennow;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ListListenNowItemResponse implements Serializable {

  @Expose
  @SerializedName("listennow_items")
  private List<ListenNowItem> listenNowItems;

  public List<ListenNowItem> getListenNowItems() {
    return listenNowItems;
  }
}
