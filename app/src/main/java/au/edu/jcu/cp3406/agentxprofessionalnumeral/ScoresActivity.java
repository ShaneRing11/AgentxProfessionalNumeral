package au.edu.jcu.cp3406.agentxprofessionalnumeral;

import android.os.Bundle;
import android.widget.ShareActionProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ScoresActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO fix crash on rotation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            ScoreFragment fragment;
            Bundle args;
            switch (position) {
                case 0:
                    args = new Bundle();
                    args.putString("difficulty", "easy");
                    fragment = new ScoreFragment();
                    fragment.setArguments(args);
                    return fragment;
                case 1:
                    args = new Bundle();
                    args.putString("difficulty", "medium");
                    fragment = new ScoreFragment();
                    fragment.setArguments(args);
                    return fragment;
                case 2:
                    args = new Bundle();
                    args.putString("difficulty", "hard");
                    fragment = new ScoreFragment();
                    fragment.setArguments(args);
                    return fragment;
                case 3:
                    args = new Bundle();
                    args.putString("difficulty", "expert");
                    fragment = new ScoreFragment();
                    fragment.setArguments(args);
                    return fragment;
            }
            return new ScoreFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] difficulties = getResources().getStringArray(R.array.difficulties);
            switch (position) {
                case 0:
                    return difficulties[0];
                case 1:
                    return difficulties[1];
                case 2:
                    return difficulties[2];
                case 3:
                    return difficulties[3];
            }
            return null;
        }
    }
}
