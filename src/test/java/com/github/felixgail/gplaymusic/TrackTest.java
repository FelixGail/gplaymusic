package com.github.felixgail.gplaymusic;

import static com.github.felixgail.gplaymusic.util.TestUtil.assertTracks;
import static com.github.felixgail.gplaymusic.util.TestUtil.assume;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNotNull;

import com.github.felixgail.gplaymusic.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.Playlist;
import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.Video;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.util.TestUtil;
import com.github.felixgail.gplaymusic.util.TestUtil.Property;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.*;
import svarzee.gps.gpsoauth.Gpsoauth;

public class TrackTest extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  @Test
  public void getTrackUrl() throws IOException {
    List<Track> tracks = getApi().getTrackApi().search("", 10);
    assertTracks(tracks);
    for (Track track : tracks) {
      URL url = null;
      try {
        url = track.getStreamURL(StreamQuality.HIGH);
      } catch (NetworkException e) {
        System.out.printf("Network Exception received for %s by %s (%s).\n",
            track.getTitle(), track.getArtist(), track.getID());
        e.printStackTrace();
      }
      assertNotNull(url);
      if (track.getVideo().isPresent()) {
        Video video = track.getVideo().get();
        assertNotNull(video.getId());
        assertNotNull(video.getURL());
      }
      System.out.printf("Valid URL received for %s by %s (%s).\n",
          track.getTitle(), track.getArtist(), track.getID());
    }
  }

  @Ignore
  @Test
  public void incrementPlaycount() throws IOException, InterruptedException {
    Track track = getApi().getTrackApi().search("Sound", 10).get(0);
    assume(track);
    assume(track.getPlayCount().isPresent());
    int playcount = track.getPlayCount().getAsInt();
    int inc = 2;
    track.incrementPlaycount(inc);
    Assert.assertEquals("Playcount was not increased locally.",
        playcount + inc, track.getPlayCount().getAsInt());
    //Wait for remote to update playcount.
    sleep(1000);
    Track trackNew = getApi().getTrackApi().search("Sound", 10).get(0);
    Assume.assumeTrue("Newly fetched track should equal original track",
        track.getID().equals(trackNew.getID()));
    Assert.assertEquals("Playcount was not increased at remote location.",
        playcount + inc, trackNew.getPlayCount().getAsInt());
  }

  @Test
  public void testDownloadSearch() throws IOException {
    Track track = getApi().getTrackApi().search("Sound", 10).get(0);
    assume(track);
    TestUtil.testDownload("searchedTrack.mp3", track);
  }

  @Test
  public void testPlaylistDownload() throws IOException {
    //PlaylistID with key test.track.playlist should be a playlist conatining both store and library tracks.
    //To find it use printAllPlaylists();
    Property testPlaylist = TestUtil.get("test.track.playlist");
    TestUtil.assume(testPlaylist);
    Playlist playlist = getApi().getPlaylistApi()
        .getPlaylist(testPlaylist.get());
    Track track;
    for (PlaylistEntry entry : playlist.getContents(-1)) {
      track = entry.getTrack();
      System.out.printf("Preparing to download '%s'\n", track.getTitle());
      TestUtil.testDownload(track.getTitle(), track);
      System.out.println("\t - Completed");
    }
  }

  private void printAllPlaylists() throws IOException {
    getApi().getPlaylistApi().listPlaylists()
        .forEach(p -> System.out.printf("%s: %s\n", p.getName(), p.getId()));
  }
}
