package com.github.felixgail.gplaymusic.util;

import com.github.felixgail.gplaymusic.model.shema.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import org.junit.Assume;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

public class TestUtil {
  public static final String USERNAME_KEY = "auth.username";
  public static final String PASSWORD_KEY = "auth.password";
  public static final String ANDROID_ID_KEY = "auth.android_id";
  public static final String TOKEN_KEY = "auth.token";
  public static final Property USERNAME;
  public static final Property PASSWORD;
  public static final Property ANDROID_ID;
  public static final Property TOKEN;
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
    USERNAME = new Property(USERNAME_KEY);
    PASSWORD = new Property(PASSWORD_KEY);
    ANDROID_ID = new Property(ANDROID_ID_KEY);
    TOKEN = new Property(TOKEN_KEY);
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
          String.format("Test has been skipped. Required value \"%s\" is missing.", property.getKey()),
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
