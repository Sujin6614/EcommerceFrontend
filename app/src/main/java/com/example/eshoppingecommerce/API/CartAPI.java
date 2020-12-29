package com.example.eshoppingecommerce.API;

import com.example.eshoppingecommerce.ResponsesModel.CartResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartAPI {

    @POST("cart/add")
    Call<Object> addToCart(@Body CartResponseModel cartResponseModel);

    @GET("cart/get/{userid}")
    Call<List<CartResponseModel>> getCartData(@Path("userid") String userid);

    @DELETE("cart/delete/{id}")
    Call<Object> removeFromCart(@Path("id") String id);


}
