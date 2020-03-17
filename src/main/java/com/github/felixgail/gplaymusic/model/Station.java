package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.requests.ListStationTracksRequest;
import com.github.felixgail.gplaymusic.model.responses.Result;
import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.snippets.StationSeed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Station implements Result, Serializable, Model {

  public final static ResultType RESULT_TYPE = ResultType.STATION;
  public final static String BATCH_URL = "radio/editstation";
  private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Expose
  private String name;
  @Expose
  private String imageUrl;
  @Expose
  private boolean deleted;
  @Expose
  private String lastModifiedTimestamp;
  @Expose
  private String recentTimestamp;
  @Expose
  private String clientId;
  @Expose
  private String sessionToken;
  @Expose
  private StationSeed seed;
  @Expose
  private List<StationSeed> stationSeeds;
  @Expose
  private String id;
  @Expose
  private String description;
  @Expose
  private List<Track> tracks = new LinkedList<>();
  @Expose
  @SerializedName("imageUrls")
  private List<ArtRef> imageArtRefs;
  @Expose
  private List<ArtRef> compositeArtRefs;
  @Expose
  private List<String> contentTypes;
  @Expose
  private String byline;

  private GPlayMusic mainApi;

  public String getName() {
    return name;
  }

  public Optional<String> getImageUrl() {
    return Optional.ofNullable(imageUrl);
  }

  public boolean isDeleted() {
    return deleted;
  }

  public Optional<String> getLastModifiedTimestamp() {
    return Optional.ofNullable(lastModifiedTimestamp);
  }

  public Optional<String> getRecentTimestamp() {
    return Optional.ofNullable(recentTimestamp);
  }

  public Optional<String> getClientId() {
    return Optional.ofNullable(clientId);
  }

  public StationSeed getSeed() {
    return seed;
  }

  public List<StationSeed> getStationSeeds() {
    return stationSeeds;
  }

  public String getId() throws IOException {
    if (id != null) {
      return id;
    }
    if (getSeed() != null) {
      Station createOrGet = mainApi.getStationApi().create(getSeed(), getName(), false);
      this.id = createOrGet.id;
      this.clientId = createOrGet.clientId;
      return id;
    }
    throw new NullPointerException("Radio does not contain ID or Seeds");
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  /**
   * Get Tracks for this Station.<br> <b> Keep in mind that this can return an empty list, if this
   * station is created on an empty playlist. </b>
   *
   * @param recentlyPlayed a list of tracks that have recently been played. tracks from this list
   * will, <b>most of the time</b>, be excluded from the response. For some reason this is sometimes
   * ignored by the server. Use {@code forceRemoveDoubles} to remove doubles returned by the
   * server.
   * @param newCall true if a new call shall be dispatched. false if the list from a previous call
   * is to be returned. Careful: Will return an empty list if no call has been made.
   * @param forceRemoveDoubles see {@code recentlyPlayed}. Force remove doubles returned by the
   * server.
   * @return A list of 25 tracks for this station.
   *
   * @throws RemoteException If the result returned from google has errors.
   */
  public List<Track> getTracks(List<Track> recentlyPlayed, boolean newCall,
      boolean forceRemoveDoubles)
      throws IOException, RemoteException {
    if (!newCall) {
      return Optional.of(tracks).orElse(Collections.emptyList());
    }
    ListStationTracksRequest request = new ListStationTracksRequest(this, 25, recentlyPlayed);
    List<Station> result = mainApi.getStationApi().getFilledStations(request);
    if(result.isEmpty()) {
      throw new RemoteException("Server returned an empty list of stations.");
    }
    Station returnedStation = result.get(0);
    Optional<List<Track>> trackOptional = Optional.ofNullable(returnedStation.tracks);
    sessionToken = returnedStation.sessionToken;
    List<Track> tracks = trackOptional.orElse(Collections.emptyList());
    tracks.forEach(t -> t.setSessionToken(sessionToken));
    if (forceRemoveDoubles) {
      Iterator<Track> iter = tracks.iterator();
      while (iter.hasNext()) {
        Track track = iter.next();
        for (Track recent : recentlyPlayed) {
          if (track.getID().equals(recent.getID())) {
            iter.remove();
          }
        }
      }
    }
    return tracks;
  }

  public Optional<List<ArtRef>> getImageArtRefs() {
    return Optional.ofNullable(imageArtRefs);
  }

  public Optional<List<ArtRef>> getCompositeArtRefs() {
    return Optional.ofNullable(compositeArtRefs);
  }

  public Optional<List<String>> getContentTypes() {
    return Optional.ofNullable(contentTypes);
  }

  public Optional<String> getByline() {
    return Optional.ofNullable(byline);
  }

  public Optional<String> getSessionToken() {
    return Optional.ofNullable(sessionToken);
  }

  @Override
  public ResultType getResultType() {
    return RESULT_TYPE;
  }

  public void delete()
      throws IOException {
    mainApi.getStationApi().deleteStations(this);
  }

  public String string() {
    return gson.toJson(this);
  }

  @Override
  public GPlayMusic getApi() {
    return this.mainApi;
  }

  @Override
  public void setApi(GPlayMusic api) {
    this.mainApi = api;
  }
}
