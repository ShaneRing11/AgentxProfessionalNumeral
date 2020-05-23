package au.edu.jcu.cp3406.agentxprofessionalnumeral;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Game;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Question;


/**
 * A fragment containing game logic
 */
public class GameFragment extends Fragment {

    private TextView question;
    private EditText guess;
    private Game game;
    private int incorrectGuesses;
    private int timeBonus;
    private Handler handler;
    private Runnable tick;
    private boolean gameRunning;
    //TODO add stateListener and send it status updates

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game, container, false);
        question = view.findViewById(R.id.question);
        guess = view.findViewById(R.id.guess);
        handler = new Handler();
        final Button bomb = view.findViewById(R.id.bomb);
        bomb.setText(String.format(Locale.getDefault(), getString(R.string.bomb), game.getBombsRemaining()));
        bomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.useBomb();
                bomb.setText(String.format(Locale.getDefault(), getString(R.string.bomb), game.getBombsRemaining()));
            }
        });
        final Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guessString = guess.getText().toString();
                if (guessString.equals("")) {
                    Toast.makeText(view.getContext(), R.string.no_guess, Toast.LENGTH_SHORT).show();
                } else {
                    if (game.checkGuess(Integer.parseInt(guessString))) {
                        game.updateScore(timeBonus);
                        showNextQuestion();
                    } else {
                        game.updateDetection(10);
                        ++incorrectGuesses;
                        if (incorrectGuesses == 3) {
                            showNextQuestion();
                        }
                    }
                }
            }
        });
        if (savedInstanceState != null) {
            guess.setText(savedInstanceState.getCharSequence("guess"));
            Question question = new Question(savedInstanceState.getIntArray("numbers"),
                    savedInstanceState.getIntArray("operations"),
                    savedInstanceState.getInt("result"),
                    savedInstanceState.getInt("missingValue"),
                    savedInstanceState.getBoolean("hasMultiplication"),
                    savedInstanceState.getBoolean("hasDivision"));
            game = new Game(Difficulty.MEDIUM,
                    savedInstanceState.getInt("score"),
                    savedInstanceState.getInt("detection"),
                    savedInstanceState.getInt("bombsRemaining"),
                    question);
            incorrectGuesses = savedInstanceState.getInt("incorrectGuesses");
            timeBonus = savedInstanceState.getInt("timeBonus");
            gameRunning = savedInstanceState.getBoolean("gameRunning");
            if (gameRunning) {
                startTicking();
            }
        } else {
            game = new Game(Difficulty.MEDIUM); //TODO load from activity instead of hardcoding
            gameRunning = true;
            showNextQuestion();
            startTicking();
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(tick);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gameRunning) {
            startTicking();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        // Save fragment data
        bundle.putInt("incorrectGuesses", incorrectGuesses);
        bundle.putInt("timeBonus", timeBonus);
        bundle.putBoolean("gameRunning", gameRunning);
        bundle.putCharSequence("guess", guess.getText());

        // Save game data
        bundle.putInt("score", game.getScore());
        bundle.putInt("detection", game.getDetection());
        bundle.putInt("bombsRemaining", game.getBombsRemaining());

        // Save the current question
        Question question = game.getQuestion();
        bundle.putIntArray("numbers", question.getNumbers());
        bundle.putIntArray("operation", question.getOperations());
        bundle.putInt("result", question.getResult());
        bundle.putInt("missingValue", question.getMissingValue());
        bundle.putBoolean("hasMultiplication", question.hasMultiplication());
        bundle.putBoolean("hasDivision", question.hasDivision());
    }

    private void showNextQuestion() {
        game.generateQuestion();
        question.setText(game.displayQuestion());
        guess.getText().clear();
        incorrectGuesses = 0;
        timeBonus = 15;
    }

    private void startTicking() {
        tick = new Runnable() {
            @Override
            public void run() {
                if (timeBonus > 0) {
                    --timeBonus;
                }
                game.updateDetection(1);
                if (game.getDetection() < 100) {
                    // Update detection in InfoFragment
                } else {
                    gameRunning = false;
                    // clear the question
                    handler.removeCallbacks(tick);
                }
                handler.postDelayed(this, 1200);
            }
        };
        handler.post(tick);
    }

    //TODO add lifecycle methods to save game state
}
