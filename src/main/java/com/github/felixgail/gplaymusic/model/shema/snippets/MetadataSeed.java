package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.github.felixgail.gplaymusic.model.shema.Artist;
import com.github.felixgail.gplaymusic.model.shema.Genre;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Optional;

public class MetadataSeed implements Serializable {

    @Expose
    private Artist artist;
    @Expose
    private Genre genre;

    //TODO: Optimize! Only one type (artist/genre) will be present.
    public Optional<Artist> getArtist() {
        return Optional.ofNullable(artist);
    }

    public Optional<Genre> getGenre() {
        return Optional.ofNullable(genre);
    }
}
