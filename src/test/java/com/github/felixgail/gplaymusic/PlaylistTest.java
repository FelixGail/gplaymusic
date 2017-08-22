package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.github.felixgail.gplaymusic.util.TestUtil.assume;
import static com.github.felixgail.gplaymusic.util.TestUtil.testPlaylistEntries;
import static org.junit.Assert.*;

public class PlaylistTest extends TestWithLogin {

    @BeforeClass
    public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
        loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
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
        testPlaylistEntries(entries);
        System.out.printf("%d playlist entries found and validated.", entries.size());
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
        testPlaylistEntries(entries);
        System.out.printf("%d playlist entries found and validated.", entries.size());
    }

    @Test
    public void testMutatePlaylist() throws IOException, InterruptedException {
        long testStartTime = System.currentTimeMillis();
        Playlist newPlaylist = Playlist.create("TestPlaylist_" + System.currentTimeMillis(),
                "Playlist created during testing", Playlist.PlaylistShareState.PRIVATE);
        assertNotNull(newPlaylist);
        assertTrue("Newly created Playlist should be empty.",
                newPlaylist.getContents(100).size() == 0);
        System.out.printf("[%ds] new playlist created.\n", timeDifferenceinSeconds(testStartTime));
        List<Track> tracksToAdd = GPlayMusic.getApiInstance().searchTracks("Imagine", 4);
        Assume.assumeTrue("List with exactly 4 songs needed.", tracksToAdd.size() == 4);
        TestUtil.assertTracks(tracksToAdd);
        newPlaylist.addTracks(tracksToAdd);
        System.out.printf("[%ds] 4 tracks added.\n", timeDifferenceinSeconds(testStartTime));
        List<PlaylistEntry> playlistContent = newPlaylist.getContents(-1);
        assertTrue("Playlist should now have 4 entries but has " + playlistContent.size(),
                playlistContent.size() == 4);
        TestUtil.testPlaylistEntries(playlistContent);
        System.out.printf("[%ds] playlist length verified.\n", timeDifferenceinSeconds(testStartTime));
        newPlaylist.delete();
        System.out.printf("[%ds] playlist deleted.\n", timeDifferenceinSeconds(testStartTime));
        List<Playlist> allPlaylists = GPlayMusic.getApiInstance().listPlaylists();
        for (Playlist fromList : allPlaylists) {
            assertFalse("PlaylistFeed should not contain created playlist anymore.",
                    fromList.getId().equals(newPlaylist.getId()));
        }
        System.out.printf("[%ds] playlist deletion tested\n.", timeDifferenceinSeconds(testStartTime));
    }

    private long timeDifferenceinSeconds(long to) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - to);
    }
}
