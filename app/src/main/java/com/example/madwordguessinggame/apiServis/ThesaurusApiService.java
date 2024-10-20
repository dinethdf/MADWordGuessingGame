package com.example.madwordguessinggame.apiServis;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ThesaurusApiService {

    // Your API key here
    private static final String API_KEY = "YOUR_API_KEY";

    public interface ApiCallback {
        void onSuccess(String synonym);
        void onFailure(String errorMessage);
    }

    public static void fetchThesaurusWord(String word, ApiCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.api-ninjas.com/v1/thesaurus?word=" + word;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Api-Key", API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ThesaurusApiService", "API call failed: " + e.getMessage());
                callback.onFailure("Failed to fetch synonym");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Unexpected response: " + response);
                    return;
                }

                String jsonResponse = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray synonymsArray = jsonObject.getJSONArray("synonyms");

                    if (synonymsArray.length() > 0) {
                        String synonym = synonymsArray.getString(0); // Get the first synonym
                        callback.onSuccess(synonym);
                    } else {
                        callback.onFailure("No synonyms found.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("JSON parsing error");
                }
            }
        });
    }
}
