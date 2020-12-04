package com.vingcoz.srishticatering.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit getClient() {
        String BASE_URL = "http://www.vingcoz.com/cateringApp/User_api/";
        //   String BASE_URL = "http://www.vingcoz.com/srishtiApp/User_api/";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
