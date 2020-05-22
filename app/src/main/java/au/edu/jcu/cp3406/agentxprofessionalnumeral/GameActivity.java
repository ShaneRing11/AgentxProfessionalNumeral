package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import au.edu.jcu.cp3406.agentxprofessionalnumeral.Game.Difficulty;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_DIFFICULTY = "MEDIUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
