package com.example.eshoppingecommerce.ResponsesModel;

public class FeedbackresponseModel {
    private String _id;
    private int rating;
    private String feedback;
    private String username;
    private String productid;

    public FeedbackresponseModel(String _id, int rating, String feedback, String username, String productid) {
        this._id = _id;
        this.rating = rating;
        this.feedback = feedback;
        this.username = username;
        this.productid = productid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }
}
