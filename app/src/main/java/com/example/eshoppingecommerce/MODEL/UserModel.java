package com.example.eshoppingecommerce.MODEL;

public class UserModel {
    private String _id;
    private String fullname;
    private String email;
    private String mobile;
    private String address;
    private String password;
    private String image;



    public UserModel(String _id, String fullname, String email, String mobile, String address, String password, String image) {
        this._id = _id;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.password = password;
        this.image = image;
    }





    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
