package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.model.listennow.ListenNowAlbum;
import com.github.felixgail.gplaymusic.model.listennow.ListenNowItem;
import com.github.felixgail.gplaymusic.model.listennow.ListenNowStation;
import com.github.felixgail.gplaymusic.util.TestUtil;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

public class TestListenNowItem extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  @Test
  public void testListItems() throws IOException {
    List<ListenNowItem> items = getApi().listListenNowItems();
    Assert.assertNotNull("ListenNowItem list is null", items);
    Assert.assertTrue("ListenNowItem list is empty", items.size() > 0);
    items.forEach(item -> {
      Assert.assertNotNull(item);
      Assert.assertNotNull(item.getImages());
      Assert.assertNotNull(item.getCompositeArtRefs());
      Assert.assertNotNull(item.getSuggestionText());
      Assert.assertNotNull(item.getSuggestionReason());
      if (item instanceof ListenNowAlbum) {
        ListenNowAlbum album = (ListenNowAlbum) item;
        Assert.assertNotNull(album.getArtistID());
        Assert.assertNotNull(album.getArtistName());
        Assert.assertNotNull(album.getArtistProfileImage());
        Assert.assertNotNull(album.getExplicitType());
        ListenNowAlbum.MetajamID id = album.getId();
        Assert.assertNotNull(id.getArtist());
        Assert.assertNotNull(id.getMetajamCompactKey());
        Assert.assertNotNull(id.getTitle());
      } else if (item instanceof ListenNowStation) {
        ListenNowStation station = (ListenNowStation) item;
        Assert.assertNotNull(station.getSeeds());
        Assert.assertNotNull(station.getTitle());
        Assert.assertTrue(station.getSeeds().size() > 0);
      }
    });
  }

}
