package com.github.felixgail.gplaymusic.model.snippets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Optional;

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

  public Optional<String> getSourceTitle() {
    return Optional.ofNullable(sourceTitle);
  }

  public Optional<String> getSourceUrl() {
    return Optional.ofNullable(sourceUrl);
  }

  public Optional<String> getLicenseTitle() {
    return Optional.ofNullable(licenseTitle);
  }

  public Optional<String> getLicenseUrl() {
    return Optional.ofNullable(licenseUrl);
  }
}
