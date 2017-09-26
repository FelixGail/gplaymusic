package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;
import java.util.List;

public class TestListenNowItem extends TestWithLogin{

    @BeforeClass
    public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
        loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
    }

    @Test
    public void testListItems() throws IOException {
        List<ListenNowItem> items = GPlayMusic.getApiInstance().listListenNowItems();
        Assert.assertNotNull("ListenNowItem list is null", items);
        Assert.assertTrue("ListenNowItem list is empty", items.size()>0);
        items.forEach(item -> {
            Assert.assertNotNull(item);
            Assert.assertNotNull(item.getImages());
            Assert.assertNotNull(item.getCompositeArtRefs());
            Assert.assertNotNull(item.getSuggestionText());
            Assert.assertNotNull(item.getSuggestionReason());
            Assert.assertNotNull(item.getType());
            Assert.assertEquals("Item is not of provided type", item.getType().getType(), item.getClass());
        });
    }

}
