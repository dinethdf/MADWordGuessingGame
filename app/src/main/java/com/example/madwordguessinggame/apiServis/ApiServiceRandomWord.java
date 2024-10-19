package com.example.madwordguessinggame.apiServis;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceRandomWord {
    @GET("randomword")
    Call<RandomWord> getRandomWord();
}
