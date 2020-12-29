package com.example.eshoppingecommerce.API;

import com.example.eshoppingecommerce.ResponsesModel.FeedbackresponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedbackAPI {

    @POST("feedback/add")
    Call<Object> giveFeedback(@Body FeedbackresponseModel feedbackresponseModel);

    @GET("feedback/get/{productid}")
    Call<List<FeedbackresponseModel>> getFeedback(@Path("productid") String productid);

}
