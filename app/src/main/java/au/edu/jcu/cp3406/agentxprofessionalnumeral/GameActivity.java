package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.QuestionBuilder;

public class GameActivity extends AppCompatActivity implements StateListener {

    public static final String EXTRA_DIFFICULTY = "MEDIUM";

    private StatusFragment statusFragment;
    private GameFragment gameFragment;
    private QuestionBuilder questionBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FragmentManager fragmentManager = getSupportFragmentManager();
        statusFragment = (StatusFragment) fragmentManager.findFragmentById(R.id.statusFragment);
        gameFragment = (GameFragment) fragmentManager.findFragmentById(R.id.gameFragment);
        Intent intent = getIntent();
        Log.i("GameActivity", "Difficulty = " + intent.getExtras().get(EXTRA_DIFFICULTY));
        questionBuilder = new QuestionBuilder((Difficulty) intent.getExtras().get(EXTRA_DIFFICULTY));
        if (savedInstanceState == null) {
            assert gameFragment != null;
            gameFragment.newGame();
            gameFragment.showNextQuestion(questionBuilder.buildQuestion());
            statusFragment.startTicking();
        }
    }

    @Override
    public void onUpdate(State state) {
        switch (state) {
            case CORRECT_GUESS:
                statusFragment.updateScore(gameFragment.updateScore(statusFragment.getTimeBonus()));
                gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                statusFragment.resetTimeBonus();
                break;
            case INCORRECT_GUESS:
                if (gameFragment.getIncorrectGuesses() == 3) {
                    gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                    statusFragment.resetTimeBonus();
                }
                statusFragment.updateDetection(10);
                break;
            case BOMB_THROWN:
                gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                statusFragment.updateDetection(5);
                statusFragment.resetTimeBonus();
                break;
            case GAME_OVER:
                gameFragment.clear();
                // TODO add buttons to submit score/view scores/new game/etc.
                break;
            case NEW_GAME:
                // Reset game
                break;
        }
    }
}
