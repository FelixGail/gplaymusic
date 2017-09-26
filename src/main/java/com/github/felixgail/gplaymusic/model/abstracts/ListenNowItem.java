package com.github.felixgail.gplaymusic.model.abstracts;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.util.deserializer.ListenNowItemDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@JsonAdapter(ListenNowItemDeserializer.class)
public abstract class ListenNowItem implements Serializable {
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

    public List<ArtRef> getCompositeArtRefs() {
        return compositeArtRefs;
    }

    public List<ArtRef> getImages() {
        return images;
    }

    public String getSuggestionReason() {
        return suggestionReason;
    }

    public String getSuggestionText() {
        return suggestionText;
    }
}
