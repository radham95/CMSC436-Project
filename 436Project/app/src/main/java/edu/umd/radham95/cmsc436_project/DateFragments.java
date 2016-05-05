package edu.umd.radham95.cmsc436_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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

        public void upDateCalories(){
            rootView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.day_fragment, null, false);

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Day Fragment selected");

            rootView = (ViewGroup) inflater.inflate(
                    R.layout.day_fragment, container, false);

            if(MainActivity.data != null){
                Model.Day day = MainActivity.data.dayList.find(MainActivity.today);
                TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                TableLayout table = (TableLayout) rootView.findViewById(R.id.dayTableMeals);

                if (day == null){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                    Log.v(TAG, "Calories remaining saved as: " + (MainActivity.data.goal - 0));
                    caloriesView.setText(((MainActivity.data.goal) + ""));
                    caloriesConsumedView.setText("0");
                    caloriesBurnedView.setText("0");

                }else{
                    Log.v(TAG,"Found data to display for this day");

                    Log.v(TAG, "Calories remaining saved as: " + (MainActivity.data.goal - day.getCalories()));
                    caloriesView.setText(((MainActivity.data.goal - day.getCalories())+""));
                    caloriesConsumedView.setText(day.getCaloriesConsumed().toString());
                    caloriesBurnedView.setText(day.getCaloriesBurned().toString());

                    for (Model.Meal meal: day.getMeals()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView)row.findViewById(R.id.row_name)).setText(meal.name);
                        ((TextView)row.findViewById(R.id.row_value)).setText(meal.calories.toString());
                        Log.d(TAG,"Adding "+meal.name+" with "+meal.calories+" calories to the table");
                        table.addView(row);
                    }
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
