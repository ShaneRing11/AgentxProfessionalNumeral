package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* Manages database changes between versions
*/
public class AgentxDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "agentx";
    private static final int DB_VERSION = 1;

    AgentxDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Test data
        String[] names = new String[] {"James", "Daniel", "Aaron", "Lana", "Alistair", "Jason", "Jessie", "Denice", "Amy"};
        int[] scores = new int[] {70, 80, 100, 110, 150, 120, 180, 164, 201};
        String[] difficulties = new String[] {"easy", "hard", "medium", "expert", "medium", "easy", "easy", "hard", "medium"};
        ContentValues scoreValues;

        if (oldVersion < 1) {
            // Create the scores table
            db.execSQL("CREATE TABLE SCORES (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " NAME TEXT, "
                    + "SCORE INTEGER, "
                    + "DIFFICULTY TEXT);");
            // Add the test data to the table
            for (int i = 0; i < names.length; ++i) {
                scoreValues = new ContentValues();
                scoreValues.put("NAME", names[i]);
                scoreValues.put("SCORE", scores[i]);
                scoreValues.put("DIFFICULTY", difficulties[i]);
                db.insert("SCORES", null, scoreValues);
            }
        }
    }
}
