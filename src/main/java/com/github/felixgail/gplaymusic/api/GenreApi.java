package com.github.felixgail.gplaymusic.api;

import com.github.felixgail.gplaymusic.model.Genre;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GenreApi implements SubApi{
    private GPlayMusic mainAPI;

    GenreApi(GPlayMusic api) {
        this.mainAPI = api;
    }

    /**
     * Returns a list of the base genres
     */
    public List<Genre> get() throws IOException {
        return get(null);
    }

    /**
     * @param parent a parent genre, for a list of the base genres.
     */
    public List<Genre> get(Genre parent) throws IOException {
        String id = "";
        if (parent != null) {
            id = parent.getId();
        }
        List<Genre> genres = mainAPI.getService()
                .getGenres(id).execute().body().getGenres().orElse(Collections.emptyList());
        genres.forEach(g -> g.setApi(this));
        return genres;
    }

    @Override
    public GPlayMusic getMainApi() {
        return mainAPI;
    }
}
