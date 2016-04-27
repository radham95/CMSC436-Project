package edu.umd.radham95.cmsc436_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Raymond on 4/26/16.
 * For viewpager layout thing
 */
public class DateFragments {
    private static final String TAG = "Date Fragments";
    static ViewGroup rootView;


    static public class DayFragment extends Fragment {
        public DayFragment(){
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Day Fragment selected");

            rootView = (ViewGroup) inflater.inflate(
                    R.layout.day_fragment, container, false);

            if(MainActivity.data != null){
                Model.Day day = MainActivity.data.dayList.find(MainActivity.today);
                if (day == null){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    caloriesView.setText(day.getCalories().toString());
                    caloriesConsumedView.setText(day.getCaloriesConsumed().toString());
                    caloriesBurnedView.setText(day.getCaloriesBurned().toString());
                }

            }
                return rootView;
        }
    }

    static public class WeekFragment extends Fragment {
        public WeekFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Week Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.week_fragment, container, false);

            if(MainActivity.data != null){
                LinkedList<Model.Day> week = MainActivity.data.dayList.getWeek(MainActivity.today);
                if (week == null || week.isEmpty()){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    Double caloriesBurned = 0.0, caloriesConsumed = 0.0;
                    for (Model.Day day: week){
                        caloriesBurned += day.getCaloriesBurned();
                        caloriesConsumed += day.getCaloriesConsumed();
                    }

                    caloriesView.setText(((Double) (caloriesConsumed-caloriesBurned)).toString());
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());
                }

            }

            return rootView;
        }
    }

    static public class MonthFragment extends Fragment {
        public MonthFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Month Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.month_fragment, container, false);

            if(MainActivity.data != null){
                LinkedList<Model.Day> month = MainActivity.data.dayList.getMonth(MainActivity.today);
                if (month == null || month.isEmpty()){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    Double caloriesBurned = 0.0, caloriesConsumed = 0.0;
                    for (Model.Day day: month){
                        caloriesBurned += day.getCaloriesBurned();
                        caloriesConsumed += day.getCaloriesConsumed();
                    }

                    caloriesView.setText(((Double) (caloriesConsumed-caloriesBurned)).toString());
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());
                }

            }

            return rootView;
        }
    }

    static public class YearFragment extends Fragment {
        public YearFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Year Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.year_fragment, container, false);

            if(MainActivity.data != null){
                LinkedList<Model.Day> year = MainActivity.data.dayList.getYear(MainActivity.today);
                if (year == null || year.isEmpty()){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    Double caloriesBurned = 0.0, caloriesConsumed = 0.0;
                    for (Model.Day day: year){
                        caloriesBurned += day.getCaloriesBurned();
                        caloriesConsumed += day.getCaloriesConsumed();
                    }

                    caloriesView.setText(((Double) (caloriesConsumed-caloriesBurned)).toString());
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());
                }

            }

            return rootView;
        }
    }
}
