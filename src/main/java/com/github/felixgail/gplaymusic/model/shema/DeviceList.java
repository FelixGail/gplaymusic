package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;

public class DeviceList extends LinkedList<DeviceList.DeviceInfo> implements Serializable {

    public class DeviceInfo implements Serializable {
        @Expose
        private String id;
        @Expose
        private String friendlyName;
        @Expose
        private String type;
        @Expose
        private String lastAccessedTimeMs;

        public String getId() {
            if(id.matches("\\A0x\\S{16}\\z")) {
                id = id.substring(2);
            }
            return id;
        }

        public String getFriendlyName() {
            return friendlyName;
        }

        public String getType() {
            return type;
        }

        public String getLastAccessedTimeMs() {
            return lastAccessedTimeMs;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setFriendlyName(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setLastAccessedTimeMs(String lastAccessedTimeMs) {
            this.lastAccessedTimeMs = lastAccessedTimeMs;
        }
    }

    public DeviceList() {
        super();
    }

    public DeviceInfo getFirstAndroid() {
        for (DeviceInfo info : this) {
            if (info.type.equals("ANDROID")) {
                return info;
            }
        }
        throw new RuntimeException("No linked Android device found!");
    }
}
