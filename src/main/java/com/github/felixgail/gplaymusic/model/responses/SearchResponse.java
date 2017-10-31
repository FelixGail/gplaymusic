package com.github.felixgail.gplaymusic.model.responses;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.*;
import com.github.felixgail.gplaymusic.model.listennow.Situation;
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

  public void addApis(GPlayMusic mainApi) {
    for (Result entry : entries) {
      if(entry instanceof Track){
        ((Track)entry).setApi(mainApi.getTrackApi());
      }else if(entry instanceof Station) {
        ((Station)entry).setApi(mainApi.getStationApi());
      }else if(entry instanceof PlaylistEntry){
        ((PlaylistEntry)entry).setApi(mainApi.getPlaylistEntryApi());
      }else if(entry instanceof Playlist){
        ((Playlist)entry).setApi(mainApi.getPlaylistApi());
      }else if(entry instanceof Genre){
        ((Genre)entry).setApi(mainApi.getGenreApi());
      }else if(entry instanceof PodcastSeries) {
        ((PodcastSeries)entry).getEpisodes()
                .ifPresent(l -> l.forEach(e -> e.setApi(mainApi)));
      }
    }
  }

  public String string() {
    return "SearchResults:\n\t" +
        entries.stream().map(Object::toString).collect(Collectors.joining("\n\t"));
  }

}
