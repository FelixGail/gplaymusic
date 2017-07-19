package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PlaylistEntry implements Serializable {
    @Expose
    private String id;
    @Expose
    private String clientId;
    @Expose
    private String playlistId;
    @Expose
    private String absolutePosition;
    @Expose
    private String trackId;
    @Expose
    private String creationTiestamp;
    @Expose
    private String lastModifiedTimestamp;
    @Expose
    private boolean deleted;
    @Expose
    private String source;
    @Expose
    private Track track;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getAbsolutePosition() {
        return absolutePosition;
    }

    public void setAbsolutePosition(String absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getCreationTiestamp() {
        return creationTiestamp;
    }

    public void setCreationTiestamp(String creationTiestamp) {
        this.creationTiestamp = creationTiestamp;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
