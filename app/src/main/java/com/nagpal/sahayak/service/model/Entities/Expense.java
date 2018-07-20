package com.nagpal.sahayak.service.model.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Expense {
    @SerializedName("party_name")
    @Expose
    private String partyName;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("category")
    @Expose
    private String category;

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
