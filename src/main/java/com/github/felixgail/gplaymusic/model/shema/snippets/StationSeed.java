package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.github.felixgail.gplaymusic.model.enums.StationSeedType;
import com.github.felixgail.gplaymusic.model.shema.Album;
import com.github.felixgail.gplaymusic.model.shema.Artist;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StationSeed implements Serializable {

    @Expose
    private StationSeedType seedType;
    @Expose
    @SerializedName(value = "albumId", alternate = {"artistId", "genreId", "trackId",
            "curatedStationId", "playlistShareToken"})
    private String seedId;
    @Expose
    private MetadataSeed metadataSeed;

    public StationSeed(Album album) {
        this.seedType = StationSeedType.ALBUM;
        this.seedId = album.getAlbumId();
    }

    public StationSeed(Artist artist) {
        this.seedType = StationSeedType.ARTIST;
    }

    public StationSeedType getSeedType() {
        return seedType;
    }

    public MetadataSeed getMetadataSeed() {
        return metadataSeed;
    }

    public void setMetadataSeed(MetadataSeed metadataSeed) {
        this.metadataSeed = metadataSeed;
    }
}
