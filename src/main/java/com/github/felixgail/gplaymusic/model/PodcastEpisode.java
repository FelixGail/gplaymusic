package com.github.felixgail.gplaymusic.model;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.exceptions.NetworkException;
import com.github.felixgail.gplaymusic.model.enums.Provider;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class PodcastEpisode extends Signable implements Serializable, Model {

  @Expose
  private List<ArtRef> art;
  @Expose
  private String author;
  @Expose
  private boolean deleted;
  @Expose
  private String description;
  @Expose
  private String durationMillis;
  @Expose
  private String episodeId;
  @Expose
  private String explicitType;
  @Expose
  private String fileSize;
  @Expose
  private String playbackPositionMillis;
  @Expose
  private String publicationTimestampMillis;
  @Expose
  private String seriesId;
  @Expose
  private String seriesTitle;
  @Expose
  private String title;

  private GPlayMusic api;

  public Optional<List<ArtRef>> getArt() {
    return Optional.ofNullable(art);
  }

  public Optional<String> getAuthor() {
    return Optional.ofNullable(author);
  }

  public boolean isDeleted() {
    return deleted;
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public String getDurationMillis() {
    return durationMillis;
  }

  @Override
  public String getID() {
    return episodeId;
  }

  public String getExplicitType() {
    return explicitType;
  }

  public String getFileSize() {
    return fileSize;
  }

  public Optional<String> getPlaybackPositionMillis() {
    return Optional.ofNullable(playbackPositionMillis);
  }

  public Optional<String> getPublicationTimestampMillis() {
    return Optional.ofNullable(publicationTimestampMillis);
  }

  public String getSeriesId() {
    return seriesId;
  }

  public String getSeriesTitle() {
    return seriesTitle;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public Signature getSignature() {
    return super.createSignature(this.getID());
  }

  /**
   * Returns a URL to download a podcast episode in set quality. URL will only be valid for 1
   * minute. You will likely need to handle redirects. <br> <b>TODO: Find out if free users have
   * access to podcast episodes</b>
   *
   * @param quality quality of the stream
   * @return temporary url to the title
   * @throws IOException Throws an IOException on severe failures (no internet connection...) or a
   * {@link NetworkException} on request failures.
   */
  @Override
  public URL getStreamURL(StreamQuality quality)
      throws IOException {
    return urlFetcher(api, quality, Provider.PODCAST, EMPTY_MAP);
  }

  @Override
  public GPlayMusic getApi() {
    return api;
  }

  @Override
  public void setApi(GPlayMusic api) {
    this.api = api;
  }
}
