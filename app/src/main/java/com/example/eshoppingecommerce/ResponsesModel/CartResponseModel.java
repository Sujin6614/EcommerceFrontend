package com.example.eshoppingecommerce.ResponsesModel;


public class CartResponseModel {


    private String _id;
    private String userid;
    private String productid;
    private String productname;
    private String image;
    private String fullname;
    private String email;
    private String address;
    private String mobile;
    private int price;




    public CartResponseModel(String _id, String userid, String productid, String productname, String image, String fullname, String email, String address, String mobile, int price) {
        this._id=_id;
        this.userid = userid;
        this.productid = productid;
        this.productname = productname;
        this.image = image;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.mobile = mobile;
        this.price = price;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getName() {
        return productname;
    }

    public void setName(String productname) {
        this.productname = productname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return fullname;
    }

    public void setUsername(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
