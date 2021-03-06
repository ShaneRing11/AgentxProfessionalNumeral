package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;
import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.QuestionBuilder;

/**
 * Activity holding fragments required to play a game and handling communication between them
 */
public class GameActivity extends AppCompatActivity implements StateListener {

    public static final String EXTRA_DIFFICULTY = "MEDIUM";

    private FragmentManager fragmentManager;
    private StatusFragment statusFragment;
    private GameFragment gameFragment;
    private GameOverFragment gameOverFragment;
    private QuestionBuilder questionBuilder;
    private boolean playSounds;
    private SoundPool soundPool;
    private int[] soundIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fragmentManager = getSupportFragmentManager();
        statusFragment = (StatusFragment) fragmentManager.findFragmentById(R.id.statusFragment);
        gameFragment = (GameFragment) fragmentManager.findFragmentById(R.id.gameFragment);
        gameOverFragment = (GameOverFragment) fragmentManager.findFragmentById(R.id.gameOverFragment);
        Intent intent = getIntent();

        // Set the difficulty of the game
        Difficulty difficulty = (Difficulty) Objects.requireNonNull(intent.getExtras()).get(EXTRA_DIFFICULTY);
        assert difficulty != null;
        gameOverFragment.setDifficulty(difficulty);
        Log.i("GameActivity", difficulty.name().toLowerCase());
        questionBuilder = new QuestionBuilder(difficulty);

        // Add sound effects if sound is enabled
        playSounds = intent.getBooleanExtra("playSound", true);
        if (playSounds) {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            soundIds = new int[]{soundPool.load(this, R.raw.huh, 1), // Incorrect answer
                    soundPool.load(this, R.raw.stop, 1), // Third incorrect answer
                    soundPool.load(this, R.raw.yoink, 1), // Correct answer
                    soundPool.load(this, R.raw.bomb, 1), // Bomb thrown
                    soundPool.load(this, R.raw.alarm, 1) // Game over
            };

        }

        // Load previous state if one is saved
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

    // Updates fragments based on the current state of the game
    @Override
    public void onUpdate(State state) {
        switch (state) {
            case INCORRECT_GUESS: // Update display and generate a new question after too many guesses
                if (gameFragment.getIncorrectGuesses() == 3) {
                    gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                    statusFragment.resetTimeBonus();
                    if (playSounds) {
                        soundPool.play(soundIds[1], 1, 1, 1, 0, 1);
                    }
                } else {
                    if (playSounds) {
                        soundPool.play(soundIds[0], 1, 1, 1, 0, 1);
                    }
                }
                statusFragment.updateDetection(10);
                break;
            case CORRECT_GUESS: // Update game score and generate a new question
                if (playSounds) {
                    soundPool.play(soundIds[2], 1, 1, 1, 0, 1);
                }
                statusFragment.updateScore(gameFragment.updateScore(statusFragment.getTimeBonus()));
                gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                statusFragment.resetTimeBonus();
                break;
            case BOMB_THROWN: // Skip the current question
                if (playSounds) {
                    soundPool.play(soundIds[3], 1, 1, 1, 0, 1);
                }
                gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                statusFragment.updateDetection(5);
                statusFragment.resetTimeBonus();
                break;
            case GAME_OVER: // Stop the game and bring up the game over screen
                if (playSounds) {
                    soundPool.play(soundIds[4], 1, 1, 1, 0, 1);
                }
                fragmentManager.beginTransaction().hide(gameFragment).commit();
                gameFragment.hideKeyboard();
                fragmentManager.beginTransaction().show(gameOverFragment).commit();
                gameOverFragment.setScore(gameFragment.getScore());
                break;
            case NEW_GAME: // Reset the display and start a new game
                fragmentManager.beginTransaction().hide(gameOverFragment).commit();
                fragmentManager.beginTransaction().show(gameFragment).commit();
                gameFragment.newGame();
                statusFragment.reset();
                gameFragment.showNextQuestion(questionBuilder.buildQuestion());
                statusFragment.startTicking();
                break;
            case MAIN_MENU: // Exit back to the menu
                finish();
        }
    }
}