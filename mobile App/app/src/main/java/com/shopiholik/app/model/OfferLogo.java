package com.shopiholik.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author agrawroh
 * @version v1.0
 */
public class OfferLogo {
    @SerializedName("name")
    private String name;

    @SerializedName("domain")
    private String domain;

    @SerializedName("logo")
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
