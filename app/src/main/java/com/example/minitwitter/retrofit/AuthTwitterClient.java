package com.example.minitwitter.retrofit;

import com.example.minitwitter.common.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    private static AuthTwitterClient instance = null;
    private AuthTwitterService authTwitterClient;
    private Retrofit retrofit;

    private AuthTwitterClient() {
        //Include the token in the header
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        authTwitterClient = retrofit.create(AuthTwitterService.class);
    }

    public static AuthTwitterClient getInstance() {
        if (instance == null) instance = new AuthTwitterClient();
        return instance;
    }

    public AuthTwitterService getAuthTwitterService() {
        return authTwitterClient;
    }
}
