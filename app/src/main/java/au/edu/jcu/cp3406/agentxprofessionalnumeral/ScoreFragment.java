package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private String difficulty;
    private SQLiteDatabase db;
    private Cursor cursor;

    public ScoreFragment(String difficulty) {
        this.difficulty = difficulty;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        ListView listScores = view.findViewById(R.id.listScores);
        SQLiteOpenHelper agentxDatabaseHelper = new AgentxDatabaseHelper(getContext());
        try {
            db = agentxDatabaseHelper.getReadableDatabase();
            cursor = db.query("SCORES",
                    new String[]{"NAME", "SCORE"},
                    "DIFFICULTY = ?",
                    new String[]{difficulty}
                    , null, null,
                    "SCORE DESC");
            cursor.moveToFirst();
            String[] scoresList = new String[cursor.getCount()];
            for (int i = 0; i < scoresList.length; ++i) {
                scoresList[i] = String.format("%s: %50d",
                        cursor.getString(0),
                        cursor.getInt(1));
                cursor.moveToNext();
            }
            ArrayAdapter<String> scoresAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
            scoresAdapter.addAll(scoresList);
            listScores.setAdapter(scoresAdapter);
        } catch (SQLException e) {
            Toast toast = Toast.makeText(getContext(), "Error loading scores", Toast.LENGTH_SHORT);
            toast.show();
        }
        return view;
    }


}