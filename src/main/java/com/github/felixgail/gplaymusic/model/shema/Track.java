package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.abstracts.Signable;
import com.github.felixgail.gplaymusic.model.enums.Provider;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.enums.SubscriptionType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.requestbodies.IncrementPlaycountRequest;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.util.language.Language;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Track extends Signable implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.TRACK;
    private static Gson gsonPrettyPrinter = new GsonBuilder().setPrettyPrinting().create();

    //TODO: Not all Attributes added.
    @Expose
    private String title;
    @Expose
    private String artist;
    @Expose
    private String composer;
    @Expose
    private String album;
    @Expose
    private String albumArtist;
    @Expose
    private int year;
    @Expose
    private int trackNumber;
    @Expose
    private String genre;
    @Expose
    private String durationMillis;
    @Expose
    private List<ArtRef> albumArtRef;
    @Expose
    private int discNumber;
    @Expose
    private String estimatedSize;
    @Expose
    private String trackType;
    @Expose
    private String storeId;
    @Expose
    private String albumId;
    @Expose
    private List<String> artistId;
    @Expose
    private String nid;
    @Expose
    private boolean trackAvailableForSubscription;
    @Expose
    private boolean trackAvailableForPurchase;
    @Expose
    private boolean albumAvailableForPurchase;
    @Expose
    private String explicitType;
    @Expose
    private int playCount;
    @Expose
    private String rating;
    @Expose
    private int beatsPerMinute;
    @Expose
    private String clientId;
    @Expose
    private String comment = "";

    //This attribute is only set when the track is retrieved from a station.
    @Expose
    @SerializedName("wentryid")
    private String wentryID;

    public static Track getTrack(String trackID) throws IOException {
        Track track = GPlayMusic.getApiInstance().getService().fetchTrack(trackID).execute().body();
        if (track.getID() == null) {
            throw new IOException(String.format("'%s' did not return a valid track", trackID));
        }
        return track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(String durationMillis) {
        this.durationMillis = durationMillis;
    }

    public List<ArtRef> getAlbumArtRef() {
        return albumArtRef;
    }

    public void setAlbumArtRef(List<ArtRef> albumArtRef) {
        this.albumArtRef = albumArtRef;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public String getEstimatedSize() {
        return estimatedSize;
    }

    public void setEstimatedSize(String estimatedSize) {
        this.estimatedSize = estimatedSize;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    /**
     * Returns how often the song has been played. Not valid, when song has been fetched via
     * {@link Track#getTrack(String)} as the server response does not contain this key.
     */
    public int getPlayCount() {
        return playCount;
    }

    public String getRating() {
        return rating;
    }

    public int getBeatsPerMinute() {
        return beatsPerMinute;
    }

    public String getClientId() {
        return clientId;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String getID() {
        if (storeId == null) {
            return getNid();
        }
        return storeId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public List<String> getArtistId() {
        return artistId;
    }

    public void setArtistId(List<String> artistId) {
        this.artistId = artistId;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public boolean isTrackAvailableForSubscription() {
        return trackAvailableForSubscription;
    }

    public void setTrackAvailableForSubscription(boolean trackAvailableForSubscription) {
        this.trackAvailableForSubscription = trackAvailableForSubscription;
    }

    public boolean isTrackAvailableForPurchase() {
        return trackAvailableForPurchase;
    }

    public void setTrackAvailableForPurchase(boolean trackAvailableForPurchase) {
        this.trackAvailableForPurchase = trackAvailableForPurchase;
    }

    public boolean isAlbumAvailableForPurchase() {
        return albumAvailableForPurchase;
    }

    public void setAlbumAvailableForPurchase(boolean albumAvailableForPurchase) {
        this.albumAvailableForPurchase = albumAvailableForPurchase;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public void setExplicitType(String explicitType) {
        this.explicitType = explicitType;
    }

    public String getWentryID() {
        return wentryID;
    }

    public void setWentryID(String wentryID) {
        this.wentryID = wentryID;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Track) && ((Track) o).getID().equals(this.getID());
    }

    @Override
    public Signature getSignature() {
        return super.createSignature(this.getID());
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }

    /**
     * Returns a URL to download the song in set quality.
     * URL will only be valid for 1 minute.
     * You will likely need to handle redirects.
     * <br>
     * <b>Please note that this function is available for Subscribers only.
     * On free accounts use getStationTrackURL.</b>
     *
     * @param quality quality of the stream
     * @return temporary url to the title
     * @throws IOException Throws an IOException on severe failures (no internet connection...)
     *                     or a {@link NetworkException} on request failures.
     */
    @Override
    public String getStreamURL(StreamQuality quality)
            throws IOException {
        if (GPlayMusic.getApiInstance().getConfig().getSubscription() == SubscriptionType.FREE) {
            throw new IOException(Language.get("users.free.NotAllowed"));
        }
        if (getID() == null || getID().isEmpty()) {
            throw new IOException(Language.get("track.InvalidID"));
        }
        return urlFetcher(quality, Provider.STREAM, EMPTY_MAP);
    }

    /**
     * Fetch the URL from a free Station.
     * Make sure the provided {@link Station} does not return Null on {@link Station#getSessionToken()}.
     * <br>
     * <b>Subscribers should use the {@link #getStreamURL(StreamQuality)}</b> method, which is easier to handle.
     * TODO: provide way to get session token.
     *
     * @param station A station created by TODO.
     *                that contains the song queried for.
     * @param quality - quality of the stream
     * @return a url to download songs from.
     * @throws IOException on severe failures (no internet connection...)
     *                     or a {@link NetworkException} on request failures.
     */
    public String getStationTrackURL(Station station, StreamQuality quality)
            throws IOException {
        if (getWentryID() == null || getWentryID().isEmpty()) {
            throw new IOException(Language.get("track.InvalidWentryID"));
        }
        if (GPlayMusic.getApiInstance().getConfig().getSubscription() == SubscriptionType.ALL_ACCESS) {
            return getStreamURL(quality);
        }
        if (station.getSessionToken() == null || station.getSessionToken().isEmpty()) {
            throw new IOException(Language.get("station.InvalidSessionToken"));
        }
        Map<String, String> map = new HashMap<>();
        map.putAll(STATION_MAP);
        map.put("wentryid", getWentryID());
        map.put("sesstok", station.getSessionToken());
        return urlFetcher(quality, Provider.STATION, map);
    }

    /**
     * Increments the playcount of this song by {@code count}.
     *
     * @param count amount of plays that will be added to the current count.
     * @return whether the incrementation was successful.
     */
    public boolean incrementPlaycount(int count) throws IOException {
        MutationResponse response = GPlayMusic.getApiInstance().getService().incremetPlaycount(
                new IncrementPlaycountRequest(count, this)).execute().body();
        if (response.checkSuccess()) {
            playCount += count;
            return true;
        }
        return false;
    }

    public String string() {
        return gsonPrettyPrinter.toJson(this);
    }

    public void download(StreamQuality quality, Path path) throws IOException {
        URL url = new URL(getStreamURL(quality));
        Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }
}
