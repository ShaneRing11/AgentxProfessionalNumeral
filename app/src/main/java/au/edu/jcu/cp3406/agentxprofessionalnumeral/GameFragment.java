package au.edu.jcu.cp3406.agentxprofessionalnumeral;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Game;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Question;


/**
 * A fragment containing game logic
 */
public class GameFragment extends Fragment {

    private View view;
    private StateListener listener;
    private TextView question;
    private EditText guess;
    private LinearLayout bombPouch;
    private ImageView[] bombs;
    private ImageView[] alerts;
    private Game game;
    private int incorrectGuesses;
    private float[] downPosition;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game, container, false);
        question = view.findViewById(R.id.question);
        guess = view.findViewById(R.id.guess);
        Button submit = view.findViewById(R.id.submit);
        bombPouch = view.findViewById(R.id.bombPouch);
        bombs = new ImageView[]{view.findViewById(R.id.bombOne),
                view.findViewById(R.id.bombTwo),
                view.findViewById(R.id.bombThree)};
        alerts = new ImageView[]{view.findViewById(R.id.lowAlert),
                view.findViewById(R.id.mediumAlert),
                view.findViewById(R.id.highAlert)};

        // Load the previous game state if it is saved
        if (savedInstanceState != null) {
            guess.setText(savedInstanceState.getCharSequence("guess"));
            // Recreate the current question
            Question savedQuestion = new Question(savedInstanceState.getIntArray("numbers"),
                    savedInstanceState.getIntArray("operations"),
                    savedInstanceState.getInt("result"),
                    savedInstanceState.getInt("missingValue"),
                    savedInstanceState.getBoolean("hasMultiplication"),
                    savedInstanceState.getBoolean("hasDivision"));
            game = new Game(savedInstanceState.getInt("score"),
                    savedInstanceState.getInt("bombsRemaining"),
                    savedQuestion);
            incorrectGuesses = savedInstanceState.getInt("incorrectGuesses");
            question.setText(game.displayQuestion());

            // Set bomb and guesses remaining indicators
            for (int i = bombs.length - 1; i >= 0; --i) {
                if (game.getBombsRemaining() - 1 < i) {
                    bombs[i].setVisibility(View.INVISIBLE);
                }
            }
            for (int i = alerts.length - 1; i > 0; --i) {
                if (i <= incorrectGuesses) {
                    alerts[i].setVisibility(View.VISIBLE);
                }
            }
        }

        // Set touch listener to detect bomb throws
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Get the location of this fragment and the bomb pouch
                int[] fragPosition = new int[2];
                view.getLocationOnScreen(fragPosition);
                int[] bombsPosition = new int[2];
                bombPouch.getLocationOnScreen(bombsPosition);

                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downPosition = new float[]{x, y}; // Record the start position of the motion
                        break;
                    case MotionEvent.ACTION_UP:
                        // Check that the start of the motion happened within the bounds of the bomb pouch
                        if (downPosition[0] >= bombsPosition[0] - fragPosition[0] && downPosition[1] >= bombsPosition[1] - fragPosition[1]) {
                            // Check that the motion was large enough and that there are bombs left
                            int bombsRemaining = game.getBombsRemaining();
                            if (x > downPosition[1] + 100 && bombsRemaining > 0) {
                                listener.onUpdate(State.BOMB_THROWN);
                                game.useBomb();
                                // Remove a bomb from the display
                                bombs[bombsRemaining - 1].setVisibility(View.INVISIBLE);
                                Log.i("GameFragment", "Bomb thrown, " + game.getBombsRemaining() + " bombs left");
                            }
                        }
                        break;
                }
                return true;
            }
        });
        // Add click listener for submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guessString = guess.getText().toString();
                // Require a guess to be entered
                if (guessString.equals("")) {
                    Toast.makeText(view.getContext(), R.string.no_guess, Toast.LENGTH_SHORT).show();
                } else {
                    if (game.checkGuess(Integer.parseInt(guessString))) {
                        listener.onUpdate(State.CORRECT_GUESS);
                    } else {
                        ++incorrectGuesses;
                        listener.onUpdate(State.INCORRECT_GUESS);
                        // Update the alerts display
                        if (incorrectGuesses < 3) {
                            alerts[incorrectGuesses].setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        // Save fragment data
        bundle.putInt("incorrectGuesses", incorrectGuesses);
        bundle.putCharSequence("guess", guess.getText());

        // Save game data
        bundle.putInt("score", game.getScore());
        bundle.putInt("bombsRemaining", game.getBombsRemaining());

        // Save the current question
        Question question = game.getQuestion();
        bundle.putIntArray("numbers", question.getNumbers());
        bundle.putIntArray("operations", question.getOperations());
        bundle.putInt("result", question.getResult());
        bundle.putInt("missingValue", question.getMissingValue());
        bundle.putBoolean("hasMultiplication", question.hasMultiplication());
        bundle.putBoolean("hasDivision", question.hasDivision());
    }

    // Sets the new question and updates the display
    void showNextQuestion(Question nextQuestion) {
        game.setQuestion(nextQuestion);
        question.setText(game.displayQuestion());
        guess.getText().clear(); // Clear the guess
        incorrectGuesses = 0;
        alerts[1].setVisibility(View.INVISIBLE);
        alerts[2].setVisibility(View.INVISIBLE);
    }

    // Minimises the on screen keyboard
    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    int updateScore(int timeBonus) {
        return game.updateScore(timeBonus);
    }

    int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    // Resets the game
    void newGame() {
        game = new Game();
    }

    int getScore() {
        return game.getScore();
    }
}