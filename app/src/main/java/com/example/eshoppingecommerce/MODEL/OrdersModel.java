package com.example.eshoppingecommerce.MODEL;

public class OrdersModel {
    private String id;
    private String productid;
    private String userid;
    private boolean payment;

    public OrdersModel(String id, String productid, String userid, boolean payment) {
        this.id = id;
        this.productid = productid;
        this.userid = userid;
        this.payment=payment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }
}
