package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutation;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.MutationFactory;
import com.github.felixgail.gplaymusic.model.requestbodies.mutations.Mutator;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Playlist implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.PLAYLIST;
    private final static String batchUrl = "playlistbatch";

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

    protected Playlist(String name, String id, PlaylistShareState shareState, String description, PlaylistType type,
                     String lastModifiedTimestamp, String creationTimestamp) {
        this.name = name;
        this.id = id;
        this.shareState = shareState;
        this.description = description;
        this.type = type;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.creationTimestamp = creationTimestamp;
    }

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

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public String getRecentTimestamp() {
        return recentTimestamp;
    }

    public void setRecentTimestamp(String recentTimestamp) {
        this.recentTimestamp = recentTimestamp;
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

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
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

    public PlaylistShareState getShareState() {
        return shareState;
    }

    public void setShareState(PlaylistShareState shareState) {
        this.shareState = shareState;
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

    public enum PlaylistType implements Serializable {
        @SerializedName("SHARED")
        SHARED,
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

    @Override
    public ResultType getResultType() {
        return RESULT_TYPE;
    }

    /**
     * Creates a new playlist.
     * @param name Name of the playlist. <b>Doesn't</b> have to be unique
     * @param description Optional. A description for the playlist.
     * @param shareState share state of the playlist. defaults to {@link PlaylistShareState#PRIVATE} on null.
     * @return The newly created Playlist. Warning: Playlist is not filled yet.
     * @throws IOException
     */
    public static Playlist create(@NotNull String name, String description, PlaylistShareState shareState)
            throws IOException {
        Mutator mutator = new Mutator(MutationFactory.getAddPlaylistMutation(name, description, shareState));
        MutateResponse response = GPlayMusic.getApiInstance().makeBatchCall(batchUrl, mutator);
        String id = response.getItems().get(0).getId();
        return new Playlist(name, id, (shareState == null?PlaylistShareState.PRIVATE:shareState),
                description, PlaylistType.USER_GENERATED, "0", "-1");
    }

    /**
     * Adds {@link Track}s to this playlist.
     *
     * @param tracks Array of tracks to be added
     * @return List of created PlaylistEntries. As this list is only filled with the information available,
     * <b>{@link PlaylistEntry#creationTiestamp}, {@link PlaylistEntry#lastModifiedTimestamp} and
     * {@link PlaylistEntry#source}</b> are not set (null).
     * @throws IOException
     */
    public List<PlaylistEntry> addTracks(Track... tracks)
            throws IOException{
        List<PlaylistEntry> playlistEntries = new LinkedList<>();
        Mutator mutator = new Mutator();
        UUID last = null;
        UUID current = UUID.randomUUID();
        UUID next = UUID.randomUUID();
        for (Track track : tracks) {
            Mutation currentMutation = MutationFactory.getAddPlaylistEntryMutation(this, track, last, current, next);
            mutator.addMutation(currentMutation);
            last = current;
            current = next;
            next = UUID.randomUUID();
        }
        MutateResponse response = GPlayMusic.getApiInstance().makeBatchCall("plentriesbatch", mutator);
        for (int i = 0; i < tracks.length; i++) {
            MutateResponse.Item item = response.getItems().get(i);
            playlistEntries.add(new PlaylistEntry(item.getId(), item.getClientID(), this.getId(),
                    tracks[i], null, null, null, false));
        }
        return playlistEntries;
    }
}
