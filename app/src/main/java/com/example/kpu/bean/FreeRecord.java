package com.example.kpu.bean;

public class FreeRecord {
    private int type;
    private String title;
    private String timeDate;
    private String totalFee;
    private String extraFee;
    private String lateCharges;
    private String discount;
    private String discountFee;
    private String paidFee;

    public FreeRecord(int type,String title, String timeDate, String totalFee, String extraFee,
                      String lateCharges, String discount,String discountFee, String paidFee) {
        this.type = type;
        this.title = title;
        this.timeDate = timeDate;
        this.totalFee = totalFee;
        this.extraFee = extraFee;
        this.lateCharges = lateCharges;
        this.discount = discount;
        this.discountFee = discountFee;
        this.paidFee = paidFee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    // Getter and setter methods
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(String extraFee) {
        this.extraFee = extraFee;
    }

    public String getLateCharges() {
        return lateCharges;
    }

    public void setLateCharges(String lateCharges) {
        this.lateCharges = lateCharges;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(String paidFee) {
        this.paidFee = paidFee;
    }
}
