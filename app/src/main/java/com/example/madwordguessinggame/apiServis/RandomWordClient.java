package com.example.madwordguessinggame.apiServis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class RandomWordClient {
    private static Retrofit randomWordFit = null;
    private static final String BASE_URL = "https://api.api-ninjas.com/v1/";

    public static Retrofit getClient() {
        if (randomWordFit == null) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-Api-Key", "HR5B4IJcG7CElE183HzbbA==z3LzrhPAvGsItEUM")
                            .build();
                    return chain.proceed(request);
                }
            }).build();

            randomWordFit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return randomWordFit;
    }
}