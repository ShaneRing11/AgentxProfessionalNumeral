package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;


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
    private String difficulty;
    private int score;
    private Twitter twitter = TwitterFactory.getSingleton();
    private User user;
    private boolean scoreSubmitted;
    private boolean scoreShared;

    public GameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        name = view.findViewById(R.id.name);
        submit = view.findViewById(R.id.submit);
        share = view.findViewById(R.id.tweet);
        highScores = view.findViewById(R.id.highScores);
        newGame = view.findViewById(R.id.newGame);
        mainMenu = view.findViewById(R.id.mainMenu);

        if (savedInstanceState == null) {
            scoreSubmitted = false;
            scoreShared = false;
        } else {
            scoreSubmitted = savedInstanceState.getBoolean("scoreSubmitted");
            scoreShared = savedInstanceState.getBoolean("scoreShared");
            toggleButtonsVisible();
            share.setEnabled(scoreShared);
            share.setText(savedInstanceState.getCharSequence("shareText"));
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateScoreTask().execute();
                scoreSubmitted = true;
                toggleButtonsVisible();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetScore();
                share.setEnabled(false);
            }
        });
        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScoresActivity.class);
                startActivity(intent);
            }
        });
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
    private boolean isAuthorised() {
        try {
            user = twitter.verifyCredentials();
            Log.i("GameOverFragment", "verified");
            return true;
        } catch (Exception e) {
            Log.i("GameOverFragment", e.getMessage());
            return false;
        }
    }

    private void toggleButtonsVisible() {
        View[] submissionViews = new View[] {name, submit};
        View[] menuViews = new  View[] {share, highScores, newGame, mainMenu};

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

    void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    private class UpdateScoreTask extends AsyncTask<Void, Void, Boolean> {

        private ContentValues scoreValues;

        protected void onPreExecute() {
            scoreValues = new ContentValues();
            scoreValues.put("NAME", name.getText().toString());
            scoreValues.put("SCORE", score);
            scoreValues.put("DIFFICULTY", difficulty);
        }

        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper agentxDatabaseHelper = new AgentxDatabaseHelper(getContext());
            try {
                SQLiteDatabase db = agentxDatabaseHelper.getWritableDatabase();
                db.insert("SCORES", null, scoreValues);
                db.close();
                Log.i("GameOverFragment", "Score added");
                return true;
            } catch (SQLiteException e) {
                Log.i("GameOverFragment", "Database update failed");
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(getContext(),
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}