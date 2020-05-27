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
            switch (position) {
                case 0:
                    return new ScoreFragment("easy");
                case 1:
                    return new ScoreFragment("medium");
                case 2:
                    return new ScoreFragment("hard");
                case 3:
                    return new ScoreFragment("expert");
            }
            return new ScoreFragment("easy");
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
