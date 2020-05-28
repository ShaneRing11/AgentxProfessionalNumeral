package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.Objects;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


/**
 * Fragment that displays options after the game ends
 */
public class GameOverFragment extends Fragment {

    private StateListener listener;
    private EditText name;
    private Button submit;
    private Button share;
    private Button highScores;
    private Button newGame;
    private Button mainMenu;
    private Difficulty difficulty;
    private int score;
    private Twitter twitter = TwitterFactory.getSingleton();
    private boolean scoreSubmitted;
    private boolean scoreShared;

    public GameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        final Context context = getContext();

        name = view.findViewById(R.id.name);
        submit = view.findViewById(R.id.submit);
        share = view.findViewById(R.id.tweet);
        highScores = view.findViewById(R.id.highScores);
        newGame = view.findViewById(R.id.newGame);
        mainMenu = view.findViewById(R.id.mainMenu);

        // Load state if saved
        if (savedInstanceState == null) {
            scoreSubmitted = false;
            scoreShared = false;
        } else {
            scoreSubmitted = savedInstanceState.getBoolean("scoreSubmitted");
            scoreShared = savedInstanceState.getBoolean("scoreShared");
            toggleButtonsVisible();
            share.setEnabled(!scoreShared);
            share.setText(savedInstanceState.getCharSequence("shareText"));
        }

        // Add button click listeners
        // Enter score into database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                if (nameString.equals("")) {
                    Toast.makeText(context, R.string.no_codename, Toast.LENGTH_SHORT).show();
                } else {
                    ScoreTaskParams params = new ScoreTaskParams(nameString, score,
                            difficulty.name().toLowerCase(), context);
                    new UpdateScoreTask().execute(params);
                    scoreSubmitted = true;
                    toggleButtonsVisible();
                }
            }
        });
        // Share score to Twitter
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetScore();
                share.setEnabled(false);
            }
        });
        // Open high scores screen
        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScoresActivity.class);
                intent.putExtra(ScoresActivity.EXTRA_DIFFICULTY, difficulty);
                startActivity(intent);
            }
        });
        // Start a new game
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdate(State.NEW_GAME);
                scoreSubmitted = false;
                scoreShared = false;
                share.setText(R.string.tweet_score);
                toggleButtonsVisible();
            }
        });
        // Return to main menu
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdate(State.MAIN_MENU);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("scoreSubmitted", scoreSubmitted);
        bundle.putBoolean("scoreShared", scoreShared);
        bundle.putCharSequence("shareText", share.getText());
    }

    // Shares the users score as a Twitter status
    private void tweetScore() {
        Background.run(new Runnable() {
            @Override
            public void run() {
                if (isAuthorised()) {
                    try {
                        twitter.updateStatus(String.format(Locale.getDefault(), getString(R.string.format_tweet),
                                name.getText().toString(), score, difficulty));
                        scoreShared = true;
                        share.setText(R.string.score_shared);
                        Log.i("GameOverFragment", "Post successful");
                    } catch (TwitterException e) {
                        Log.i("GameOverFragment", "Couldn't post tweet");
                        share.setText(R.string.share_failed);
                    }
                } else {
                    share.setText(R.string.share_failed);
                }
            }
        });
    }

    // Checks if the provided Twitter credentials are valid
    private boolean isAuthorised() {
        try {
            twitter.verifyCredentials();
            Log.i("GameOverFragment", "verified");
            return true;
        } catch (Exception e) {
            Log.i("GameOverFragment", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    // Switches whether the submission or menu button groups are active
    private void toggleButtonsVisible() {
        View[] submissionViews = new View[]{name, submit};
        View[] menuViews = new View[]{share, highScores, newGame, mainMenu};

        if (scoreSubmitted) {
            for (View view : menuViews) {
                view.setEnabled(true);
                view.setVisibility(View.VISIBLE);
            }
            for (View view : submissionViews) {
                view.setEnabled(false);
                view.setVisibility(View.INVISIBLE);
            }
        } else {
            for (View view : submissionViews) {
                view.setEnabled(true);
                view.setVisibility(View.VISIBLE);
            }
            for (View view : menuViews) {
                view.setEnabled(false);
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    void setScore(int score) {
        this.score = score;
    }

    void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    // Inner class to hold parameters for database update thread
    private static class ScoreTaskParams {

        String name;
        int score;
        String difficulty;
        Context context;

        ScoreTaskParams(String name, int score, String difficulty, Context context) {
            this.name = name;
            this.score = score;
            this.difficulty = difficulty;
            this.context = context;
        }
    }

    // Inner class that runs a thread to add the users score to the database
    private static class UpdateScoreTask extends AsyncTask<ScoreTaskParams, Void, Boolean> {

        protected Boolean doInBackground(ScoreTaskParams... params) {
            ContentValues scoreValues = new ContentValues();
            scoreValues.put("NAME", params[0].name);
            scoreValues.put("SCORE", params[0].score);
            scoreValues.put("DIFFICULTY", params[0].difficulty);
            Context context = params[0].context;
            SQLiteOpenHelper agentxDatabaseHelper = new AgentxDatabaseHelper(context);
            try {
                SQLiteDatabase db = agentxDatabaseHelper.getWritableDatabase();
                db.insert("SCORES", null, scoreValues);
                db.close();
                Log.i("GameOverFragment", "Score added");
                return true;
            } catch (SQLiteException e) {
                Log.i("GameOverFragment", "Database update failed");
                Toast toast = Toast.makeText(context,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        }
    }
}