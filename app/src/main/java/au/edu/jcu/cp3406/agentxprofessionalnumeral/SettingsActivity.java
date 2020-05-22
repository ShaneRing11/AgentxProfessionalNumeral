package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Objects;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;

public class SettingsActivity extends AppCompatActivity {

    protected static int SETTINGS_REQUEST = 751;

    public static final String EXTRA_DIFFICULTY = "MEDIUM";

    private Spinner difficulty;
    private Switch playSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        difficulty = findViewById(R.id.difficulty);
        playSound = findViewById(R.id.playSound);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Difficulty oldDifficulty = (Difficulty) Objects.requireNonNull(intent.getExtras()).get(EXTRA_DIFFICULTY);
            assert oldDifficulty != null;
            switch (oldDifficulty) {
                case EASY:
                    difficulty.setSelection(0);
                    break;
                case MEDIUM:
                    difficulty.setSelection(1);
                    break;
                case HARD:
                    difficulty.setSelection(2);
                    break;
                case EXPERT:
                    difficulty.setSelection(3);
                    break;
            }
            playSound.setChecked(intent.getBooleanExtra("playSound", true));
        } else {
            difficulty.setSelection(savedInstanceState.getInt("difficulty"));
            playSound.setChecked(savedInstanceState.getBoolean("playSound"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("difficulty", difficulty.getSelectedItemPosition());
        bundle.putBoolean("playSound", playSound.isChecked());
    }

    public void saveClicked(View view) {
        Intent intent = new Intent();
        Difficulty newDifficulty = Difficulty.MEDIUM;
        switch (difficulty.getSelectedItemPosition()) {
            case 0:
                newDifficulty = Difficulty.EASY;
                break;
            case 2:
                newDifficulty = Difficulty.HARD;
                break;
            case 3:
                newDifficulty = Difficulty.EXPERT;
                break;
        }
        intent.putExtra(MainActivity.EXTRA_DIFFICULTY, newDifficulty);
        intent.putExtra("playSound", playSound.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }
}
