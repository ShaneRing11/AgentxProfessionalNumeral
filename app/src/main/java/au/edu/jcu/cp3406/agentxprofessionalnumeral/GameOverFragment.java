package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Fragment that displays options after the game ends
 */
public class GameOverFragment extends Fragment {

    private StateListener listener;
    private EditText name;

    public GameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        name = view.findViewById(R.id.name);
        Button submit = view.findViewById(R.id.submit);
        Button highScores = view.findViewById(R.id.highScores);
        Button newGame = view.findViewById(R.id.newGame);
        Button mainMenu = view.findViewById(R.id.mainMenu);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Submit high score, show confirmation message and hide elements
            }
        });
        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open high scores activity
            }
        });
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdate(State.NEW_GAME);
                // Unhide submission elements elements
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdate(State.MAIN_MENU);
            }
        });

        return view;
    }

    private void submitScore() {

    }
}
