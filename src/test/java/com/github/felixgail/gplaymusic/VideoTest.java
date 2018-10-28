package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.Video;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.util.TestUtil;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

public class VideoTest extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  //Feature seems to be desabled at the moment.
  @Test
  public void testVideo() throws IOException {
    List<Video> videos = getApi()
        .search("", new SearchTypes(Video.RESULT_TYPE)).getVideos();
    TestUtil.assume(videos);
    Assume.assumeTrue("List of videos is empty.", videos.size() > 0);
    System.out.printf("Empty search for videos yielded %d results\n", videos.size());
    videos.forEach(v -> {
      Assert.assertNotNull("Video is null", v);
      Assert.assertNotNull("ID is null", v.getId());
      Assert.assertTrue("ID is empty", !v.getId().isEmpty());
    });
  }
}
