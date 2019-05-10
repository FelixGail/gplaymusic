package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.Video;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.util.TestUtil;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

public class VideoTest extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  @Test
  public void testVideo() throws IOException {
    //Create a query that should contain video elements
    List<Video> videos = getApi()
        .search("All or Nothing Theory", new SearchTypes(Video.RESULT_TYPE)).getVideos();
    TestUtil.assume(videos);
    Assert.assertTrue("List of videos is empty.", videos.size() > 0);
    System.out.printf("search for videos yielded %d results\n", videos.size());
    videos.forEach(v -> {
      Assert.assertNotNull("Video is null", v);
      Assert.assertNotNull("ID is null", v.getId());
      Assert.assertTrue("ID is empty", !v.getId().isEmpty());
    });
  }

  @Test
  public void testVideoInTrack() throws IOException {
    List<Track> tracks = getApi()
            .search("All or Nothing Theory", new SearchTypes(Track.RESULT_TYPE)).getTracks();
    Assume.assumeFalse("List of tracks is empty", tracks.isEmpty());
    boolean foundVideo = false;
    for(Track track : tracks) {
      if (track.getVideo().isPresent()) {
        foundVideo = true;
      }
    }
    Assert.assertTrue("No track element contained a video attribute", foundVideo);
  }
}
