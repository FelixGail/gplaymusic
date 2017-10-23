package com.github.felixgail.gplaymusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MutationResponse implements Serializable {

  @Expose
  @SerializedName(value = "mutate_response", alternate = {"responses"})
  private List<Item> mutate_response = new LinkedList<>();

  public List<Item> getItems() {
    return mutate_response;
  }

  public boolean checkSuccess() {
    for (Item item : mutate_response) {
      if (!item.getResponse_code().matches("^(OK|CONFLICT)$")) {
        return false;
      }
    }
    return true;
  }


  public class Item implements Serializable {
    @Expose
    private String id;
    @Expose
    @SerializedName("client_id")
    private String clientID;
    @Expose
    private String response_code;
    @Expose
    private Station station;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getClientID() {
      return clientID;
    }

    public void setClientID(String clientID) {
      this.clientID = clientID;
    }

    public String getResponse_code() {
      return response_code;
    }

    public void setResponse_code(String response_code) {
      this.response_code = response_code;
    }

    public boolean hasStationKey() {
      return (station != null);
    }

    /**
     * Returns a Station if one was present in the response, or null.
     * Check if it was present in the response with {@link #hasStationKey()};
     */
    public Station getStation() {
      return station;
    }
  }
}
