package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.Objects;


/**
 * A fragment that displays a list of scores for a single difficulty
 */
public class ScoreFragment extends Fragment {

    public ScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        assert getArguments() != null;
        ListView listScores = view.findViewById(R.id.listScores);

        // Query the database for a list of scores matching the difficulty and display them
        SQLiteOpenHelper agentxDatabaseHelper = new AgentxDatabaseHelper(getContext());
        try {
            SQLiteDatabase db = agentxDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("SCORES",
                    new String[]{"NAME", "SCORE"},
                    "DIFFICULTY = ?",
                    new String[]{getArguments().getString("difficulty")},
                    null, null,
                    "SCORE DESC");
//            cursor.moveToFirst();

            // Build an array of scores from the selection selection
            String[] scoresList = new String[cursor.getCount()];
            for (int i = 0; i < scoresList.length; ++i) {
                scoresList[i] = String.format(Locale.getDefault(),
                        "%-50s%3d",
                        cursor.getString(0),
                        cursor.getInt(1));
                cursor.moveToNext();
            }
            // Close the database
            cursor.close();
            db.close();

            // Add the array to the ArrayAdapter
            ArrayAdapter<String> scoresAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.leaderboard_item);
            scoresAdapter.addAll(scoresList);
            listScores.setAdapter(scoresAdapter);
        } catch (SQLException e) {
            Toast toast = Toast.makeText(getContext(), "Error loading scores", Toast.LENGTH_SHORT);
            toast.show();
        }
        return view;
    }


}