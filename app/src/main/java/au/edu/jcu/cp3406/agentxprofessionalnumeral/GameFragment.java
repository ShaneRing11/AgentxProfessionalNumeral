package au.edu.jcu.cp3406.agentxprofessionalnumeral;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
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

    private StateListener listener;
    private TextView question;
    private EditText guess;
    private Difficulty difficulty;
    private Game game;
    private int incorrectGuesses;

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
        final Button bomb = view.findViewById(R.id.bomb);
        final Button submit = view.findViewById(R.id.submit);
        if (savedInstanceState != null) {
            guess.setText(savedInstanceState.getCharSequence("guess"));
            Question savedQuestion = new Question(savedInstanceState.getIntArray("numbers"),
                    savedInstanceState.getIntArray("operations"),
                    savedInstanceState.getInt("result"),
                    savedInstanceState.getInt("missingValue"),
                    savedInstanceState.getBoolean("hasMultiplication"),
                    savedInstanceState.getBoolean("hasDivision"));
            game = new Game((Difficulty) savedInstanceState.getSerializable("difficulty"),
                    savedInstanceState.getInt("score"),
                    savedInstanceState.getInt("bombsRemaining"),
                    savedQuestion);
            incorrectGuesses = savedInstanceState.getInt("incorrectGuesses");
            if (savedInstanceState.getInt("bombsRemaining") == 0) {
                bomb.setEnabled(false);
            }
            question.setText(game.displayQuestion());
        } else {
            game = new Game(Difficulty.MEDIUM);
        }
        bomb.setText(String.format(Locale.getDefault(), getString(R.string.bomb), game.getBombsRemaining()));
        bomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.useBomb();
                bomb.setText(String.format(Locale.getDefault(), getString(R.string.bomb), game.getBombsRemaining()));
                if (game.getBombsRemaining() == 0) {
                    bomb.setEnabled(false);
                }
                listener.onUpdate(State.BOMB_THROWN);
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
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        // Save fragment data
        bundle.putSerializable("difficulty", difficulty); //TODO fix crash when generating question after rotation due to null difficulty
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

    void showNextQuestion() {
        game.generateQuestion();
        question.setText(game.displayQuestion());
        guess.getText().clear();
        incorrectGuesses = 0;
    }

    int updateScore(int timeBonus) {
        return game.updateScore(timeBonus);
    }

    int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    void clear() {
        question.setText("");
        //TODO Hide and disable buttons
    }

    void setGame(Difficulty difficulty) {
        this.difficulty = difficulty;
        game = new Game(difficulty);
    }
}