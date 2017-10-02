package com.github.felixgail.gplaymusic.model.search;

import com.github.felixgail.gplaymusic.model.interfaces.Result;
import com.github.felixgail.gplaymusic.model.shema.Album;
import com.github.felixgail.gplaymusic.model.shema.Artist;
import com.github.felixgail.gplaymusic.model.shema.Playlist;
import com.github.felixgail.gplaymusic.model.shema.PodcastSeries;
import com.github.felixgail.gplaymusic.model.shema.Station;
import com.github.felixgail.gplaymusic.model.shema.Track;
import com.github.felixgail.gplaymusic.model.shema.Video;
import com.github.felixgail.gplaymusic.model.shema.listennow.Situation;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Add result scores
public class SearchResponse implements Serializable {

  @Expose
  private List<Result> entries = new LinkedList<>();

  public SearchResponse() {
    entries = new LinkedList<>();
  }

  public List<Result> getEntries() {
    return entries;
  }

  public List<Track> getTracks() {
    return entries.stream().filter(Track.class::isInstance).map(Track.class::cast).collect(Collectors.toList());
  }

  public List<Artist> getArtists() {
    return entries.stream().filter(Artist.class::isInstance).map(Artist.class::cast).collect(Collectors.toList());
  }

  public List<Album> getAlbums() {
    return entries.stream().filter(Album.class::isInstance).map(Album.class::cast).collect(Collectors.toList());
  }

  public List<Playlist> getPlaylists() {
    return entries.stream().filter(Playlist.class::isInstance).map(Playlist.class::cast).collect(Collectors.toList());
  }

  public List<Station> getStations() {
    return entries.stream().filter(Station.class::isInstance).map(Station.class::cast).collect(Collectors.toList());
  }

  public List<Situation> getSituations() {
    return entries.stream().filter(Situation.class::isInstance).map(Situation.class::cast).collect(Collectors.toList());
  }

  public List<Video> getVideos() {
    return entries.stream().filter(Video.class::isInstance).map(Video.class::cast).collect(Collectors.toList());
  }

  public List<PodcastSeries> getPodcastSeries() {
    return entries.stream().filter(PodcastSeries.class::isInstance).map(PodcastSeries.class::cast).collect(Collectors.toList());
  }

  public String string() {
    return "SearchResults:\n\t" +
        entries.stream().map(Object::toString).collect(Collectors.joining("\n\t"));
  }

}
