package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.listennow.ListenNowSituation;
import com.github.felixgail.gplaymusic.model.shema.listennow.Situation;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

public class TestListenNowSituation extends TestWithLogin {

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
                situation.getSituations().size() > 0);
    }

    @Test
    public void testListenNowSituation() throws IOException {
        ListenNowSituation listenNowSituation = GPlayMusic.getApiInstance().getListenNowSituation();
        TestUtil.assume(listenNowSituation, listenNowSituation.getSituations());
        Assume.assumeTrue(listenNowSituation.getSituations().size() > 0);
        for (Situation s : listenNowSituation.getSituations()) {
            testSituation(s);
        }
    }

    private void testSituation(Situation situation) throws IOException {
        Assert.assertNotNull(situation.getDescription());
        Assert.assertNotNull(situation.getResultType());
        Assert.assertNotNull(situation.getTitle());
        if (situation.getStations().isPresent() && situation.getSituations().isPresent()) {
            System.out.println("Situation contains Stations and Situations");
        }
        if (situation.getStations().isPresent()) {
            Assert.assertTrue("Situation contains empty station list", situation.getStations().get().size() > 0);
            for (Station station : situation.getStations().get()) {
                Assert.assertNotNull("Station is null", station);
                TestUtil.testStation(station);
            }
        }
        if (situation.getSituations().isPresent()) {
            for (Situation s : situation.getSituations().get()) {
                testSituation(s);
            }
        }
    }
}
