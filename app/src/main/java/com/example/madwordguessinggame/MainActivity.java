package com.example.madwordguessinggame;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.madwordguessinggame.apiServis.RandomWordApi;
import com.example.madwordguessinggame.apiServis.RandomWord;
import com.example.madwordguessinggame.apiServis.RhymeApiService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private int userAttemps;
    private String userGetTime;



    private Button startGameBtn;
    private TextView textView;
    private TextView textViewProfile;
    private TextView textViewSimilerWord;
    private Button buttonApiCall;

    private EditText editTextUserInput;
    private Button buttonSubmit;
    private ImageView imageView;
    private ImageView imageView2;

    public String randomWord;
    public String RhythmWord;
    private RhymeApiService rhymeApiService;

    public int userPoint;
    public int noOfAttempt;
    private TextView userPoints;
    private TextView noAttemps;
    private TextView messageMain;
    private TextView wordLetters;

    private EditText userNameEditText;
    private Button getStartBtn;
    private SharedPreferences sharedPreferences;

    private Button gameStartBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

       textView = findViewById(R.id.textView2);


//        saveButton = findViewById(R.id.button);
        userNameEditText = findViewById(R.id.userName);
        getStartBtn = findViewById(R.id.getStartBtn);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        getStartBtn.setOnClickListener(new View.OnClickListener() {

            private TextView userNameSave;

            @Override
            public void onClick(View v) {
                // Get user input from EditText
                String userName = userNameEditText.getText().toString();

                // Save the user name in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", userName);
                editor.apply();
                setContentView(R.layout.activity_home);

                userNameSave = findViewById(R.id.textView4);
                String savedUserName = sharedPreferences.getString("user_name", "");
                userNameSave.setText(savedUserName);

              gameStartBtn = findViewById(R.id.gameStartBtn);

              gameStartBtn.setOnClickListener(new View.OnClickListener() {
                private LinearLayout linearLayoutTryAgain;
                private LinearLayout linearLayoutGetNoLetter;
                private LinearLayout linearLayoutPicLetter;
                private TextView textViewDisplay;
                private TextView messageText;
                private ImageView imageMsg;

                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.active_game_screen);

                        linearLayoutTryAgain = findViewById(R.id.linearLayoutTryAgain);
                        linearLayoutGetNoLetter = findViewById(R.id.linearLayoutGetNoLetter);
                        linearLayoutPicLetter = findViewById(R.id.linearLayoutPicLetter);

                        editTextUserInput = findViewById(R.id.textUserInput);
                        buttonSubmit = findViewById(R.id.buttonChecked);
                        imageView = findViewById(R.id.imageView10);
                        imageView2 = findViewById(R.id.imageViewMainMsg);
                        messageMain = findViewById(R.id.messageMain);
                        wordLetters = findViewById(R.id.textView10);


                        wordLetters.setText("Click Options for that");
                        getApiDataAndUpdate();
                        rhymeApiService = new RhymeApiService();


                        messageMain.setText("Enter Correct Word");
                        imageView2.setImageResource(R.drawable.btn_4);
                        imageView2.setVisibility(View.VISIBLE);



                        userPoints = findViewById(R.id.textView4);
                        noAttemps = findViewById(R.id.textView9);

                        userPoint = 100;
                        noOfAttempt = 0;
                        userPoints.setText(userPoint+" Points");
                        noAttemps.setText((10-noOfAttempt) + "");

                        textViewProfile = findViewById(R.id.textView202);

                        linearLayoutTryAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Are you Sure")
                                        .setMessage("You Point is over Plz Try again")
                                        .setPositiveButton("OK", (dialog, which) -> {
                                            resetFunction();
                                        })
                                        .setNegativeButton("Cancel", (dialog, which) -> {
                                        })
                                        .show();
                            }
                        });

                        linearLayoutGetNoLetter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                randWordLetterSet();

                            }
                        });
                        linearLayoutPicLetter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                randWordPickLetterSet();
                            }
                        });


                        buttonSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageView = findViewById(R.id.imageView10);

                                if(noOfAttempt < 9 && userPoint > 10){
                                    noOfAttempt++;
                                    userPoint = userPoint - 10;
                                    userPoints.setText( userPoint + " Points");
                                    noAttemps.setText((10 - noOfAttempt)+"");

                                    System.out.println(noOfAttempt);
                                    String userInput = editTextUserInput.getText().toString();

                                    if(noOfAttempt == 4){
                                        fetchRhymingWordsApi(randomWord);
                                    }

                                    if (randomWord.equals(userInput)) {
                                        System.out.println("The strings are equal.");

                                        messageMain.setText("You Enterd Correct Word");
                                        imageView2.setImageResource(R.drawable.happyface);
                                        imageView2.setVisibility(View.VISIBLE);
                                        imageView.setVisibility(View.VISIBLE);
                                        editTextUserInput.setEnabled(false);

                                    } else {
                                        System.out.println("The strings are not equal.");
                                        messageMain.setText("Wrong Word");

                                    }
                                    System.out.println("Word fetched: " + randomWord);
                                    System.out.println("Word User Type: " + userInput);
                                    System.out.println("Rythem word Type: " + RhythmWord);

                                }else {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Failed to Guess")
                                            .setMessage("Don't Worry Try again")
                                            .setPositiveButton("OK", (dialog, which) -> {
                                            })
                                            .show();
                                    messageMain.setText("Not coreect you try again");
                                    System.out.println(noOfAttempt + "all atempt get");
                                    noOfAttempt = 0;
                                    userPoint = 100;
                                    resetFunction();
                                }
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

            }
        });

//        getStartBtn.setOnClickListener(new View.OnClickListener() {
//
//
//            private LinearLayout linearLayoutTryAgain;
//            private LinearLayout linearLayoutGetNoLetter;
//            private LinearLayout linearLayoutPicLetter;
//            private TextView textViewDisplay;
//            private TextView messageText;
//
//
//
//            private ImageView imageMsg;
//
//            @Override
//            public void onClick(View v) {
//
//            setContentView(R.layout.active_game_screen);
//
//            linearLayoutTryAgain = findViewById(R.id.linearLayoutTryAgain);
//            linearLayoutGetNoLetter = findViewById(R.id.linearLayoutGetNoLetter);
//            linearLayoutPicLetter = findViewById(R.id.linearLayoutPicLetter);
//
//            editTextUserInput = findViewById(R.id.textUserInput);
//            buttonSubmit = findViewById(R.id.buttonChecked);
//            imageView = findViewById(R.id.imageView10);
//            imageView = findViewById(R.id.imageViewMainMsg);
//            messageMain = findViewById(R.id.messageMain);
//            wordLetters = findViewById(R.id.textView10);
//
//
//            wordLetters.setText("Click Options for that");
//            getApiDataAndUpdate();
//            rhymeApiService = new RhymeApiService();
//
//
//            messageMain.setText("Enter Correct Word");
//            imageView.setImageResource(R.drawable.btn_4);
//            imageView.setVisibility(View.VISIBLE);
//
//
//
//            userPoints = findViewById(R.id.textView4);
//            noAttemps = findViewById(R.id.textView9);
//
//            userPoint = 100;
//            noOfAttempt = 0;
//            userPoints.setText(userPoint+" Points");
//            noAttemps.setText((10-noOfAttempt) + "");
//
//            textViewProfile = findViewById(R.id.textView202);
//
//            linearLayoutTryAgain.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("Are you Sure")
//                                .setMessage("You Point is over Plz Try again")
//                                .setPositiveButton("OK", (dialog, which) -> {
//                                    resetFunction();
//                                })
//                                .setNegativeButton("Cancel", (dialog, which) -> {
//                                })
//                                .show();
//                    }
//            });
//
//                linearLayoutGetNoLetter.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    randWordLetterSet();
//
//                }
//                });
//                linearLayoutPicLetter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        randWordPickLetterSet();
//                    }
//                });
//
//
//                buttonSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        imageView = findViewById(R.id.imageView10);
//
//                        if(noOfAttempt < 9 && userPoint > 10){
//                            noOfAttempt++;
//                            userPoint = userPoint - 10;
//                            userPoints.setText( userPoint + " Points");
//                            noAttemps.setText((10 - noOfAttempt)+"");
//
//                            System.out.println(noOfAttempt);
//                            String userInput = editTextUserInput.getText().toString();
//
//                            if(noOfAttempt == 4){
//                                fetchRhymingWordsApi(randomWord);
//                            }
//
//                            if (randomWord.equals(userInput)) {
//                                System.out.println("The strings are equal.");
//
//                                messageMain.setText("You Enterd Correct Word");
//                                imageView.setVisibility(View.VISIBLE);
//                                editTextUserInput.setEnabled(false);
//
//                            } else {
//                                System.out.println("The strings are not equal.");
//                                messageMain.setText("Wrong Word");
//
//                            }
//                            System.out.println("Word fetched: " + randomWord);
//                            System.out.println("Word User Type: " + userInput);
//                            System.out.println("Rythem word Type: " + RhythmWord);
//
//                        }else {
//                            new AlertDialog.Builder(MainActivity.this)
//                                    .setTitle("Failed to Guess")
//                                    .setMessage("Don't Worry Try again")
//                                    .setPositiveButton("OK", (dialog, which) -> {
//                                    })
//                                    .show();
//                            messageMain.setText("Not coreect you try again");
//                            System.out.println(noOfAttempt + "all atempt get");
//                            noOfAttempt = 0;
//                            userPoint = 100;
//                            resetFunction();
//                        }
//                    }
//                });
//
//
////                textViewProfile = findViewById(R.id.textView5);
////                buttonApiCall = findViewById(R.id.buttonStart);
////                textViewProfile.setText("Profile Screen is now Active");
////
////                buttonApiCall.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        // Call the function to fetch the word and update the UI
////                        fetchWordAndUpdateUI();
////                    }
////                });
//
////                editTextUserInput = findViewById(R.id.editTextUserInput);
////                buttonSubmit = findViewById(R.id.buttonStart);
////                textViewDisplay = findViewById(R.id.textViewDisplay);
////                buttonSubmit.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        // Get the text from EditText and set it to the TextView
////                        String userInput = editTextUserInput.getText().toString();
////                        textViewDisplay.setText(userInput);
////                    }
////                });
//
//            }
//        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public interface FetchWordCallback {
        void onWordFetched(String word);
        void onError(String errorMessage);
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
//              textViewProfile.setText(word);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println("Error fetching word: " + errorMessage);
            }
        });
    }

    public  void resetFunction(){
        messageMain.setText("Word Changed Try!");
        System.out.println("Word fetched: Reload");
        getApiDataAndUpdate();
        editTextUserInput.setEnabled(true);
        editTextUserInput.setText("");
        imageView.setVisibility(View.INVISIBLE );
        userPoint = 100;
        noOfAttempt = 0;
        userPoints.setText(userPoint+" Points");
        noAttemps.setText((10-noOfAttempt) + "");
        wordLetters.setText("Click Options for that");

    }

    public  void randWordLetterSet(){
        int wordLength = randomWord.length();
        StringBuilder hiddenWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            hiddenWord.append("X ");
        }

        wordLetters.setText(hiddenWord.toString());
        userPoint = userPoint - 5;
        userPoints.setText(userPoint+" Points");
    }

    public  void randWordPickLetterSet(){

        int wordLength = randomWord.length();
        Random random = new Random();
        char revealLetter = randomWord.charAt(random.nextInt(wordLength));

        StringBuilder hiddenWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            if (randomWord.charAt(i) == revealLetter) {
                hiddenWord.append(randomWord.charAt(i) );
                hiddenWord.append(" ");
            } else {
                hiddenWord.append("X ");
            }
        }
        wordLetters.setText(hiddenWord.toString());
        userPoint = userPoint - 5;
        userPoints.setText(userPoint+" Points");
    }

    private void fetchRhymingWordsApi(String word) {
        rhymeApiService.fetchRhymingWords(word, new RhymeApiService.ApiCallback() {
            @Override
            public void onSuccess(String responseData) {
                runOnUiThread(() -> {
                    try {
                        // Parse the response to get the first rhyme word
                        JSONArray jsonArray = new JSONArray(responseData);
                        if (jsonArray.length() > 0) {

                            RhythmWord = jsonArray.getString(0);

                            textViewProfile.setText(RhythmWord);
                            Toast.makeText(MainActivity.this, "Rhyme Word: " + RhythmWord, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "No rhyme found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


}