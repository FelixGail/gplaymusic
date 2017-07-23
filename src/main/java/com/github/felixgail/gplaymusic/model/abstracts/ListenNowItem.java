package com.github.felixgail.gplaymusic.model.abstracts;

import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.util.deserializer.ListenNowItemDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@JsonAdapter(ListenNowItemDeserializer.class)
public abstract class ListenNowItem implements Serializable{
    @Expose
    private List<ArtRef> compositeArtRefs;
    @Expose
    private List<ArtRef> images;
    @Expose
    @SerializedName("suggestion_reason")
    private String suggestionReason;
    @Expose
    @SerializedName("suggestion_text")
    private String suggestionText;
    @Expose
    private ResultType type;

    public List<ArtRef> getCompositeArtRefs() {
        return compositeArtRefs;
    }

    public void setCompositeArtRefs(List<ArtRef> compositeArtRefs) {
        this.compositeArtRefs = compositeArtRefs;
    }

    public List<ArtRef> getImages() {
        return images;
    }

    public void setImages(List<ArtRef> images) {
        this.images = images;
    }

    public String getSuggestionReason() {
        return suggestionReason;
    }

    public void setSuggestionReason(String suggestionReason) {
        this.suggestionReason = suggestionReason;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }
}
