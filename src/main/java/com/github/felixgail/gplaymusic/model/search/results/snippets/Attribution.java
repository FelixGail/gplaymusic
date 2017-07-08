package com.github.felixgail.gplaymusic.model.search.results.snippets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attribution {

    @Expose
    @SerializedName("source_title")
    private String sourceTitle;
    @Expose
    @SerializedName("source_url")
    private String sourceUrl;
    @Expose
    @SerializedName("license_title")
    private String licenseTitle;
    @Expose
    @SerializedName("license_url")
    private String licenseUrl;

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getLicenseTitle() {
        return licenseTitle;
    }

    public void setLicenseTitle(String licenseTitle) {
        this.licenseTitle = licenseTitle;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }
}
