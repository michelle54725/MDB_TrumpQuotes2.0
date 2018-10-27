package com.demo.mdb.spring2017finalassessment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    TextView phraseTextView;
    TextView accuracyTextView;
    ImageView doneImageView;
    Chronometer timer;
    EditText typedEditText;

    String phrase;
    String[] splitPhrase;
    boolean gameStarted = false;
    String typedString;

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        firebaseDatabase = FirebaseDatabase.getInstance();
        phraseTextView = (TextView) findViewById(R.id.phraseTextView);
        accuracyTextView = (TextView) findViewById(R.id.wpmTextView);
        doneImageView = (ImageView) findViewById(R.id.doneImageView);
        timer = (Chronometer) findViewById(R.id.timer);
        typedEditText = (EditText) findViewById(R.id.typedEditText);

        doneImageView.setOnClickListener(this);

        /* TODO Part 4
         * call getRandomPhrase in a new thread. Use the result of this to set the phrase variable,
         * set the phraseTextView to that phrase, and set splitPhrase to phrase.split("\\s")
         */
        new getRandomPhrase().execute();

        typedEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        typedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!gameStarted) {
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                    gameStarted = true;
                }
                typedString = s.toString();
                accuracyTextView.setText(getAccuracyPercentage() + "%");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doneImageView:
                clickDone();
                break;
        }
    }

    public void clickDone() {
        timer.stop();
        int accuracyPercentage = getAccuracyPercentage();
        int wpm = getWPM();
        accuracyTextView.setText(accuracyPercentage + "% with " + wpm + " WPM");

        /* TODO Part 6
         * Add a game to the database. Keep in mind that we also want to know which users are
         * attached to which games, so we should be changing two nodes of the database here, "games"
         * and "users". We know how to generate random keys for adding games, but how can we add
         * that game to our user's node? We can't generate a random key, because we want it to go
         * to the same node every time for a given user.
         */
        final DatabaseReference myRef_games = FirebaseDatabase.getInstance().getReference("/games");
        final String mKey_games = myRef_games.push().getKey(); //create empty node to get key of it

        final DatabaseReference myRef_users = FirebaseDatabase.getInstance().getReference("/users");
        final String mKey_users = mAuth.getUid(); // get unique user id

        // add game to database
        // update user's game list

        typedEditText.setEnabled(false);
        doneImageView.setClickable(false);
    }

    private int getAccuracyPercentage() {
        String[] typedStringWords = typedString.split("\\s");
        double correctCount = 0;
        for (int i = 0; i < typedStringWords.length; i++) {
            if (i < splitPhrase.length && typedStringWords[i].equals(splitPhrase[i])) {
                correctCount++;
            }
        }
        return (int) (100 * correctCount / typedStringWords.length);
    }

    private int getWPM() {
        double minutes = ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000.0) / 60.0;
        return (int) (typedString.split("\\s").length / minutes);
    }

    private class getRandomPhrase extends AsyncTask<Void, Integer, String> {
        /* TODO Part 4.5
         * call getRandomPhrase in a new thread. Use the result of this to set the phrase variable,
         * set the phraseTextView to that phrase, and set splitPhrase to phrase.split("\\s")
         */

        getRandomPhrase() { }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return Utils.getRandomPhrase().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            phrase = result;
            phraseTextView.setText(phrase);
            splitPhrase = phrase.split("\\s");
        }
    }

}