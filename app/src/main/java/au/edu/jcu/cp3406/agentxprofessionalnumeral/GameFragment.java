package au.edu.jcu.cp3406.agentxprofessionalnumeral;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Game;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Question;


/**
 * A fragment containing game logic
 */
public class GameFragment extends Fragment {

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
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game, container, false);
        question = view.findViewById(R.id.question);
        guess = view.findViewById(R.id.guess);
        Button submit = view.findViewById(R.id.submit);
        bombPouch = view.findViewById(R.id.bombPouch);
        bombs = new ImageView[] {view.findViewById(R.id.bombOne),
                view.findViewById(R.id.bombTwo),
                view.findViewById(R.id.bombThree)};
        alerts = new ImageView[] {view.findViewById(R.id.lowAlert),
                view.findViewById(R.id.mediumAlert),
                view.findViewById(R.id.highAlert)};
        if (savedInstanceState != null) {
            guess.setText(savedInstanceState.getCharSequence("guess"));
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
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] fragPosition = new int[2];
                view.getLocationOnScreen(fragPosition);
                int[] bombsPosition = new int[2];
                bombPouch.getLocationOnScreen(bombsPosition);
                float x = event.getX();
                float y = event.getY();
                String action = "";
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downPosition = new float[] {x, y};
                        break;
                    case MotionEvent.ACTION_UP:
                        if (downPosition[0] >= bombsPosition[0] - fragPosition[0] && downPosition[1] >= bombsPosition[1] - fragPosition[1]) {
                            int bombsRemaining = game.getBombsRemaining();
                            if (x > downPosition[1] + 100 && bombsRemaining > 0) {
                                listener.onUpdate(State.BOMB_THROWN);
                                game.useBomb();
                                bombs[bombsRemaining - 1].setVisibility(View.INVISIBLE);
                                Log.i("GameFragment", "Bomb thrown, " + game.getBombsRemaining() + " bombs left");
                            }
                        }
                        break;
                }
                return true;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guessString = guess.getText().toString();
                if (guessString.equals("")) {
                    Toast.makeText(view.getContext(), R.string.no_guess, Toast.LENGTH_SHORT).show();
                } else {
                    if (game.checkGuess(Integer.parseInt(guessString))) {
                        listener.onUpdate(State.CORRECT_GUESS);
                    } else {
                        ++incorrectGuesses;
                        listener.onUpdate(State.INCORRECT_GUESS);
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

    void showNextQuestion(Question nextQuestion) {
        game.setQuestion(nextQuestion);
        question.setText(game.displayQuestion());
        guess.getText().clear();
        incorrectGuesses = 0;
        alerts[1].setVisibility(View.INVISIBLE);
        alerts[2].setVisibility(View.INVISIBLE);
    }

    int updateScore(int timeBonus) {
        return game.updateScore(timeBonus);
    }

    int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    void newGame() {
        game = new Game();
    }
    int getScore() {
        return game.getScore();
    }
}