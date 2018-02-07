package com.github.felixgail.gplaymusic.model.requests;


import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;

public class TimeZoneOffset implements Serializable {

  @Expose
  private RequestSignal requestSignals;

  public TimeZoneOffset(String offset) {
    this.requestSignals = new RequestSignal(offset);
  }

  public TimeZoneOffset() {
    this.requestSignals = new RequestSignal(getLocalOffset());
  }

  private String getLocalOffset() {
    return String.valueOf(ZoneOffset.systemDefault().getRules().getOffset(Instant.now())
        .getTotalSeconds());
  }

  private class RequestSignal implements Serializable {

    @Expose
    private String timeZoneOffsetSecs;

    public RequestSignal(String offset) {
      this.timeZoneOffsetSecs = offset;
    }
  }
}
