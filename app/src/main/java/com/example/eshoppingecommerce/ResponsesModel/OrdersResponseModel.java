package com.example.eshoppingecommerce.ResponsesModel;

public class OrdersResponseModel {

    private String _id;
    private String productname;
    private int price;
    private String image;
    private String userid;
    private boolean payment;

    public OrdersResponseModel(String _id, String productname, int price, String image, String userid, boolean payment) {
        this._id = _id;
        this.productname = productname;
        this.price = price;
        this.image = image;
        this.userid = userid;
        this.payment = payment;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return userid;
    }

    public void setUsername(String username) {
        this.userid = username;
    }
}
