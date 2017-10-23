package com.github.felixgail.gplaymusic.model.listennow;

import com.github.felixgail.gplaymusic.model.Station;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A ListenNowSituation is a selection of curated {@link Station}.
 * Selector considers songs rated by you and recent activity as well as day and daytime. Stations are wrapped inside
 * {@link Situation}s. Retrieve via {@link #getSituations()}.
 */
public class ListenNowSituation implements Serializable {

  @Expose
  private String primaryHeader;
  @Expose
  private String subHeader;
  @Expose
  private List<Situation> situations = new LinkedList<>();
  @Expose
  private DistilledContextWrapper distilledContextWrapper = new DistilledContextWrapper();

  public String getPrimaryHeader() {
    return primaryHeader;
  }

  public String getSubHeader() {
    return subHeader;
  }

  public List<Situation> getSituations() {
    return situations;
  }

  public DistilledContextWrapper getDistilledContextWrapper() {
    return distilledContextWrapper;
  }


  public class DistilledContextWrapper implements Serializable {
    @Expose
    private String distilledContextToken;

    public String getDistilledContextToken() {
      return distilledContextToken;
    }

    public void setDistilledContextToken(String distilledContextToken) {
      this.distilledContextToken = distilledContextToken;
    }
  }
}
