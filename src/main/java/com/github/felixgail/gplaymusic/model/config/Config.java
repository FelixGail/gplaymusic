package com.github.felixgail.gplaymusic.model.config;

import com.github.felixgail.gplaymusic.model.enums.SubscriptionType;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Config implements Serializable {

    @Expose
    private Map<String, String> map = new HashMap<>();
    private Locale locale = Locale.US;
    private String androidID;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> config) {
        this.map = config;
    }

    public String get(String key) {
        return map.get(key);
    }

    public boolean getBool(String key) {
        return map.containsKey(key) && Boolean.parseBoolean(map.get(key));
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public SubscriptionType getSubscription() {
        return getBool("isNautilusUser") ? SubscriptionType.ALL_ACCESS : SubscriptionType.FREE;
    }

    public String getAndroidID() {
        return androidID;
    }

    public void setAndroidID(String androidID) {
        this.androidID = androidID;
    }
}
