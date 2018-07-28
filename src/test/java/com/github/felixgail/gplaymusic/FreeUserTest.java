package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import java.io.IOException;
import java.util.List;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

public class FreeUserTest extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.FREE_USERNAME, TestUtil.FREE_PASSWORD,
        TestUtil.FREE_ANDROID_ID, TestUtil.FREE_TOKEN);
  }

  @Test
  public void testLibrarySongDownload() throws IOException {
    List<Track> tracks = getApi().getTrackApi().getLibraryTracks();
    Assume.assumeTrue("This user has no library tracks", tracks.size() > 0);
    Track track = tracks.get(0);
    TestUtil.testDownload("FreeUser_LibraryTrackDownloadTest.mp3", track);
  }

}
