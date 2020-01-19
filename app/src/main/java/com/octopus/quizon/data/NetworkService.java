package com.octopus.quizon.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    public static final String BASE_URL = "http://185.66.85.157";
   // public static final String BASE_URL = "http://74d26a90.ngrok.io/";
    public static final String BASE_PORT = ":8000";
    public static final String PIC_PORT = ":8080";

    private static NetworkService mInstance;
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL+BASE_PORT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JSONPlaceHolderAPI getAPI() {
        return mRetrofit.create(JSONPlaceHolderAPI.class);
    }
}