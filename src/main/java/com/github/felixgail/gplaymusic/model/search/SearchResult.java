package com.github.felixgail.gplaymusic.model.search;

import com.github.felixgail.gplaymusic.model.search.results.*;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResult implements Serializable{

    @Expose
    private List<Result> entries;

    public SearchResult(){
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

    @Override
    public String toString() {
        return "SearchResults:\n\t" +
                entries.stream().map(e -> e.toString()).collect(Collectors.joining("\n\t"));
    }

}
