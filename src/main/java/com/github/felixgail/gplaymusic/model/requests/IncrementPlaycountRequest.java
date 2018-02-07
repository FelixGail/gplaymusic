package com.github.felixgail.gplaymusic.model.requests;

import com.github.felixgail.gplaymusic.model.Track;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.validation.constraints.NotNull;

public class IncrementPlaycountRequest implements Serializable {

  @Expose
  @SerializedName("track_stats")
  private List<Inner> trackStats;

  public IncrementPlaycountRequest(int playCount, @NotNull Track track) {
    this.trackStats = Collections.singletonList(new Inner(playCount, track));
  }

  private class Inner {

    @Expose
    private String id;
    @Expose
    @SerializedName("incremental_plays")
    private int plays;
    @Expose
    @SerializedName("last_play_time_millis")
    private String lastPlayedMillis;
    @Expose
    private int type;
    @Expose
    @SerializedName("track_events")
    private List<Event> events;

    Inner(int playCount, @NotNull Track track) {
      long timeStampMillis = System.currentTimeMillis();

      this.id = track.getID();
      this.plays = playCount;
      this.lastPlayedMillis = String.valueOf(timeStampMillis);
      this.type = track.getID().startsWith("T") ? 2 : 1;

      events = new LinkedList<>();
      String timestampMicros = String.valueOf(timeStampMillis * 1000);
      for (int i = 0; i < playCount; i++) {
        events.add(new Event(timestampMicros));
      }
    }
  }

  private class Event {

    @Expose
    @SerializedName("context_type")
    private int contextType = 1;
    @Expose
    @SerializedName("event_timestamp_micros")
    private String timestamp;
    @Expose
    @SerializedName("event_type")
    private int eventType = 2;

    Event(String timestampMicros) {
      this.timestamp = timestampMicros;
    }
  }
}
