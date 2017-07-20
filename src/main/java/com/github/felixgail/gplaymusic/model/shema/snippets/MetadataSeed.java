package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.github.felixgail.gplaymusic.model.shema.Artist;
import com.github.felixgail.gplaymusic.model.shema.Genre;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MetadataSeed implements Serializable {

    @Expose
    private Artist artist;
    @Expose
    private Genre genre;

    //TODO: Optimize! Only one type (artist/genre) will be present.
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
