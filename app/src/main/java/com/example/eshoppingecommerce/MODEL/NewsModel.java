package com.example.eshoppingecommerce.MODEL;

public class NewsModel {

   private String  _id;
   private String heading;
   private String description;
   private String date;

    public NewsModel(String _id, String heading, String description, String date) {
        this._id = _id;
        this.heading = heading;
        this.description = description;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
