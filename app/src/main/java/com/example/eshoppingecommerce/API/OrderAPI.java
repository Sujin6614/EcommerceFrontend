package com.example.eshoppingecommerce.API;

import com.example.eshoppingecommerce.ResponsesModel.OrdersResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderAPI {

    @POST("order/add")
    Call<Object> orderProduct(@Body OrdersResponseModel ordersResponseModel);

    @GET("order/get/{userid}")
    Call<List<OrdersResponseModel>>  getUserOrders(@Path("userid") String userid);

    @DELETE("order/delete/{id}")
    Call<Object> cancelOrder(@Path("id") String id);

}
