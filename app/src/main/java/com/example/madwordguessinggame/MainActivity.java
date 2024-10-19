package com.example.madwordguessinggame;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.madwordguessinggame.apiServis.RandomWordApi;
import com.example.madwordguessinggame.apiServis.RandomWord;


public class MainActivity extends AppCompatActivity {


    private Button button;
    private TextView textView;
    private TextView textViewProfile;
    private Button buttonApiCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

      button = findViewById(R.id.button);
      textView = findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the text to be visible and change its content
                textView.setVisibility(View.VISIBLE);
                textView.setText("Button Clicked! Hello, Android.");
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.activity_home);
//                textViewProfile = findViewById(R.id.textApi);
//                buttonApiCall = findViewById(R.id.buttonApiCall);
//                textViewProfile.setText("Profile Screen is now Active");
//
//                buttonApiCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Call the function to fetch the word and update the UI
//                        fetchWordAndUpdateUI();
//                    }
//                });


            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void fetchWordAndUpdateUI() {
        RandomWordApi.fetchRandomWord(new RandomWordApi.ApiCallback<RandomWord>() {
            @Override
            public void onSuccess(RandomWord response) {
                String word = response.getWord().get(0);
                textViewProfile.setText(word);
            }
            @Override
            public void onFailure(String errorMessage) {
                textViewProfile.setText("Error get word: " + errorMessage);
            }
        });
    }
}