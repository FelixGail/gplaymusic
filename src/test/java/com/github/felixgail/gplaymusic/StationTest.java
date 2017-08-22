package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.enums.StationSeedType;
import com.github.felixgail.gplaymusic.model.search.SearchTypes;
import com.github.felixgail.gplaymusic.model.shema.Album;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.github.felixgail.gplaymusic.util.TestUtil.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class StationTest extends TestWithLogin {

    @BeforeClass
    public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
        loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
    }

    @Test
    public void testStation() throws IOException {
        List<Station> stations = GPlayMusic.getApiInstance().listStations();
        assertNotNull(stations);
        assertFalse(stations.isEmpty());
        //Prefer a non Playlist station, as they can return empty lists if created on empty playlists.
        Optional<Station> stationOptional = stations.stream()
                .filter(s -> !s.getSeed().getSeedType().equals(StationSeedType.PLAYLIST)).findFirst();
        Station station;
        station = stationOptional.orElseGet(() -> stations.get(0));
        assertNotNull(station);
        assertNotNull(station.getName());
        assertNotNull(station.getId());
        int numEntries = 30;
        List<Track> stationTracks = station.getTracks(numEntries, null, true);
        assertNotNull(stationTracks);
        assertTrue(String.format("Expected list length '%d', got '%d'", numEntries, stationTracks.size()),
                stationTracks.size() == numEntries);
        assertTracks(stationTracks);
        numEntries = 25;
        List<Track> newTracks = station.getTracks(numEntries, stationTracks, true);
        assertNotNull(newTracks);
        assertTrue(String.format("Expected list length '%d', got '%d'", numEntries, newTracks.size()),
                newTracks.size() == numEntries);
        assertTracks(newTracks);
        assertFalse(containsEqualSong(stationTracks, newTracks));
    }

    @Test
    public void createTrackStation() throws IOException {
        Track track = GPlayMusic.getApiInstance().searchTracks("Imagine", 1).get(0);
        assume(track);
        int numEntries = 25;
        Station station = Station.create(new StationSeed(track), "TestTrackStation", true, numEntries);
        TestUtil.testStation(station, numEntries);
    }

    @Test
    public void createAlbumStation() throws IOException {
        Album album = GPlayMusic.getApiInstance()
                .search("Imagine", 1, new SearchTypes(ResultType.ALBUM)).getAlbums().get(0);
        assume(album);
        int numEntries = 25;
        Station station = Station.create(new StationSeed(album), "TestAlbumStation", true, numEntries);
        TestUtil.testStation(station, numEntries);
    }
}
