package com.github.felixgail.gplaymusic.model.shema.listennow;

import com.github.felixgail.gplaymusic.model.shema.snippets.Attribution;
import com.github.felixgail.gplaymusic.model.shema.snippets.ProfileImage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

    public void setArtistMetajamID(String artistMetajamID) {
        this.artistMetajamID = artistMetajamID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public ProfileImage getArtistProfileImage() {
        return artistProfileImage;
    }

    public void setArtistProfileImage(ProfileImage artistProfileImage) {
        this.artistProfileImage = artistProfileImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Attribution getDescriptionAttribution() {
        return descriptionAttribution;
    }

    public void setDescriptionAttribution(Attribution descriptionAttribution) {
        this.descriptionAttribution = descriptionAttribution;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public void setExplicitType(String explicitType) {
        this.explicitType = explicitType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MetajamID getId() {
        return id;
    }

    public void setId(MetajamID id) {
        this.id = id;
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

        public void setMetajamCompactKey(String metajamCompactKey) {
            this.metajamCompactKey = metajamCompactKey;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
