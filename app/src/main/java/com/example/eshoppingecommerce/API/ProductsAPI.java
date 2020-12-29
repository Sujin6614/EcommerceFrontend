package com.example.eshoppingecommerce.API;

import com.example.eshoppingecommerce.MODEL.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductsAPI {

    @GET("product/get")
    Call<List<ProductModel>> getAllProducts();


    @GET("product/get/category/{category}")
    Call<List<ProductModel>> getProductsBycategory(@Path("category") String category);


}
