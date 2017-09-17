package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;
import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.github.felixgail.gplaymusic.util.TestUtil.assertTracks;
import static com.github.felixgail.gplaymusic.util.TestUtil.assume;
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

    @Test
    public void incrementPlaycount() throws IOException {
        Track track = GPlayMusic.getApiInstance().searchTracks("Sound", 10).get(0);
        assume(track);
        int playcount = track.getPlayCount();
        int inc = 2;
        track.incrementPlaycount(inc);
        Assert.assertEquals("Playcount was not increased locally.",
                playcount + inc, track.getPlayCount());
        Track trackNew = GPlayMusic.getApiInstance().searchTracks("Sound", 10).get(0);
        Assume.assumeTrue("Newly fetched track should equal original track",
                track.getID().equals(trackNew.getID()));
        Assert.assertEquals("Playcount was not increased at remote location.",
                playcount + inc, trackNew.getPlayCount());
    }

    @Test
    public void download() throws IOException {
        Track track = GPlayMusic.getApiInstance().searchTracks("Sound", 10).get(0);
        assume(track);
        Path path = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"), "test.mp3");
        track.download(StreamQuality.HIGH, path);
        File file = path.toFile();
        Assert.assertTrue("File does not exist", file.exists());
        Assert.assertEquals("Is not an audio file", new Tika().detect(file), "audio/mpeg");
    }
}
