package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.PLAYLIST;

    public enum PlaylistType implements Serializable {
        @SerializedName("SHARED")
        SHARED,
        @SerializedName("MAGIC")
        MAGIC,
        @SerializedName("USER_GENERATED")
        USER_GENERATED;
    }

    public enum PlaylistShareState implements Serializable {
        @SerializedName("PRIVATE")
        PRIVATE,
        @SerializedName("PUBLIC")
        PUBLIC;
    }

    @Expose
    private String name;
    @Expose
    private PlaylistType type;
    @Expose
    private String shareToken;
    @Expose
    private String description;
    @Expose
    private String ownerName;
    @Expose
    private String ownerProfilePhotoUrl;
    @Expose
    private String lastModifiedTimeStamp;
    @Expose
    private String recentTimeStamp;
    @Expose
    private boolean accessControlled;
    @Expose
    private boolean deleted;
    @Expose
    private String creationTimeStamp;
    @Expose
    private String id;
    @Expose
    @SerializedName("albumArtRef")
    private List<ArtRef> artRef;
    @Expose
    private String explicitType;
    @Expose
    private String contentType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaylistType getType() {
        return type;
    }

    public void setType(PlaylistType type) {
        this.type = type;
    }

    public String getShareToken() {
        return shareToken;
    }

    public void setShareToken(String shareToken) {
        this.shareToken = shareToken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerProfilePhotoUrl() {
        return ownerProfilePhotoUrl;
    }

    public void setOwnerProfilePhotoUrl(String ownerProfilePhotoUrl) {
        this.ownerProfilePhotoUrl = ownerProfilePhotoUrl;
    }

    public String getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }

    public void setLastModifiedTimeStamp(String lastModifiedTimeStamp) {
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }

    public String getRecentTimeStamp() {
        return recentTimeStamp;
    }

    public void setRecentTimeStamp(String recentTimeStamp) {
        this.recentTimeStamp = recentTimeStamp;
    }

    public boolean isAccessControlled() {
        return accessControlled;
    }

    public void setAccessControlled(boolean accessControlled) {
        this.accessControlled = accessControlled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(String creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ArtRef> getArtRef() {
        return artRef;
    }

    public void setArtRef(List<ArtRef> artRef) {
        this.artRef = artRef;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public void setExplicitType(String explicitType) {
        this.explicitType = explicitType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Playlist) &&
                ((this.shareToken != null &&
                        ((Playlist) o).shareToken != null &&
                        this.shareToken.equals(((Playlist) o).getShareToken())) ||
                        (this.id != null && ((Playlist)o).getId() != null &&
                                this.getId().equals(((Playlist) o).getId())));
    }
}
