package com.github.felixgail.gplaymusic.model.shema.listennow;

import com.github.felixgail.gplaymusic.model.shema.Situation;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ListenNowSituation implements Serializable{

    @Expose
    private String primaryHeader;
    @Expose
    private String subHeader;
    @Expose
    private List<Situation> situations = new LinkedList<>();
    @Expose
    private DistilledContextWrapper distilledContextWrapper = new DistilledContextWrapper();

    public String getPrimaryHeader() {
        return primaryHeader;
    }

    public void setPrimaryHeader(String primaryHeader) {
        this.primaryHeader = primaryHeader;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(String subHeader) {
        this.subHeader = subHeader;
    }

    public List<Situation> getSituations() {
        return situations;
    }

    public void setSituations(List<Situation> situations) {
        this.situations = situations;
    }

    public DistilledContextWrapper getDistilledContextWrapper() {
        return distilledContextWrapper;
    }

    public void setDistilledContextWrapper(DistilledContextWrapper distilledContextWrapper) {
        this.distilledContextWrapper = distilledContextWrapper;
    }

    public class DistilledContextWrapper implements Serializable{
        @Expose
        private String distilledContextToken;

        public String getDistilledContextToken() {
            return distilledContextToken;
        }

        public void setDistilledContextToken(String distilledContextToken) {
            this.distilledContextToken = distilledContextToken;
        }
    }
}
