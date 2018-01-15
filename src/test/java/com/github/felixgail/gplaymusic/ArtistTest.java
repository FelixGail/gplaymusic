package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.Artist;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;

import static com.github.felixgail.gplaymusic.TestWithLogin.getApi;
import static com.github.felixgail.gplaymusic.TestWithLogin.loginToService;

public class ArtistTest {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  @Test
  public void testRelatedArtists() throws IOException {
    List<Artist> artistList = getApi().search("John", new SearchTypes(ResultType.ARTIST)).getArtists();
    Assert.assertNotNull(artistList);
    Assert.assertNotEquals(0, artistList.size());
    int relArtistNum = 0;
    for(Artist artist : artistList) {
      if(artist.getArtistId().isPresent()) {
        artist = getApi().getArtist(artist.getArtistId().get(), false, 0, 10);
        Assert.assertNotNull(artist);
        if(artist.getRelatedArtists().isPresent()){
          relArtistNum += 1;
          Assert.assertNotEquals(0, artist.getRelatedArtists().get().size());
        }
      }
    }
    Assert.assertNotEquals(
        String.format("No Artist (of %d) had a list of related artists", artistList.size()), 0,
        relArtistNum);
    System.out.printf("%d of %d artists had a list of related artists\n", relArtistNum, artistList.size());
  }


}
