package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {

    private TextView score;
    private TextView message;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
        score = view.findViewById(R.id.score);
        message = view.findViewById(R.id.message);
        return view;
    }

    void setScore(int newScore) {
        score.setText(String.format(Locale.getDefault(), getString(R.string.score), newScore));
    }

    void setMessage(String message) {
        this.message.setText(message);
    }
}
