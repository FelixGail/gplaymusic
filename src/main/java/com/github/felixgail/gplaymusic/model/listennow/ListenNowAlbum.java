package com.github.felixgail.gplaymusic.model.listennow;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.Album;
import com.github.felixgail.gplaymusic.model.Model;
import com.github.felixgail.gplaymusic.model.snippets.Attribution;
import com.github.felixgail.gplaymusic.model.snippets.ProfileImage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * A {@link ListenNowItem} suggestion. Retrieve the described {@link Album} via {@link #getAlbum(boolean)}.
 */
public class ListenNowAlbum extends ListenNowItem implements Model {
  @Expose
  @SerializedName("artist_metajam_id")
  private String artistMetajamID;
  @Expose
  @SerializedName("artist_name")
  private String artistName;
  @Expose
  @SerializedName("artist_profile_image")
  private ProfileImage artistProfileImage;
  @Expose
  private String description;
  @Expose
  @SerializedName("description_attribution")
  private Attribution descriptionAttribution;
  @Expose
  private String explicitType;
  @Expose
  private String title;
  @Expose
  private MetajamID id;

  private GPlayMusic mainApi;

  public String getArtistID() {
    return artistMetajamID;
  }


  public String getArtistName() {
    return artistName;
  }


  public ProfileImage getArtistProfileImage() {
    return artistProfileImage;
  }


  public String getDescription() {
    return description;
  }


  public Optional<Attribution> getDescriptionAttribution() {
    return Optional.ofNullable(descriptionAttribution);
  }

  public Optional<String> getExplicitType() {
    return Optional.ofNullable(explicitType);
  }

  public Album getAlbum(boolean includeTracks) throws IOException {
    return mainApi.getAlbum(getId().getMetajamCompactKey(), includeTracks);
  }

  public String getTitle() {
    return title;
  }


  public MetajamID getId() {
    return id;
  }

  @Override
  public GPlayMusic getApi() {
    return mainApi;
  }

  @Override
  public void setApi(GPlayMusic api) {
    this.mainApi = api;
  }

  public class MetajamID implements Serializable {
    @Expose
    private String metajamCompactKey;
    @Expose
    private String artist;
    @Expose
    private String title;

    public String getMetajamCompactKey() {
      return metajamCompactKey;
    }

    public String getArtist() {
      return artist;
    }

    public String getTitle() {
      return title;
    }
  }
}
