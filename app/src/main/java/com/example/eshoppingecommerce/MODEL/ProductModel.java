package com.example.eshoppingecommerce.MODEL;

public class ProductModel {
    private String _id;
    private String productname;
    private int price;
    private String brand;
    private String category;
    private String description;
    private String image;
    private int warrenty;
    private String date;

    public ProductModel(String _id, String productname, int price, String brand, String category, String description, String image, int warrenty, String date) {
        this._id = _id;
        this.productname = productname;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.image = image;
        this.warrenty = warrenty;
        this.date = date;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWarrenty() {
        return warrenty;
    }

    public void setWarrenty(int warrenty) {
        this.warrenty = warrenty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}