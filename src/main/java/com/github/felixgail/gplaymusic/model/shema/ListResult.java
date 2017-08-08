package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ListResult<T> implements Serializable {

    @Expose
    @SerializedName(value = "data",
            alternate = {"series", "listennow_items", "mutate_response"})
    private InnerData<T> data = new InnerData<>();

    public List<T> toList() {
        return data.items;
    }

    private class InnerData<U> implements Serializable {
        @Expose
        @SerializedName(value = "items",
                alternate = "stations")
        private List<U> items = new LinkedList<>();
    }
}
