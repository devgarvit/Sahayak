package com.nagpal.sahayak.service.model.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseRequest {
    @SerializedName("amount")
    @Expose
    private Double amount;

    @SerializedName("payment_type")
    @Expose
    private String paymentType;

    @SerializedName("party_name")
    @Expose
    private String partyName;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
