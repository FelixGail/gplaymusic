package com.github.felixgail.gplaymusic.model.search.results.snippets;

import com.github.felixgail.gplaymusic.model.search.results.Artist;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MetadataSeed implements Serializable {

    @Expose
    private Artist artist;
    @Expose
    private Genre genre;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
