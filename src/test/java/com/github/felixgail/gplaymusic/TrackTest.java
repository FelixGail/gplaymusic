package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

import static com.github.felixgail.gplaymusic.util.TestUtil.assertTracks;
import static org.junit.Assert.assertNotNull;

public class TrackTest extends TestWithLogin {

    @BeforeClass
    public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
        loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
    }

    @Test
    public void getTrackUrl() throws IOException {
        Track track = GPlayMusic.getApiInstance().searchTracks("Sound", 10).get(0);
        assertTracks(track);
        String url = track.getStreamURL(StreamQuality.HIGH);
        assertNotNull(url);
        Assert.assertTrue(url.startsWith("http"));
    }
}
