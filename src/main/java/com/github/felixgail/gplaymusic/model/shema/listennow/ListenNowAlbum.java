package com.github.felixgail.gplaymusic.model.shema.listennow;

import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.model.shema.snippets.Attribution;
import com.github.felixgail.gplaymusic.model.shema.snippets.ProfileImage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Optional;

public class ListenNowAlbum extends ListenNowItem {
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

  public String getArtistMetajamID() {
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


  public String getTitle() {
    return title;
  }


  public MetajamID getId() {
    return id;
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
