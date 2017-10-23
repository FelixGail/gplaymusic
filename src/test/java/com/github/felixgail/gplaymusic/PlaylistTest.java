package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.Arrays;
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
    System.out.println(privatePlaylist.getId());
    List<PlaylistEntry> entries = privatePlaylist.getContents(-1);
    assertNotNull(entries);
    testPlaylistEntries(entries);
    System.out.printf("%d playlist entries found and validated.\n", entries.size());
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
    System.out.printf("%d playlist entries found and validated.\n", entries.size());
  }

  @Test
  public void testMutatePlaylist() throws IOException {
    long testStartTime = System.currentTimeMillis();
    Playlist newPlaylist = null;
    try {
      newPlaylist = createPlaylist();
      System.out.printf("[%ds] new playlist created: %s\n",
          timeDifferenceinSeconds(testStartTime), newPlaylist.getId());
      addTracksToPlaylist(newPlaylist, 4);
      System.out.printf("[%ds] 4 tracks added.\n", timeDifferenceinSeconds(testStartTime));
      List<PlaylistEntry> playlistContent = newPlaylist.getContents(-1);
      assertEquals("Playlist should now have 4 entries but has " + playlistContent.size(),
          4, playlistContent.size());
      TestUtil.testPlaylistEntries(playlistContent);
      System.out.printf("[%ds] playlist length verified.\n", timeDifferenceinSeconds(testStartTime));
      PlaylistEntry entry2 = playlistContent.remove(2);
      PlaylistEntry entry0 = playlistContent.remove(0);
      newPlaylist.removeEntries(entry0, entry2);
      playlistContent = newPlaylist.getContents(-1);
      containsEntry(playlistContent, entry2, entry0);
      System.out.printf("[%ds] 2 entries removal verified from cache.\n", timeDifferenceinSeconds(testStartTime));
      Playlist.updateCache();
      containsEntry(newPlaylist.getContents(-1), entry0, entry2);
      System.out.printf("[%ds] 2 entries removal verified after refresh.\n", timeDifferenceinSeconds(testStartTime));
    } catch (IOException e) {
      Assert.fail("An Error occurred while testing:\n" + e);
    } finally {
      if (newPlaylist != null) {
        newPlaylist.delete();
        System.out.printf("[%ds] playlist deleted.\n", timeDifferenceinSeconds(testStartTime));
        List<Playlist> allPlaylists = GPlayMusic.getApiInstance().listPlaylists();
        Playlist finalNewPlaylist = newPlaylist;
        allPlaylists.forEach(p -> assertFalse("PlaylistFeed should not contain created playlist anymore.",
            p.getId().equals(finalNewPlaylist.getId())));
        System.out.printf("[%ds] playlist deletion tested\n", timeDifferenceinSeconds(testStartTime));
      }
    }
  }

  private void addTracksToPlaylist(Playlist playlist, int count) throws IOException {
    List<Track> tracksToAdd = GPlayMusic.getApiInstance().searchTracks("Imagine", count);
    Assume.assumeTrue("List with exactly 4 songs needed.", tracksToAdd.size() == count);
    TestUtil.assertTracks(tracksToAdd);
    playlist.addTracks(tracksToAdd);
  }

  private Playlist createPlaylist() throws IOException {
    Playlist newPlaylist = Playlist.create("TestPlaylist_" + System.currentTimeMillis(),
        "Playlist created during testing", Playlist.PlaylistShareState.PRIVATE);
    assertNotNull(newPlaylist);
    assertTrue("Newly created Playlist should be empty.",
        newPlaylist.getContents(100).size() == 0);
    List<Playlist> allPlaylists = GPlayMusic.getApiInstance().listPlaylists();
    boolean contains = false;
    for (Playlist fromList : allPlaylists) {
      if (fromList.getId().equals(newPlaylist.getId())) {
        contains = true;
        break;
      }
    }
    assertTrue("List of Playlists does not contain newly created Playlist", contains);
    return newPlaylist;
  }

  @Test
  public void testMoveEntries() throws IOException {
    Playlist playlist = null;
    try {
      playlist = createPlaylist();
      addTracksToPlaylist(playlist, 4);
      List<PlaylistEntry> playlistContent = playlist.getContents(-1);
      PlaylistEntry entry3 = playlistContent.get(3);
      PlaylistEntry entry2 = playlistContent.get(2);
      PlaylistEntry entry1 = playlistContent.get(1);
      PlaylistEntry entry0 = playlistContent.get(0);
      entry1.move(entry3, null);
      assertEquals("Entry1 has not been moved to last position in playlist", 3,
          playlist.getContents(-1).indexOf(entry1));
      entry3.move(null, entry0);
      assertEquals("Entry3 has not been moved to first position in playlist", 0,
          playlist.getContents(-1).indexOf(entry3));
      entry2.move(entry3, entry0);
      assertEquals("Entry1 has not been moved to second position in playlist", 1,
          playlist.getContents(-1).indexOf(entry2));
      List<PlaylistEntry> newOrder = Arrays.asList(entry3, entry2, entry0, entry1);
      Playlist.updateCache();
      playlistContent = playlist.getContents(-1);
      for (int i = 0; i < 4; i++) {
        assertEquals("Different PlaylistEntry expected at position " + i,
            newOrder.get(i).getId(), playlistContent.get(i).getId());
      }
    } catch (IOException e) {
      Assert.fail("An error occured while testing: \n" + e);
    } finally {
      if (playlist != null) {
        playlist.delete();
      }
    }
  }

  private void containsEntry(List<PlaylistEntry> playlistContent, PlaylistEntry... entries) {
    assertEquals("PlaylistLength should be 2", 2, playlistContent.size());
    playlistContent.forEach(p -> assertTrue("Playlist still contains a removed entry",
        Arrays.stream(entries).filter(e -> p.getId().equals(e.getId())).count() == 0));
  }

  private long timeDifferenceinSeconds(long to) {
    return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - to);
  }
}
