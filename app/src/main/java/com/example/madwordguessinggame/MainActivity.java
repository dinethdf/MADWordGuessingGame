package com.example.madwordguessinggame;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.madwordguessinggame.apiServis.RandomWordApi;
import com.example.madwordguessinggame.apiServis.RandomWord;


public class MainActivity extends AppCompatActivity {

    private int userPoints;
    private int userAttemps;
    private String userGetTime;



    private Button button;
    private TextView textView;
    private TextView textViewProfile;
    private Button buttonApiCall;

    private EditText editTextUserInput;
    private Button buttonSubmit;
    private TextView textViewDisplay;
    private TextView messageText;
    private ImageView imageView;
    private LinearLayout linearLayoutTryAgain;

    public String randomWord;

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

            setContentView(R.layout.active_game_screen);
            getApiDataAndUpdate();


              linearLayoutTryAgain = findViewById(R.id.linearLayoutTryAgain);
              textViewProfile = findViewById(R.id.textView10);
              editTextUserInput = findViewById(R.id.textUserInput);
              buttonSubmit = findViewById(R.id.buttonChecked);
              imageView = findViewById(R.id.imageView10);


                linearLayoutTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetFunction();
                    }
                });

//                messageText = findViewById(R.id.textView);
//                messageText.setText("");

                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView = findViewById(R.id.imageView10);
                        
                        // Get the text from EditText and set it to the TextView
                        String userInput = editTextUserInput.getText().toString();
                        if (!userInput.isEmpty() && randomWord.equals(userInput)) {
                            System.out.println("The strings are equal.");

//                          messageText.setText("You word Is correnct");
                            imageView.setVisibility(View.VISIBLE);
                            editTextUserInput.setEnabled(false);

                        } else {
                            System.out.println("The strings are not equal.");
//                          messageText.setText("You word Is not correnct");

                        }
                        System.out.println("Word fetched: " + randomWord);
                        System.out.println("Word User Type: " + userInput);

                    }
                });


//                textViewProfile = findViewById(R.id.textView5);
//                buttonApiCall = findViewById(R.id.buttonStart);
//                textViewProfile.setText("Profile Screen is now Active");
//
//                buttonApiCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Call the function to fetch the word and update the UI
//                        fetchWordAndUpdateUI();
//                    }
//                });

//                editTextUserInput = findViewById(R.id.editTextUserInput);
//                buttonSubmit = findViewById(R.id.buttonStart);
//                textViewDisplay = findViewById(R.id.textViewDisplay);
//                buttonSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Get the text from EditText and set it to the TextView
//                        String userInput = editTextUserInput.getText().toString();
//                        textViewDisplay.setText(userInput);
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

    public interface FetchWordCallback {
        void onWordFetched(String word);  // Called on success
        void onError(String errorMessage); // Called on failure
    }

    private void fetchWordAndUpdateUI(FetchWordCallback callback) {
        RandomWordApi.fetchRandomWord(new RandomWordApi.ApiCallback<RandomWord>() {
            @Override
            public void onSuccess(RandomWord response) {
                // Get the word
                String word = response.getWord().get(0);

                randomWord = word;

                // Pass the word to the callback
                if (callback != null) {
                    callback.onWordFetched(word);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Show error in the TextView
                textViewProfile.setText("Error getting word: " + errorMessage);

                // Pass the error to the callback
                if (callback != null) {
                    callback.onError(errorMessage);
                }
            }
        });
    }

    public  void getApiDataAndUpdate(){
        fetchWordAndUpdateUI(new FetchWordCallback() {
            @Override
            public void onWordFetched(String word) {
                System.out.println("Word fetched: " + word);

                // Update the UI
                textViewProfile.setText(word);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Error fetching word: " + errorMessage);
            }
        });
    }

    public  void resetFunction(){
        System.out.println("Word fetched: Reload");
        getApiDataAndUpdate();
        editTextUserInput.setEnabled(true);
        editTextUserInput.setText("");
        imageView.setVisibility(View.INVISIBLE );

    }

}