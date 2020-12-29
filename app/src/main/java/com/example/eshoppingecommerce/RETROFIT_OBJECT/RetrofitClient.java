package com.example.eshoppingecommerce.RETROFIT_OBJECT;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    //private final static String BASE_URL = "http://192.168.1.39:8080/";
    private final static String BASE_URL = "http://10.0.2.2:8080/";
    Retrofit retrofit;

    public String BASE_URL() {
        return BASE_URL;
    }

    public Retrofit createInstance() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }

}
