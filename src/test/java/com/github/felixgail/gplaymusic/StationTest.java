package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.StationSeedType;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.github.felixgail.gplaymusic.util.TestUtil.assertTracks;
import static com.github.felixgail.gplaymusic.util.TestUtil.containsEqualSong;
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
        List<Track> stationTracks = station.getTracks(5, null, true);
        assertNotNull(stationTracks);
        assertTrue(stationTracks.size() == 5);
        assertTracks(stationTracks);
        List<Track> newTracks = station.getTracks(10, stationTracks, true);
        assertNotNull(newTracks);
        assertTrue(newTracks.size() == 10);
        assertTracks(newTracks);
        assertFalse(containsEqualSong(stationTracks, newTracks));
    }
}
