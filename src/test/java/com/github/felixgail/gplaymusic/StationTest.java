package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.Album;
import com.github.felixgail.gplaymusic.model.Artist;
import com.github.felixgail.gplaymusic.model.Station;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.enums.StationSeedType;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.model.snippets.StationSeed;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
  @Ignore
  //Is this an error by google play? will return a broken station by 'Onlap' that contains 0 tracks
  public void testStation() throws IOException {
    List<Station> stations = getApi().getStationApi().listStations();
    assertNotNull(stations);
    assertFalse(stations.isEmpty());
    //Prefer a non Playlist station, as they can return empty lists if created on empty playlists.
    Optional<Station> stationOptional = stations.stream()
        .filter(s -> !s.getSeed().getSeedType().equals(StationSeedType.PLAYLIST))
        .filter(s -> !s.isDeleted()).findFirst();
    Station station;
    station = stationOptional.orElseGet(() -> stations.get(0));
    System.out.println(station.string());
    assertNotNull(station);
    assertNotNull(station.getName());
    assertNotNull(station.getId());
    List<Track> stationTracks = station.getTracks(null, true, false);
    assertNotNull(stationTracks);
    assertTrue(String.format("Expected list length 25, got '%d'", stationTracks.size()),
        stationTracks.size() == 25);
    assertTracks(stationTracks);
    List<Track> newTracks = station.getTracks(stationTracks, true, true);
    assertNotNull(newTracks);
    assertTrue(String.format("Expected list length expected <=25, got '%d'", newTracks.size()),
        newTracks.size() <= 25);
    assertTracks(newTracks);
    int doubles = containsDoubledTracks(stationTracks, newTracks);
    assertTrue(String.format("Second Call contained %d doubles.", doubles), doubles == 0);
  }

  @Test
  public void createTrackStation() throws IOException {
    Track track = getApi().getTrackApi().search("Imagine", 1).get(0);
    assume(track);
    Station station = getApi().getStationApi().create(new StationSeed(track), "TestTrackStation", false);
    TestUtil.testStation(station);
    station.delete();
  }

  @Test
  public void createAlbumStation() throws IOException {
    Album album = getApi()
        .search("Imagine", 1, new SearchTypes(ResultType.ALBUM)).getAlbums().get(0);
    assume(album);
    Station station = getApi().getStationApi().create(new StationSeed(album), "TestAlbumStation", false);
    TestUtil.testStation(station);
    station.delete();
  }

  @Test
  public void createArtistStation() throws IOException {
    Artist artist = getApi()
        .search("Imagine", 1, new SearchTypes(ResultType.ARTIST)).getArtists().get(0);
    assume(artist);
    Station station = getApi().getStationApi().create(new StationSeed(artist), "TestArtistStation", false);
    TestUtil.testStation(station);
    station.delete();
  }
}
