package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.TokenProvider;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.github.felixgail.gplaymusic.util.TestUtil.assume;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class AuthenticatedTests {

    @BeforeClass
    public static void loginToService() throws IOException, Gpsoauth.TokenRequestFailed {
        AuthToken token;
        if (TestUtil.TOKEN.isValid() && !TestUtil.TOKEN.get().isEmpty()) {
            System.out.println("Using existing token.");
            token = TokenProvider.provideToken(TestUtil.TOKEN.get());
        } else {
            System.out.println("New Token required.");
            TestUtil.assumeFilled(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID);
            token = TokenProvider.provideToken(TestUtil.USERNAME.get(),
                    TestUtil.PASSWORD.get(), TestUtil.ANDROID_ID.get());
            TestUtil.set(TestUtil.TOKEN_KEY, token.getToken());
        }
        new GPlayMusic.Builder().setAuthToken(token).build();
    }

    @Test
    public void getTrackUrl() throws IOException {
        Track track = GPlayMusic.getApiInstance().searchTracks("Sound", 10).get(0);
        assertNotNull(track);
        assertNotNull(track.getTitle());
        assertNotNull(track.getArtist());
        assertNotNull(track.getID());
        String url = track.getStreamURL(StreamQuality.HIGH);
        assertNotNull(url);
        Assert.assertTrue(url.startsWith("http"));
    }

    @Test
    public void listPlaylists() throws IOException {
        List<Playlist> playlists = GPlayMusic.getApiInstance().listPlaylists();
        assertNotNull(playlists);
        assertFalse(playlists.isEmpty());
        for (Playlist playlist : playlists) {
            assertNotNull(playlist);
            assertNotNull(playlist.getName());
            assertNotNull(playlist.getType());
            assertNotNull(playlist.getId());
        }
    }

    @Test
    public void getPrivatePlaylistContent() throws IOException {
        List<Playlist> playlists = GPlayMusic.getApiInstance().listPlaylists();
        assume(playlists);
        Optional<Playlist> privatePlaylistOptional =
                playlists.stream()
                        .filter(p -> p.getType().equals(Playlist.PlaylistType.USER_GENERATED)).findFirst();
        Assume.assumeTrue("Test could not be run. No private playlist found.",
                privatePlaylistOptional.isPresent());
        Playlist privatePlaylist = privatePlaylistOptional.get();
        List<PlaylistEntry> entries = privatePlaylist.getContents(-1);
        assertNotNull(entries);
        for (PlaylistEntry entry : entries) {
            assertNotNull(entry);
        }
    }

    @Test
    public void getSharedPlaylistContent() throws IOException {
        List<Playlist> playlists = GPlayMusic.getApiInstance().listPlaylists();
        assume(playlists);
        Optional<Playlist> sharedPlaylistOptional =
                playlists.stream().filter(p -> p.getType().equals(Playlist.PlaylistType.SHARED)).findFirst();
        Assume.assumeTrue("Test could not be run. No shared playlist found.",
                sharedPlaylistOptional.isPresent());
        Playlist sharedPlaylist = sharedPlaylistOptional.get();
        List<PlaylistEntry> entries = sharedPlaylist.getContents(100);
        assertNotNull(entries);
        for (PlaylistEntry entry : entries) {
            assertNotNull(entry);
        }
    }

    @Test
    public void testStation() throws IOException {
        List<Station> stations = GPlayMusic.getApiInstance().listStations();
        assertNotNull(stations);
        assertFalse(stations.isEmpty());
        Station station = stations.get(0);
        assertNotNull(station);
        assertNotNull(station.getName());
    }

}
