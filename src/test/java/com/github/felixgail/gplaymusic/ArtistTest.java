package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.Artist;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

import static com.github.felixgail.gplaymusic.TestWithLogin.getApi;
import static com.github.felixgail.gplaymusic.TestWithLogin.loginToService;

public class ArtistTest {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  @Test
  public void testRelatedArtists() throws IOException {
    Artist artist = getApi().getArtist(TestUtil.get("test.artist").get(), false, 0, 10);
    Assert.assertNotNull(artist);
    Assert.assertTrue(artist.getRelatedArtists().isPresent());
    Assert.assertNotEquals(0, artist.getRelatedArtists().get().size());
  }


}
