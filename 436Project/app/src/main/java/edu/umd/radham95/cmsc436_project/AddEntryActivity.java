package edu.umd.radham95.cmsc436_project;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class AddEntryActivity extends FragmentActivity {
    static private final String TAG = "Add Entry Activity";
    static protected final int MEAL = 0, EXERCISE = 1;
    static private final int NUM_MODES = 2;
    protected int mode;
    private ToggleButton mToggleMeal, mToggleExercise;
    static private ViewPager mPager;
    static private PagerAdapter mPagerAdapter;

    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    /**
     * A simple fragment pager adapter
     */
    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0) {
                return new MealFragment();
            }else {
                return new ExerciseFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_MODES;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public PageListener(){}

        public void onPageSelected(int position) {
            Log.v(TAG, "page " + position + " selected");

            if (position == 0) {
                if (mode != MEAL) {
                    Log.d(TAG, " mode needs to be changed to Meal after view changed");
                    mToggleMeal.setChecked(true);
                }

            }else{
                if (mode != EXERCISE) {
                    Log.d(TAG, " mode needs to be changed to Exercise after view changed");
                    mToggleExercise.setChecked(true);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addentry_activity);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.addOnPageChangeListener(new PageListener());
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupMainScreen();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    void switchMode(int mode) {
        this.mode = mode;
    }

    void setupMainScreen(){
        mode = MEAL;

        mOnCheckedChangeListener =
                new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mToggleMeal.setOnCheckedChangeListener(null);
                        mToggleMeal.setChecked(false);
                        mToggleMeal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        mToggleMeal.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleExercise.setOnCheckedChangeListener(null);
                        mToggleExercise.setChecked(false);
                        mToggleExercise.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        mToggleExercise.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        buttonView.setChecked(true);
                        buttonView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.hilight));

                        if (!isChecked){
                            Log.v(TAG, "Date mode not changed, it remains the same");
                        }else{
                            if (buttonView.getId() == R.id.toggleMeal){
                                switchMode(MEAL);
                            }else if (buttonView.getId() == R.id.toggleExercise){
                                switchMode(EXERCISE);
                            }else{
                                Log.e(TAG, "Unknown toggle referred to onCheckedChangeListener");
                            }
                        }

                        Log.d(TAG,"Toggle recorded "+buttonView.getText()+" "+isChecked);
                        mPager.setCurrentItem(mode);
                    }
                };

        mToggleMeal = (ToggleButton) findViewById(R.id.toggleMeal);
        mToggleExercise = (ToggleButton) findViewById(R.id.toggleExercise);

        mToggleMeal.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mToggleExercise.setOnCheckedChangeListener(mOnCheckedChangeListener);

        mToggleMeal.setChecked(true);

    }
}
