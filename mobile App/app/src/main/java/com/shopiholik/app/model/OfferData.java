package com.shopiholik.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author agrawroh
 * @version v1.0
 */
public class OfferData implements Parcelable {
    private String brandName;
    private String brandeImageURL;
    private String brandeClassification;
    private String offerArrivalDate;
    private String offerPercentage;
    private String offerExpiry;
    private String couponCode;

    public OfferData() {
        /* Do Nothing */
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandeImageURL() {
        return brandeImageURL;
    }

    public void setBrandeImageURL(String brandeImageURL) {
        this.brandeImageURL = brandeImageURL;
    }

    public String getBrandeClassification() {
        return brandeClassification;
    }

    public void setBrandeClassification(String brandeClassification) {
        this.brandeClassification = brandeClassification;
    }

    public String getOfferArrivalDate() {
        return offerArrivalDate;
    }

    public void setOfferArrivalDate(String offerArrivalDate) {
        this.offerArrivalDate = offerArrivalDate;
    }

    public String getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public String getOfferExpiry() {
        return offerExpiry;
    }

    public void setOfferExpiry(String offerExpiry) {
        this.offerExpiry = offerExpiry;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object oData) {
        if (oData == this) {
            return true;
        }

        if (!(oData instanceof OfferData)) {
            return false;
        }

        return this.getBrandName().equals(((OfferData) oData).getBrandName());
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Brand Name: ").append(brandName)
                .append(" | Brand Classification: ").append(brandeClassification).toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeString(brandeImageURL);
        dest.writeString(brandeClassification);
        dest.writeString(offerArrivalDate);
        dest.writeString(offerPercentage);
        dest.writeString(offerExpiry);
        dest.writeString(couponCode);
    }

    public static final Parcelable.Creator<OfferData> CREATOR
            = new Parcelable.Creator<OfferData>() {
        public OfferData createFromParcel(Parcel in) {
            return new OfferData(in);
        }

        public OfferData[] newArray(int size) {
            return new OfferData[size];
        }
    };

    private OfferData(Parcel in) {
        this.brandName = in.readString();
        this.brandeImageURL = in.readString();
        this.brandeClassification = in.readString();
        this.offerArrivalDate = in.readString();
        this.offerPercentage = in.readString();
        this.offerExpiry = in.readString();
        this.couponCode = in.readString();
    }

    public int describeContents() {
        return 0;
    }
}
