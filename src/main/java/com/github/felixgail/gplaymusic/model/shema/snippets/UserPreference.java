package com.github.felixgail.gplaymusic.model.shema.snippets;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class UserPreference implements Serializable {

    @Expose
    private boolean autoDownload;
    @Expose
    private boolean notifyOnNewEpisode;
    @Expose
    private boolean subscribed;

    public boolean isAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(boolean autoDownload) {
        this.autoDownload = autoDownload;
    }

    public boolean isNotifyOnNewEpisode() {
        return notifyOnNewEpisode;
    }

    public void setNotifyOnNewEpisode(boolean notifyOnNewEpisode) {
        this.notifyOnNewEpisode = notifyOnNewEpisode;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
