package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;


/**
 * A fragment displaying game information
 */
public class StatusFragment extends Fragment {

    private StateListener listener;
    private TextView score;
    private TextView message;
    private int detection;
    private int timeBonus;
    private Handler handler;
    private Runnable tick;
    private boolean gameRunning;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
        score = view.findViewById(R.id.score);
        message = view.findViewById(R.id.message);
        handler = new Handler();
        if (savedInstanceState == null) {
            detection = 0;
            timeBonus = 15;
            score.setText(String.format(Locale.getDefault(), getString(R.string.score), 0));
        } else {
            detection = savedInstanceState.getInt("detection");
            timeBonus = savedInstanceState.getInt("timeBonus");
            gameRunning = savedInstanceState.getBoolean("gameRunning");
        }
        if (detection > 100) {
            message.setText(getString(R.string.spotted));
        } else {
            message.setText(String.format(Locale.getDefault(), getString(R.string.detection), detection));
        }
        return view;
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(tick);
        gameRunning = detection < 100;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gameRunning) {
            startTicking();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("detection", detection);
        bundle.putInt("timeBonus", timeBonus);
        bundle.putBoolean("gameRunning", gameRunning);
    }

    void startTicking() {

        tick = new Runnable() {
            @Override
            public void run() {
                updateDetection(1);
                if (timeBonus > 0) {
                    --timeBonus;
                }
                handler.postDelayed(this, 1200);
            }
        };
        handler.post(tick);
    }

    void updateScore(int newScore) {
        score.setText(String.format(Locale.getDefault(), getString(R.string.score), newScore));
    }

    void updateDetection(int addedDetection) {
        detection += addedDetection;
        if (detection < 100) {
            if (isAdded()) {
                message.setText(String.format(Locale.getDefault(), getString(R.string.detection), detection));
            }
        } else {
            gameRunning = false;
            message.setText(R.string.spotted);
            handler.removeCallbacks(tick);
            listener.onUpdate(State.GAME_OVER);
        }
    }

    void resetTimeBonus() {
        timeBonus = 15;
    }

    int getTimeBonus() {
        return timeBonus;
    }

    void reset() {
        detection = 0;
        timeBonus = 15;
        score.setText(String.format(Locale.getDefault(), getString(R.string.score), 0));
    }
}
