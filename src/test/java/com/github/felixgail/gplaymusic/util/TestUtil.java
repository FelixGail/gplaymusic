package com.github.felixgail.gplaymusic.util;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.Station;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Assume;

public class TestUtil {

  public static final Property USERNAME;
  public static final Property PASSWORD;
  public static final Property ANDROID_ID;
  public static final Property TOKEN;
  public static final Property FREE_USERNAME;
  public static final Property FREE_PASSWORD;
  public static final Property FREE_ANDROID_ID;
  public static final Property FREE_TOKEN;
  private static final Properties PROPS;
  private static final String RESOURCE = "gplaymusic.properties";

  static {
    PROPS = new Properties();
    InputStream in = TestUtil.class
        .getClassLoader()
        .getResourceAsStream(RESOURCE);

    try {
      PROPS.load(in);
      in.close();
    } catch (IOException | NullPointerException e) {
      System.out.println("gplaymusic.properties file not found. Most test will not be executed.");
    }
    USERNAME = new Property("auth.username");
    PASSWORD = new Property("auth.password");
    ANDROID_ID = new Property("auth.android_id");
    TOKEN = new Property("auth.token");

    FREE_USERNAME = new Property("auth.free.username");
    FREE_PASSWORD = new Property("auth.free.password");
    FREE_ANDROID_ID = new Property("auth.free.android_id");
    FREE_TOKEN = new Property("auth.free.token");
  }

  public static Property get(String s) {
    return new Property(s);
  }

  public static void set(String key, String value) throws IOException {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(TestUtil.class.getClassLoader().getResource(RESOURCE).getFile());
      PROPS.setProperty(key, value);
      PROPS.store(writer, null);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  public static void assume(Property... properties) {
    for (Property property : properties) {
      Assume.assumeTrue(
          String.format("Test has been skipped. Required value \"%s\" is missing.",
              property.getKey()),
          property.isValid());
    }
  }

  public static void assumeFilled(Property... properties) {
    assume(properties);
    for (Property property : properties) {
      Assume.assumeTrue(
          String.format("Test has been skipped. \"%s\" is not allowed to be an empty String.",
              property.getKey()),
          !property.get().isEmpty());
    }
  }

  public static void assume(Object... objects) {
    for (Object object : objects) {
      Assume.assumeNotNull(
          String.format("Test has been skipped. Required object \"%s\" is null.", object),
          object);
    }
  }

  public static void testPlaylistEntries(List<PlaylistEntry> entries) throws IOException {
    for (PlaylistEntry entry : entries) {
      assertNotNull(entry);
      assertNotNull(entry.getAbsolutePosition());
      assertNotNull(entry.getTrackId());
      assertTracks(entry.getTrack());
    }
  }

  public static void assertTracks(Track... tracks) {
    assertTracks(Arrays.asList(tracks));
  }

  public static void assertTracks(List<Track> tracks) {
    for (Track track : tracks) {
      assertNotNull(track);
      assertNotNull(track.getTitle());
      assertNotNull(track.getArtist());
      assertNotNull(track.getID());
    }
  }

  public static void testStation(Station station) throws IOException {
    assertNotNull(station);
    List<Track> tracks = station.getTracks(null, true, false);
    assertNotNull(tracks);
    assertTracks(tracks);
    assertTrue("Empty track list returned", tracks.size() > 0);
  }

  public static int containsDoubledTracks(List<Track> list1, List<Track> list2) {
    int doubledTracks = 0;
    if (list1 != null && list2 != null) {
      for (Track fromList1 : list1) {
        for (Track fromList2 : list2) {
          if (fromList1.getID().equals(fromList2.getID())) {
            System.out.printf("Equal Songs:\nSong1: %s (%s)\nSong2: %s (%s)",
                fromList1.getTitle(), fromList1.getID(),
                fromList2.getTitle(), fromList2.getID());
            doubledTracks++;
          }
        }
      }
    }
    return doubledTracks;
  }

  public static void testDownload(String fileName, Track track) throws IOException {
    Path path = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"), fileName);
    track.download(StreamQuality.LOW, path);
    File file = path.toFile();
    Assert.assertTrue("File does not exist", file.exists());
    Assert.assertEquals("Is not an audio file", new Tika().detect(file), "audio/mpeg");
  }

  public static class Property {

    private String key;

    public Property(String k) {
      this.key = k;
    }

    public String getKey() {
      return key;
    }

    @Override
    public String toString() {
      return get();
    }

    public boolean isValid() {
      return (get() != null);
    }

    public String get() {
      return PROPS.getProperty(getKey());
    }
  }

}
