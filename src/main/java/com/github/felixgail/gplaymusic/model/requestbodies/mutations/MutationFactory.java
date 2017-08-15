package com.github.felixgail.gplaymusic.model.requestbodies.mutations;

import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.github.felixgail.gplaymusic.util.serializer.MutationSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.util.*;

public class MutationFactory {
    /**
     * Creates the Mutation needed to remove a PlaylistEntry from a playlist.
     * Mutation has to be wrapped inside a Mutator before sending it to the endpoint.
     *
     * @param entry PlaylistEntry to delete
     */
    public static Mutation<String> getDeletePlaylistEntryMutation(PlaylistEntry entry) {
        return new StringMutation("delete", entry.getId());
    }

    /**
     * Creates the Mutation needed to remove a playlist.
     * Mutation has to be wrapped inside a Mutator before sending it to the endpoint.
     * <br>
     * <b>WARNING: THIS CAN NOT BE UNDONE!</b>
     *
     * @param playlist - playlist to remove
     */
    public static Mutation<String> getDeletePlaylistMutation(Playlist playlist) {
        return new StringMutation("delete", playlist.getId());
    }

    /**
     * Creates the Mutation needed to add a PlaylistEntry to a playlist.
     * Mutation has to be wrapped inside a Mutator before sending it to the endpoint.
     * IDs will be the ClientIDs of the entries.
     *
     * @param playlist     A playlist the track will be added to.
     * @param track        The track to add.
     * @param preceedingID clientID of the preceeding {@link com.github.felixgail.gplaymusic.model.shema.PlaylistEntry} or null if this is the first to add.
     * @param currentID    clientID of the track to add. Can be crated arbitrary, but should be saved if multiple Mutations
     *                     will be added.
     * @param followingID  clientID of the following {@link com.github.felixgail.gplaymusic.model.shema.PlaylistEntry} or null if this is the last entry to add.
     */
    public static Mutation<Map<String, Object>> getAddPlaylistEntryMutation(Playlist playlist,
                                                                            Track track, UUID preceedingID,
                                                                            UUID currentID, UUID followingID) {
        Map<String, Object> create = new HashMap<>();
        create.put("clientID", currentID.toString());
        create.put("creationTimestamp", "-1");
        create.put("deleted", false);
        create.put("lastModifiedTimestamp", "0");
        create.put("playlistId", playlist.getId());
        create.put("source", 1);
        create.put("trackId", track.getID());

        if (track.getID().startsWith("T")) {
            create.put("source", 2);
        }

        if (preceedingID != null) {
            create.put("precedingEntryId", preceedingID.toString());
        }

        if (followingID != null) {
            create.put("followingEntryId", followingID.toString());
        }
        return new MapMutation("create", create);
    }

    /**
     * Creates the Mutation needed to create a new playlist.
     * Playlist names <b>don't</b> have to be unique.
     * Mutation has to be wrapped inside a Mutator before sending it to the endpoint.
     *
     * @param name        a playlist name (not unique)
     * @param description description of the playlist (e.g. "Full of my dearest tunes") or null
     * @param shareState  share state of the playlist. defaults to
     *                    {@link com.github.felixgail.gplaymusic.model.shema.Playlist.PlaylistShareState#PRIVATE} if left null
     */
    public static Mutation<Map<String, Object>> getAddPlaylistMutation(String name, String description,
                                                                       Playlist.PlaylistShareState shareState) {
        shareState = (shareState != null) ? shareState : Playlist.PlaylistShareState.PRIVATE;

        Map<String, Object> create = new HashMap<>();
        create.put("creationTimestamp", "-1");
        create.put("deleted", false);
        create.put("lastModifiedTimestamp", "0");
        create.put("name", name);
        create.put("description", description);
        create.put("type", Playlist.PlaylistType.USER_GENERATED);
        create.put("shareState", shareState);
        return new MapMutation("create", create);
    }

    /**
     * Creates the Mutation needed to reorder Tracks inside a playlist.
     * Mutation has to be wrapped inside a Mutator before sending it to the endpoint.
     * <br>
     * <b>WARNING: Call will result in an error if {@code precedingEntryId} or {@code followingEntry} are not
     * directly behind each other.</b>
     *
     * @param plentry         PlaylistEntry to reorder
     * @param preceedingEntry Entry that will be before the provided entry, or null provided will be the first
     * @param followingEntry  Entry that will be after the providen entry, or null if provided will be the last
     */
    public static Mutation<Map<String, Object>> getReorderPlaylistEntryMutation(
            PlaylistEntry plentry, PlaylistEntry preceedingEntry,
            PlaylistEntry followingEntry) {
        if (preceedingEntry == null && followingEntry == null) {
            throw new IllegalArgumentException("Either preceding or following entry must be provided.");
        }
        Map<String, Object> update = new HashMap<>();
        update.put("clientId", plentry.getClientId());
        update.put("creationTimestamp", plentry.getCreationTiestamp());
        update.put("deleted", plentry.isDeleted());
        update.put("id", plentry.getId());
        update.put("lastModifiedTimestamp", plentry.getLastModifiedTimestamp());
        update.put("playlistId", plentry.getPlaylistId());
        update.put("source", plentry.getSource());
        update.put("trackId", plentry.getTrackId());
        update.put("precedingEntryId", (preceedingEntry != null) ? preceedingEntry.getClientId() : null);
        update.put("followingEntryId", (followingEntry != null) ? followingEntry.getClientId() : null);
        return new MapMutation("update", update);
    }

    /**
     * Creates the Mutation needed to modify a playlist.
     * Playlist names <b>don't</b> have to be unique.
     * At least one parameter has to be set.
     * <br>
     * Mutation has to be wrapped inside a Mutator before sending it to the endpoint.
     */
    public static Mutation<Map<String, Object>> getUpdatePlaylistMutation(
            String newID, String newName, String newDescription,
            Playlist.PlaylistShareState newShareState) {

        if (newID == null && newName == null && newDescription == null && newShareState == null) {
            throw new IllegalArgumentException("At least one update parameter has to be set!");
        }

        Map<String, Object> update = new HashMap<>();
        update.put("id", newID);
        update.put("name", newName);
        update.put("description", newDescription);
        update.put("shareState", newShareState);
        return new MapMutation("update", update);
    }

    public static Mutation<Map<String, Object>> getPodcastMutation(String seriesID,
                                                                   boolean subscribe,
                                                                   boolean notifyOnNewEpisode) {
        MapMutation mutation = new MapMutation("update");
        mutation.addMutation("seriesID", seriesID);
        mutation.addMutation("subscribed", subscribe);
        Map<String, Boolean> userPreferences = new HashMap<>();
        userPreferences.put("subscribed", subscribe);
        userPreferences.put("notifyOnNewEpisode", notifyOnNewEpisode);
        mutation.addMutation("userPreferences", userPreferences);
        return mutation;
    }

    public static Mutation<Map<String, Object>> getAddPodcastSeriesMutation(
            String seriesID, boolean notifyOnNewEpisode) {
        return getPodcastMutation(seriesID, true, notifyOnNewEpisode);
    }

    public static Mutation<Map<String, Object>> getDeletePodcastSeriesMutation(String seriesID) {
        return getPodcastMutation(seriesID, false, false);
    }

    public static Mutation getDeleteStationMutation(Station station) {
        return new DeleteStationMutation(station);
    }

    /**
     * Creates the Mutation needed to create a {@link Station}
     * At least one parameter has to be set.
     *
     * @param name          name of the station
     * @param seed          seed of the station
     * @param includeTracks whether or not the response should include a list of tracks in the new station
     * @param numEntries    server response includes set number of Tracks for the created station.
     */
    public static Mutation getAddStationMutation(String name, StationSeed seed, boolean includeTracks, int numEntries) {
        return new CreateStationMutation(name, seed, includeTracks, numEntries);
    }

    @JsonAdapter(MutationSerializer.class)
    private static class MapMutation implements Mutation<Map<String, Object>>, Serializable {

        @Expose
        private Map<String, Object> data;
        private String attributeName;

        MapMutation(String attributeName) {
            data = new HashMap<>();
            this.attributeName = attributeName;
        }

        MapMutation(String attributeName, Map<String, Object> map) {
            data = map;
            this.attributeName = attributeName;
        }

        public void addMutation(String key, Object value) {
            data.put(key, value);
        }

        @Override
        public Map<String, Object> getMutation() {
            return data;
        }


        public void setMutation(Map<String, Object> stringObjectMap) {
            data = stringObjectMap;
        }

        @Override
        public String getSerializedAttributeName() {
            return attributeName;
        }

        public void setSerializedAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }
    }

    @JsonAdapter(MutationSerializer.class)
    private static class StringMutation implements Mutation<String>, Serializable {
        @Expose
        private String data;

        private String attributeName;

        StringMutation(String attributeName, String data) {
            this.attributeName = attributeName;
            this.data = data;
        }

        @Override
        public String getMutation() {
            return data;
        }


        public void setMutation(String s) {
            data = s;
        }

        @Override
        public String getSerializedAttributeName() {
            return attributeName;
        }

        public void setSerializedAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }
    }

    //Thanks for the consistency google
    private static class CreateStationMutation implements Mutation, Serializable {
        @Expose
        private CreateOrGet createOrGet;
        @Expose
        private boolean includeFeed;
        @Expose
        private int numEntries;
        @Expose
        private Params params = new Params();

        CreateStationMutation(String name, StationSeed seed, boolean includeFeed, int numEntries) {
            this.createOrGet = new CreateOrGet(name, seed);
            this.includeFeed = includeFeed;
            this.numEntries = numEntries;
        }

        @Override
        public Mutation getMutation() {
            return this;
        }

        @Override
        public String getSerializedAttributeName() {
            return "custom";
        }

        private static class Params implements Serializable {
            @Expose
            private int contentFilter = 1;
        }

        private static class CreateOrGet implements Serializable {
            @Expose
            private String clientID = UUID.randomUUID().toString();
            @Expose
            private boolean deleted = false;
            @Expose
            private int imageType = 1;
            @Expose
            private String lastModifiedTimestamp = "-1";
            @Expose
            private String name;
            @Expose
            private String recentTimestamp = String.format("%d", System.currentTimeMillis() * 1000);
            @Expose
            private StationSeed seed;
            @Expose
            private List<Track> tracks = new LinkedList<>();

            CreateOrGet(String name, StationSeed seed) {
                this.name = name;
                this.seed = seed;
            }
        }
    }

    private static class DeleteStationMutation implements Mutation, Serializable {

        @Expose
        private String delete;
        @Expose
        private boolean includeFeed = false;
        @Expose
        private int numEntries = 0;

        DeleteStationMutation(Station station) {
            delete = station.getId();
        }

        @Override
        public Object getMutation() {
            return this;
        }

        @Override
        public String getSerializedAttributeName() {
            return "custom";
        }
    }
}
