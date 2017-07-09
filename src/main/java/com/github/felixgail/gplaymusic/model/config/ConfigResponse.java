package com.github.felixgail.gplaymusic.model.config;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConfigResponse implements Serializable {

    @Expose
    private Map<String, String> config = new HashMap<>();

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public String get(String key) {
        return config.get(key);
    }

    public boolean getBool(String key) {
        return config.containsKey(key) && Boolean.parseBoolean(config.get(key));
    }

    public boolean containsKey(String key) {
        return config.containsKey(key);
    }

    public void put(String key, String value) {
        config.put(key, value);
    }
}
