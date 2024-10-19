package com.example.madwordguessinggame.apiServis;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomWordApi {

    private static final String TAG = "ApiCall";

    public interface ApiCallback<T> {
        void onSuccess(T response);
        void onFailure(String errorMessage);
    }

    public static void fetchRandomWord(ApiCallback<RandomWord> callback) {
        ApiServiceRandomWord apiService = RandomWordClient.getClient().create(ApiServiceRandomWord.class);
        Call<RandomWord> call = apiService.getRandomWord();

        makeApiCall(call, callback);
    }

    // A generic method to handle all API calls
    private static <T> void makeApiCall(Call<T> call, ApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Response not successful");
                    callback.onFailure("Response not successful");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(TAG, "API Error: " + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }
}
