package com.github.felixgail.gplaymusic.model.requestbodies;


import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TimeZoneOffset implements Serializable {
    @Expose
    private RequestSignal requestSignals;

    public TimeZoneOffset(String offset) {
        this.requestSignals = new RequestSignal(offset);
    }

    public TimeZoneOffset() {
        this.requestSignals = new RequestSignal("0");
    }

    private class RequestSignal implements Serializable {
        @Expose
        private String timeZoneOffsetSecs;

        public RequestSignal(String offset) {
            this.timeZoneOffsetSecs = offset;
        }
    }
}
