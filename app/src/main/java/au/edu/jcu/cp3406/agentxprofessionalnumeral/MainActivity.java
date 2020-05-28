package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;

/**
 * Activity containing the main menu of the app
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_DIFFICULTY = "MEDIUM";

    private Difficulty difficulty;
    private boolean playSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            difficulty = Difficulty.MEDIUM;
            playSound = true;
        } else {
            difficulty = (Difficulty) savedInstanceState.getSerializable("difficulty");
            playSound = savedInstanceState.getBoolean("playSound");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("difficulty", difficulty);
        outState.putBoolean("playSound", playSound);
    }

    // Gets the button that was clicked and opens the relevant activity
    public void buttonClicked(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.play: // Starts a new game
                intent = new Intent(this, GameActivity.class);
                intent.putExtra("playSound", playSound);
                intent.putExtra(GameActivity.EXTRA_DIFFICULTY, difficulty);
                startActivity(intent);
                break;
            case R.id.highScores: // Opens the high score screen
                intent = new Intent(this, ScoresActivity.class);
                intent.putExtra(ScoresActivity.EXTRA_DIFFICULTY, difficulty);
                startActivity(intent);
                break;
            case R.id.settings: // Opens the setting menu
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("playSound", playSound);
                intent.putExtra(SettingsActivity.EXTRA_DIFFICULTY, difficulty);
                startActivityForResult(intent, 751);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SettingsActivity.SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                difficulty = (Difficulty) data.getSerializableExtra(EXTRA_DIFFICULTY);
                playSound = data.getBooleanExtra("playSound", true);
            }
        }
    }
}