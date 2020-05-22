package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Game;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    private TextView question;
    private EditText guess;
    private Game game;
    private int incorrectGuesses;
    //TODO add stateListener and send it status updates

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_question, container, false);
        game = new Game(Difficulty.MEDIUM); //TODO load from activity instead of hardcoding
        question = view.findViewById(R.id.question);
        guess = view.findViewById(R.id.guess);
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
                    // display a notification
                } else {
                    if (game.checkGuess(Integer.parseInt(guessString))) {
                        game.updateScore();
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
        //TODO add looper that ticks every 1200ms
        return view;
    }

    private void showNextQuestion() {
        game.generateQuestion();
        question.setText(game.displayQuestion());
        guess.getText().clear();
        incorrectGuesses = 0;
    }

    //TODO add lifecycle methods to save game state
}
