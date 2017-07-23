package com.github.felixgail.gplaymusic.model.requestbodies;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Mutator implements Serializable {
    @Expose
    private Map<String,Object> mutations = new HashMap<>();

    public Map<String, Object> getMutations() {
        return mutations;
    }

    public void setMutations(Map<String, Object> mutations) {
        this.mutations = mutations;
    }

    public void addMutation(String key, Object value) {
        mutations.put(key,value);
    }

    public Object removeMutation(String key) {
        return mutations.remove(key);
    }
}