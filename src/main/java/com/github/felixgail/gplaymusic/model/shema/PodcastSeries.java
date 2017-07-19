package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.shema.snippets.UserPreference;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class PodcastSeries implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.PODCAST_SERIES;

    @Expose
    private List<ArtRef> art;
    @Expose
    private String author;
    @Expose
    private String continuationToken;
    @Expose
    private String copyright;
    @Expose
    private String description;
    @Expose
    private List<PodcastEpisode> episodes;
    @Expose
    private String explicitType;
    @Expose
    private String link;
    @Expose
    private String seriesId;
    @Expose
    private String title;
    @Expose
    private int totalNumEpisodes;
    @Expose
    private UserPreference userPreferences;

    public List<ArtRef> getArt() {
        return art;
    }

    public void setArt(List<ArtRef> art) {
        this.art = art;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public void setContinuationToken(String continuationToken) {
        this.continuationToken = continuationToken;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public void setExplicitType(String explicitType) {
        this.explicitType = explicitType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalNumEpisodes() {
        return totalNumEpisodes;
    }

    public void setTotalNumEpisodes(int totalNumEpisodes) {
        this.totalNumEpisodes = totalNumEpisodes;
    }

    public UserPreference getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreference userPreferences) {
        this.userPreferences = userPreferences;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof PodcastSeries) && ((PodcastSeries) o).getSeriesId().equals(this.seriesId);
    }
}
