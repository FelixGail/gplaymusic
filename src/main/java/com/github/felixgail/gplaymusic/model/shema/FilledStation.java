package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;
import com.sun.istack.internal.NotNull;

import java.util.List;

public class FilledStation extends Station {
    @Expose
    @NotNull
    private String sessionToken;
    @Expose
    @NotNull
    private List<Track> tracks;

    @Override
    public @NotNull
    String getSessionToken() {
        return sessionToken;
    }

    @Override
    public void setSessionToken(@NotNull String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public @NotNull
    List<Track> getTracks() {
        return tracks;
    }

    @Override
    public void setTracks(@NotNull List<Track> tracks) {
        this.tracks = tracks;
    }
}
