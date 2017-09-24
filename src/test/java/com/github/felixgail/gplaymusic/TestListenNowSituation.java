package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.shema.Situation;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowSituation;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

public class TestListenNowSituation extends TestWithLogin{

    @BeforeClass
    public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
        loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
    }

    @Test
    public void testGetListenNowSituation() throws IOException {
        ListenNowSituation situation = GPlayMusic.getApiInstance().getListenNowSituation();
        Assert.assertNotNull("Situation should not be null", situation);
        Assert.assertNotNull("Header should not be empty", situation.getPrimaryHeader());
        Assert.assertNotNull("Sub header should not be empty", situation.getSubHeader());
        Assert.assertNotNull("List of Situations should not be empty", situation.getSituations());
        Assert.assertTrue("ListenNowSituation should contain more than one situation",
                situation.getSituations().size()>0);
    }

    @Test
    public void testSituation() throws IOException {
        ListenNowSituation listenNowSituation = GPlayMusic.getApiInstance().getListenNowSituation();
        TestUtil.assume(listenNowSituation, listenNowSituation.getSituations());
        Assume.assumeTrue(listenNowSituation.getSituations().size()>0);
        Situation situation = listenNowSituation.getSituations().get(0);
        Assert.assertNotNull(situation.getDescription());
        Assert.assertNotNull(situation.getResultType());
        Assert.assertNotNull(situation.getTitle());
        Assert.assertNotNull(situation.getStations());
        Assert.assertTrue(situation.getStations().size()>0);
    }
}
