package edu.umd.radham95.cmsc436_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends FragmentActivity {
    static private final String TAG = "Main Activity";
    static private final int DAY = 0, WEEK = 1, MONTH = 2, YEAR = 3;
    static private final int NUM_MODES = 4;
    private DateFormat dateFormat;
    private int dateMode;
    private ToggleButton mToggleDay, mToggleWeek, mToggleMonth, mToggleYear;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    protected static Model data;
    protected static Calendar today;

    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    /**
     * A simple pager adapter that represents 4 fragments, in
     * sequence.
     */
    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DateFragments.DayFragment();
            }else if (position == 1) {
                return new DateFragments.WeekFragment();
            }else if (position == 2) {
                return new DateFragments.MonthFragment();
            }else {
                return new DateFragments.YearFragment();
            }

        }

        @Override
        public int getCount() {
            return NUM_MODES;
        }
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public PageListener(){}

        public void onPageSelected(int position) {
            Log.v(TAG, "page "+position+" selected");

            if (position == 0) {
                if (dateMode != DAY) {
                    Log.d(TAG, " Date mode needs to be changed to Day after view changed");
                    mToggleDay.setChecked(true);
                }
            }else if (position == 1) {
                if (dateMode != WEEK) {
                    Log.d(TAG, " Date mode needs to be changed to Week after view changed");
                    mToggleWeek.setChecked(true);
                }
            }else if (position == 2) {
                if (dateMode != MONTH) {
                    Log.d(TAG, " Date mode needs to be changed to Month after view changed");
                    mToggleMonth.setChecked(true);
                }
            }else{
                if (dateMode != YEAR) {
                    Log.d(TAG, " Date mode needs to be changed to Year after view changed");
                    mToggleYear.setChecked(true);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            // load data and replace this
            Log.e(TAG,"Saved state found, but no way to restore it yet");
            data = new Model();
        }else{
            Log.d(TAG, "No saved data found");
            data = new Model();
        }

        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById(R.id.pager);
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

    void decreaseDate(){
        if (dateMode == DAY){
            today.add(Calendar.DAY_OF_YEAR,-1);
        }else if(dateMode == WEEK) {
            today.add(Calendar.WEEK_OF_YEAR, -1);
        }else if(dateMode == MONTH) {
            today.add(Calendar.MONTH, -1);
        }else if(dateMode == YEAR) {
            today.add(Calendar.YEAR, -1);
        }else{
            Log.e(TAG, "Unknown date mode ("+dateMode+"), cannot decrease date");
        }
    }

    void increaseDate(){
        if (dateMode == DAY){
            today.add(Calendar.DAY_OF_YEAR,1);
        }else if(dateMode == WEEK) {
            today.add(Calendar.WEEK_OF_YEAR,1);
        }else if(dateMode == MONTH) {
            today.add(Calendar.MONTH,1);
        }else if(dateMode == YEAR) {
            today.add(Calendar.YEAR,1);
        }else{
            Log.e(TAG, "Unknown date mode ("+dateMode+"), cannot increase date");
        }
    }

    void switchMode(int mode){
        Log.d(TAG, "Date mode changed (From " + dateMode + " to " + mode + "), switching scenes");
        dateMode = mode;

        // Rebuild scene
        switchDate();
    }

    void switchDate(){
        Log.d(TAG, "Date changed, reloading scene");
        // Rebuild scene


        if(dateMode == WEEK) {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        }else if(dateMode == MONTH) {
            dateFormat = new SimpleDateFormat("MMMM yyyy");
        }else if(dateMode == YEAR){
            dateFormat = new SimpleDateFormat("yyyy");
        }else{
            dateFormat = new SimpleDateFormat("EEEE, MM/dd/yyyy");
        }

        String dateString = dateFormat.format(today.getTime());

        TextView dateView = (TextView) findViewById(R.id.dateView);
        dateView.setText(dateString);
        Log.v(TAG, "Today's Date recorded as: " + dateString);
    }

    void setupMainScreen(){
        today = Calendar.getInstance();
        dateMode = DAY;

        mOnCheckedChangeListener =
                new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mToggleDay.setOnCheckedChangeListener(null);
                        mToggleDay.setChecked(false);
                        mToggleDay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        mToggleDay.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleWeek.setOnCheckedChangeListener(null);
                        mToggleWeek.setChecked(false);
                        mToggleWeek.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        mToggleWeek.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleMonth.setOnCheckedChangeListener(null);
                        mToggleMonth.setChecked(false);
                        mToggleMonth.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        mToggleMonth.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleYear.setOnCheckedChangeListener(null);
                        mToggleYear.setChecked(false);
                        mToggleYear.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        mToggleYear.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        buttonView.setChecked(true);
                        buttonView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.hilight));

                        if (!isChecked){
                            Log.v(TAG, "Date mode not changed, it remains the same");
                        }else{
                            if (buttonView.getId() == R.id.toggleDay){
                                switchMode(DAY);
                            }else if (buttonView.getId() == R.id.toggleWeek){
                                switchMode(WEEK);
                            }else if (buttonView.getId() == R.id.toggleMonth){
                                switchMode(MONTH);
                            }else if (buttonView.getId() == R.id.toggleYear){
                                switchMode(YEAR);
                            }else{
                                Log.e(TAG, "Unknown toggle referred to onCheckedChangeListener");
                            }
                        }

                        Log.d(TAG,"Toggle recorded "+buttonView.getText()+" "+isChecked);
                        mPager.setCurrentItem(dateMode);
                    }
                };

        switchDate();

        ImageButton dateLeft = (ImageButton) findViewById(R.id.dateLeft);
        dateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseDate();
                switchDate();
            }
        });

        ImageButton dateRight = (ImageButton) findViewById(R.id.dateRight);
        dateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                increaseDate();
                switchDate();
            }
        });

        mToggleDay = (ToggleButton) findViewById(R.id.toggleDay);
        mToggleWeek = (ToggleButton) findViewById(R.id.toggleWeek);
        mToggleMonth = (ToggleButton) findViewById(R.id.toggleMonth);
        mToggleYear = (ToggleButton) findViewById(R.id.toggleYear);

        mToggleDay.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mToggleWeek.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mToggleMonth.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mToggleYear.setOnCheckedChangeListener(mOnCheckedChangeListener);

        mToggleDay.setChecked(true);
    }
}
