package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.Context;
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
    private String difficulty;
    private int score;
    private Twitter twitter = TwitterFactory.getSingleton();
    private User user;

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
        Button submit = view.findViewById(R.id.submit);
        Button share = view.findViewById(R.id.tweet);
        Button highScores = view.findViewById(R.id.highScores);
        Button newGame = view.findViewById(R.id.newGame);
        Button mainMenu = view.findViewById(R.id.mainMenu);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Submit high score, show confirmation message and hide elements
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetScore();
            }
        });
        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open high scores activity
            }
        });
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdate(State.NEW_GAME);
                // Unhide submission elements elements
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

    //TODO remember whether score submission elements hidden and entered name

    private void submitScore() {

    }

    private void tweetScore() {
        Background.run(new Runnable() {
            @Override
            public void run() {
                if (isAuthorised()) {
                    try {
                        twitter.updateStatus(String.format(Locale.getDefault(), getString(R.string.format_tweet),
                                name.getText().toString(), score, difficulty));
                        Toast.makeText(getView().getContext(), "Score shared", Toast.LENGTH_SHORT).show();
                    } catch (TwitterException ignored) {
                        Log.i("GameOverFragment", "Couldn't post tweet");
                    }
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

    void setScore(int score) {
        this.score = score;
    }

    void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
