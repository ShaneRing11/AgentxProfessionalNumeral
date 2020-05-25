package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.QuestionBuilder;

public class GameActivity extends AppCompatActivity implements StateListener {

    public static final String EXTRA_DIFFICULTY = "MEDIUM";

    private FragmentManager fragmentManager;
    private StatusFragment statusFragment;
    private GameFragment gameFragment;
    private GameOverFragment gameOverFragment;
    private QuestionBuilder questionBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        fragmentManager = getSupportFragmentManager();
        statusFragment = (StatusFragment) fragmentManager.findFragmentById(R.id.statusFragment);
        gameFragment = (GameFragment) fragmentManager.findFragmentById(R.id.gameFragment);
        gameOverFragment = (GameOverFragment) fragmentManager.findFragmentById(R.id.gameOverFragment);
        Intent intent = getIntent();
        questionBuilder = new QuestionBuilder((Difficulty) intent.getExtras().get(EXTRA_DIFFICULTY));
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().hide(gameOverFragment).commit();
            assert gameFragment != null;
            gameFragment.newGame();
            gameFragment.showNextQuestion(questionBuilder.buildQuestion());
            statusFragment.startTicking();
        } else {
            statusFragment.updateScore(gameFragment.getScore());
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
                fragmentManager.beginTransaction().hide(gameFragment).commit();
                hideKeyboard();
                fragmentManager.beginTransaction().show(gameOverFragment).commit();
                break;
            case NEW_GAME:
                fragmentManager.beginTransaction().hide(gameOverFragment).commit();
                hideKeyboard();
                fragmentManager.beginTransaction().show(gameFragment).commit();
                gameFragment.newGame();
                statusFragment.reset();
                gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                statusFragment.startTicking();
                break;
            case MAIN_MENU:
                finish();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}