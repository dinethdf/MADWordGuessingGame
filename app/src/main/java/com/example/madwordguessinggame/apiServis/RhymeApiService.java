package com.example.madwordguessinggame.apiServis;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class RhymeApiService {
    private static final String API_URL = "https://api.api-ninjas.com/v1/rhyme?word=";
    private static final String API_KEY = "HR5B4IJcG7CElE183HzbbA==z3LzrhPAvGsItEUM";
    private OkHttpClient client;

    public RhymeApiService() {
        client = new OkHttpClient();
    }

    // Interface for handling API responses
    public interface ApiCallback {
        void onSuccess(String responseData);
        void onFailure(String errorMessage);
    }

    public void fetchRhymingWords(String word, ApiCallback callback) {
        String url = API_URL + word;

        Request request = new Request.Builder()
                .url(url)
                .header("X-Api-Key", API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed to fetch data: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                } else {
                    callback.onFailure("Error: " + response.message());
                }
            }
        });
    }
}

