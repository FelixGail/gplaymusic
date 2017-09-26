package com.github.felixgail.gplaymusic.model.shema.listennow;

import com.github.felixgail.gplaymusic.model.abstracts.ListenNowItem;
import com.github.felixgail.gplaymusic.model.shema.snippets.ProfileImage;
import com.github.felixgail.gplaymusic.model.shema.snippets.StationSeed;
import com.github.felixgail.gplaymusic.util.deserializer.ListenNowStationDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

@JsonAdapter(ListenNowStationDeserializer.class)
public class ListenNowStation extends ListenNowItem {
    @Expose
    @SerializedName("highlight_color")
    private String highlightColor;
    @Expose
    private List<StationSeed> seeds = new LinkedList<>();
    @Expose
    @SerializedName("profile_image")
    private ProfileImage profileImage;
    @Expose
    private String title;

    public String getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(String highlightColor) {
        this.highlightColor = highlightColor;
    }

    public List<StationSeed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<StationSeed> seeds) {
        this.seeds = seeds;
    }

    public void addSeed(StationSeed seed) {
        if (seeds == null) {
            seeds = new LinkedList<>();
        }
        seeds.add(seed);
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
