package edu.umd.radham95.cmsc436_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "Main Activity";
    static final int DAY = 0, WEEK = 1, MONTH = 2, YEAR = 3;
    DateFormat dateFormat;
    Calendar today;
    private int dateMode;
    ToggleButton mToggleDay, mToggleWeek, mToggleMonth, mToggleYear;

    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupMainScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    void LoadData(){

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
                        mToggleDay.setBackgroundColor(Color.BLACK);
                        mToggleDay.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleWeek.setOnCheckedChangeListener(null);
                        mToggleWeek.setChecked(false);
                        mToggleWeek.setBackgroundColor(Color.BLACK);
                        mToggleWeek.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleMonth.setOnCheckedChangeListener(null);
                        mToggleMonth.setChecked(false);
                        mToggleMonth.setBackgroundColor(Color.BLACK);
                        mToggleMonth.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        mToggleYear.setOnCheckedChangeListener(null);
                        mToggleYear.setChecked(false);
                        mToggleYear.setBackgroundColor(Color.BLACK);
                        mToggleYear.setOnCheckedChangeListener(mOnCheckedChangeListener);

                        buttonView.setChecked(true);
                        buttonView.setBackgroundColor(Color.GRAY);

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
