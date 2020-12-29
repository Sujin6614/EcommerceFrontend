package com.example.eshoppingecommerce.API;

import com.example.eshoppingecommerce.MODEL.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsAPI {

    @GET("news/get")
    Call<List<NewsModel>> getAllNews ();
}
