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
    static final int DEFAULT_DATE_MODE = DAY;
    DateFormat dateFormat;
    Calendar today;
    private int dateMode = DEFAULT_DATE_MODE;
    ToggleButton mToggleDay, mToggleWeek, mToggleMonth, mToggleYear;

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
        Log.d(TAG, "Date mode changed, switching scenes");
        dateMode = mode;

        // Rebuild scene
    }

    void switchDate(){
        Log.d(TAG, "Date changed, reloading scene");
        // Rebuild scene

        String dateString = dateFormat.format(today.getTime());

        TextView dateView = (TextView) findViewById(R.id.dateView);
        dateView.setText(dateString);
        Log.v(TAG, "Today's Date recorded as: " + dateString);
    }

    void setupMainScreen(){
        today = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mToggleDay.setBackgroundColor(Color.BLACK);
                        mToggleWeek.setBackgroundColor(Color.BLACK);
                        mToggleMonth.setBackgroundColor(Color.BLACK);
                        mToggleYear.setBackgroundColor(Color.BLACK);
                        buttonView.setBackgroundColor(Color.GRAY);

                        if (!isChecked){
                            buttonView.setChecked(true);
                        }

                        Log.d(TAG,"Toggle recorded "+buttonView.getText()+" "+isChecked);


                    }
                };

        mToggleDay.setOnCheckedChangeListener(onCheckedChangeListener);
        mToggleWeek.setOnCheckedChangeListener(onCheckedChangeListener);
        mToggleMonth.setOnCheckedChangeListener(onCheckedChangeListener);
        mToggleYear.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
