package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.requestbodies.SharedPlaylistRequest;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutation;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Playlist implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.PLAYLIST;
    public final static String BATCH_URL = "playlistbatch";

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
    private String lastModifiedTimestamp;
    @Expose
    private String recentTimestamp;
    @Expose
    private boolean accessControlled;
    @Expose
    private boolean deleted;
    @Expose
    private String creationTimestamp;
    @Expose
    private String id;
    @Expose
    @SerializedName("albumArtRef")
    private List<ArtRef> artRef;
    @Expose
    private String explicitType;
    @Expose
    private String contentType;
    @Expose
    private PlaylistShareState shareState;

    Playlist(String name, String id, PlaylistShareState shareState, String description, PlaylistType type,
             String lastModifiedTimestamp, String creationTimestamp) {
        this.name = name;
        this.id = id;
        this.shareState = shareState;
        this.description = description;
        this.type = type;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.creationTimestamp = creationTimestamp;
    }

    public Playlist(String id) throws IOException {
        Optional<Playlist> playlistOptional = GPlayMusic.getApiInstance().listPlaylists()
                .stream().filter(p -> p.getId().equals(id)).findFirst();
        if (playlistOptional.isPresent()) {
            Playlist remote = playlistOptional.get();
            this.name = remote.name;
            this.id = remote.id;
            this.shareState = remote.shareState;
            this.description = remote.description;
            this.shareToken = remote.shareToken;
            this.lastModifiedTimestamp = remote.lastModifiedTimestamp;
            this.creationTimestamp = remote.creationTimestamp;
            this.recentTimestamp = remote.recentTimestamp;
            this.ownerName = remote.ownerName;
            this.ownerProfilePhotoUrl = remote.ownerProfilePhotoUrl;
            this.type = remote.type;
            this.accessControlled = remote.accessControlled;
            this.deleted = remote.deleted;
            this.artRef = remote.artRef;
            this.explicitType = remote.explicitType;
            this.contentType = remote.contentType;
        } else {
            throw new IllegalArgumentException("This user is not subscribed to any playlist with that id.");
        }
    }

    /**
     * Creates a new playlist.
     *
     * @param name        Name of the playlist. <b>Doesn't</b> have to be unique
     * @param description Optional. A description for the playlist.
     * @param shareState  share state of the playlist. defaults to {@link PlaylistShareState#PRIVATE} on null.
     * @return The newly created Playlist. Warning: Playlist is not filled yet and timestamps are not valid
     * (Systemtime@Request != Servertime@Creation)
     * @throws IOException
     */
    public static Playlist create(String name, String description, PlaylistShareState shareState)
            throws IOException {
        Mutator mutator = new Mutator(MutationFactory.getAddPlaylistMutation(name, description, shareState));
        String systemTime = Long.toString(System.currentTimeMillis());
        MutationResponse response = GPlayMusic.getApiInstance().getService().makeBatchCall(BATCH_URL, mutator);
        String id = response.getItems().get(0).getId();
        return new Playlist(name, id, (shareState == null ? PlaylistShareState.PRIVATE : shareState),
                description, PlaylistType.USER_GENERATED, systemTime, systemTime);
    }

    public String getName() {
        return name;
    }

    public PlaylistType getType() {
        if (type == null) {
            return PlaylistType.USER_GENERATED;
        }
        return type;
    }

    public String getShareToken() {
        return shareToken;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerProfilePhotoUrl() {
        return ownerProfilePhotoUrl;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public String getRecentTimestamp() {
        return recentTimestamp;
    }

    public boolean isAccessControlled() {
        return accessControlled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getId() {
        return id;
    }

    public List<ArtRef> getArtRef() {
        return artRef;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public String getContentType() {
        return contentType;
    }

    public PlaylistShareState getShareState() {
        if (shareState == null) {
            return PlaylistShareState.PRIVATE;
        }
        return shareState;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Playlist) &&
                ((this.shareToken != null &&
                        ((Playlist) o).shareToken != null &&
                        this.shareToken.equals(((Playlist) o).getShareToken())) ||
                        (this.id != null && ((Playlist) o).getId() != null &&
                                this.getId().equals(((Playlist) o).getId())));
    }

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }

    public void delete() throws IOException {
        GPlayMusic.getApiInstance().deletePlaylists(this);
    }

    /**
     * Adds {@link Track}s to this playlist.
     *
     * @param tracks Array of tracks to be added
     * @return List of created PlaylistEntries. As this list is only filled with the information available,
     * <b>{@link PlaylistEntry#creationTiestamp}, {@link PlaylistEntry#lastModifiedTimestamp} and
     * {@link PlaylistEntry#source}</b> are not set (null). To get fully filled entries, use TODO!
     * @throws IOException
     */
    public List<PlaylistEntry> addTracks(List<Track> tracks)
            throws IOException {
        List<PlaylistEntry> playlistEntries = new LinkedList<>();
        Mutator mutator = new Mutator();
        UUID last = null;
        UUID current = UUID.randomUUID();
        UUID next = UUID.randomUUID();
        for (Track track : tracks) {
            Mutation currentMutation = MutationFactory.
                    getAddPlaylistEntryMutation(this, track, last, current, next);
            mutator.addMutation(currentMutation);
            last = current;
            current = next;
            next = UUID.randomUUID();
        }
        MutationResponse response = GPlayMusic.getApiInstance().getService().makeBatchCall(PlaylistEntry.BATCH_URL, mutator);
        for (int i = 0; i < tracks.size(); i++) {
            MutationResponse.Item item = response.getItems().get(i);
            playlistEntries.add(new PlaylistEntry(item.getId(), item.getClientID(), this.getId(),
                    tracks.get(i), null, null, null, false));
        }
        return playlistEntries;
    }

    /**
     * see javadoc at {@link #addTracks(List)}.
     */
    public List<PlaylistEntry> addTracks(Track... tracks) throws IOException {
        return addTracks(Arrays.asList(tracks));
    }

    /**
     * Returns the contents (as a list of PlaylistEntries) for this playlist.
     *
     * @param maxResults only applicable for shared playlist, otherwise ignored.
     *                   Sets the amount of entries that should be returned.
     *                   Valid range between 0 and 1000. Invalid values will default to 1000.
     * @throws IOException
     */
    public List<PlaylistEntry> getContents(int maxResults)
            throws IOException {
        if (!getType().equals(PlaylistType.SHARED)) {
            //python implementation suggests that this should also work for magic playlists
            return getContentsForUserGeneratedPlaylist();
        }
        return getContentsForSharedPlaylist(maxResults);
    }

    private List<PlaylistEntry> getContentsForUserGeneratedPlaylist() throws IOException {
        List<PlaylistEntry> all_entries =
                GPlayMusic.getApiInstance().listAllPlaylistEntries();
        return all_entries.stream()
                .filter(entry -> entry.getPlaylistId().equals(getId()))
                .sorted(PlaylistEntry::compareTo)
                .collect(Collectors.toList());
    }

    private List<PlaylistEntry> getContentsForSharedPlaylist(int maxResults) throws IOException {
        SharedPlaylistRequest requestBody = new SharedPlaylistRequest(this, maxResults);
        return GPlayMusic.getApiInstance().getService().listSharedPlaylistEntries(requestBody).execute().body().toList();
    }

    public enum PlaylistType implements Serializable {
        @SerializedName("SHARED")
        SHARED,
        //TODO: find out what a magic playlist is. last hint: i don't have any. next idea: crawl python implementation
        @SerializedName("MAGIC")
        MAGIC,
        @SerializedName("USER_GENERATED")
        USER_GENERATED
    }

    public enum PlaylistShareState implements Serializable {
        @SerializedName("PRIVATE")
        PRIVATE,
        @SerializedName("PUBLIC")
        PUBLIC
    }
}
