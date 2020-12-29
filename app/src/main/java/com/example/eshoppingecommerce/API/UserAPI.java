package com.example.eshoppingecommerce.API;

import com.example.eshoppingecommerce.MODEL.USERIMAGEMODEL;
import com.example.eshoppingecommerce.MODEL.UserModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface UserAPI {

    @POST("user/register")
    Call<Object> addUser(@Body UserModel userModel);

    @POST("user/login")
    Call<UserModel> loginUser(@Body UserModel userModel);


    @Multipart
    @POST("upload/user/image")
    Call<USERIMAGEMODEL> UploadUserImage(@Part MultipartBody.Part body);


    @POST("user/update")
    Call<Object> updateUser(@Body UserModel userModel);

}
