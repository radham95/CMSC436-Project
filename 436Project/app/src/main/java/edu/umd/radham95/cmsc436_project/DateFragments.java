package edu.umd.radham95.cmsc436_project;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Raymond on 4/26/16.
 * For viewpager layout thing
 */
public class DateFragments {
    private static final String TAG = "Date Fragments";
    static ViewGroup rootView;
    private final static int QUANTITY_STEP = 1;
    private final static double TIME_STEP = .25;

    private static void createMealHeader(ViewGroup rootView, TableLayout tableLayout){
        Context context = rootView.getContext();
        TableRow row = (TableRow) LayoutInflater.from(context).inflate(R.layout.row_in_table,null);
        TextView name = (TextView) row.findViewById(R.id.row_name);
        TextView value = (TextView) row.findViewById(R.id.row_value);
        TextView quantity = (TextView) row.findViewById(R.id.row_quantity);

        name.setText(context.getResources().getString(R.string.meals));
        value.setText(context.getResources().getString(R.string.calories));
        quantity.setText(context.getResources().getString(R.string.quantity));

        name.setTypeface(Typeface.DEFAULT_BOLD);
        value.setTypeface(Typeface.DEFAULT_BOLD);
        quantity.setTypeface(Typeface.DEFAULT_BOLD);

        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

        tableLayout.addView(row);
    }

    public static void createExerciseHeader(ViewGroup rootView, TableLayout tableLayout){
        Context context = rootView.getContext();
        TableRow row = (TableRow) LayoutInflater.from(context).inflate(R.layout.row_in_table, null);
        TextView name = (TextView) row.findViewById(R.id.row_name);
        TextView value = (TextView) row.findViewById(R.id.row_value);
        TextView quantity = (TextView) row.findViewById(R.id.row_quantity);

        name.setText(context.getResources().getString(R.string.exercises));
        value.setText(context.getResources().getString(R.string.calories_per_hour));
        quantity.setText(context.getResources().getString(R.string.hours));

        name.setTypeface(Typeface.DEFAULT_BOLD);
        value.setTypeface(Typeface.DEFAULT_BOLD);
        quantity.setTypeface(Typeface.DEFAULT_BOLD);

        // Hide the quantity change buttons, can only be used on meals/exercises in a day
        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

        tableLayout.addView(row);
    }

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

            if(MainActivity.dayList != null){
                final DayList.Day day = MainActivity.dayList.find(MainActivity.today);
                TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                TableLayout mealTable = (TableLayout) rootView.findViewById(R.id.dayMealsTable);
                TableLayout exerciseTable = (TableLayout) rootView.findViewById(R.id.dayExerciseTable);

                if (day == null){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                    Log.v(TAG, "Calories remaining saved as: " + (MainActivity.goal - 0));
                    caloriesView.setText(((MainActivity.goal) + ""));
                    caloriesConsumedView.setText("0");
                    caloriesBurnedView.setText("0");

                }else{
                    Log.v(TAG,"Found data to display for this day");

                    Double caloriesConsumed = day.getMealCalories();
                    Double caloriesBurned = day.getExerciseCalories();
                    Double calorieTotal = caloriesConsumed - caloriesBurned;

                    Log.v(TAG, "Calories remaining saved as: " + (MainActivity.goal - calorieTotal));
                    caloriesView.setText((MainActivity.goal - calorieTotal) + "");
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());

                    if (day.meals.size() > 0) DateFragments.createMealHeader(rootView, mealTable);
                    if (day.exercises.size() > 0) DateFragments.createExerciseHeader(rootView, exerciseTable);

                    for (DayList.Meal meal : day.meals.values()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView) row.findViewById(R.id.row_name)).setText(meal.name);
                        ((TextView) row.findViewById(R.id.row_value)).setText(meal.calories.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(meal.quantity + "");
                        Log.d(TAG, "Adding " + meal.name + " with " + meal.calories + " calories to the table");

                        final String mealName = meal.name;

                        ((Button) row.findViewById(R.id.row_minus)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "decreasing quantity of "+mealName);
                                day.changeMealQuantity(mealName, -1 * QUANTITY_STEP);
                                MainActivity.updateFragments();
                            }
                        });

                        ((Button) row.findViewById(R.id.row_plus)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "incresing quantity of "+mealName);
                                day.changeMealQuantity(mealName, QUANTITY_STEP);
                                MainActivity.updateFragments();
                            }
                        });

                        mealTable.addView(row);
                    }

                    for (DayList.Exercise exercise: day.exercises.values()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView) row.findViewById(R.id.row_name)).setText(exercise.name);
                        ((TextView) row.findViewById(R.id.row_value)).setText(exercise.caloriesPerHour.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(exercise.hours.toString());

                        final String exerciseName = exercise.name;

                        ((Button) row.findViewById(R.id.row_minus)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "decreasing quantity of " + exerciseName);
                                day.changeExerciseTime(exerciseName, -1*TIME_STEP);
                                MainActivity.updateFragments();
                            }
                        });

                        ((Button) row.findViewById(R.id.row_plus)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "incresing quantity of " + exerciseName);
                                day.changeExerciseTime(exerciseName, TIME_STEP);
                                MainActivity.updateFragments();
                            }
                        });

                        Log.d(TAG, "Adding " + exercise.name + " with " + exercise.caloriesPerHour + " calories to the table");
                        exerciseTable.addView(row);
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

            int numDaysInWeek = 7;

            if(MainActivity.dayList != null){
                DayList.Week week = MainActivity.dayList.getWeek(MainActivity.today);
                TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);
                TableLayout mealTable = (TableLayout) rootView.findViewById(R.id.weekMealTable);
                TableLayout exerciseTable = (TableLayout) rootView.findViewById(R.id.weekExerciseTable);
                Double weekGoal = MainActivity.goal * numDaysInWeek;

                if (week == null || week.isEmpty()){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                    caloriesView.setText(weekGoal + "");
                    caloriesConsumedView.setText("0");
                    caloriesBurnedView.setText("0");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    Double caloriesConsumed = week.getMealCalories();
                    Double caloriesBurned = week.getExerciseCalories();
                    Double calorieTotal = caloriesConsumed - caloriesBurned;

                    caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    Log.v(TAG, "Calories remaining saved as: " + (weekGoal - calorieTotal));

                    caloriesView.setText(weekGoal - calorieTotal + "");
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());

                    if (week.getMeals().size() > 0) DateFragments.createMealHeader(rootView, mealTable);
                    if (week.getExercises().size() > 0) DateFragments.createExerciseHeader(rootView, exerciseTable);

                    for (DayList.Meal meal : week.getMeals()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView) row.findViewById(R.id.row_name)).setText(meal.name);
                        ((TextView) row.findViewById(R.id.row_value)).setText(meal.calories.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(meal.quantity+"");

                        // Hide the quantity change buttons, can only be used on meals/exercises in a day
                        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
                        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

                        Log.d(TAG,"Adding "+meal.name+" with "+meal.calories+" calories to the table");
                        mealTable.addView(row);
                    }

                    for (DayList.Exercise exercise: week.getExercises()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView) row.findViewById(R.id.row_name)).setText(exercise.name);
                        ((TextView) row.findViewById(R.id.row_value)).setText(exercise.caloriesPerHour.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(exercise.hours.toString());

                        // Hide the quantity change buttons, can only be used on meals/exercises in a day
                        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
                        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

                        Log.d(TAG, "Adding " + exercise.name + " with " + exercise.caloriesPerHour + " calories to the table");
                        exerciseTable.addView(row);
                    }

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

            int numDaysInMonth = MainActivity.today.getActualMaximum(Calendar.MONTH);

            if(MainActivity.dayList != null){
                DayList.Month month = MainActivity.dayList.getMonth(MainActivity.today);
                TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);
                TableLayout mealTable = (TableLayout) rootView.findViewById(R.id.monthMealTable);
                TableLayout exerciseTable = (TableLayout) rootView.findViewById(R.id.monthExerciseTable);
                Double weekGoal = MainActivity.goal * numDaysInMonth;

                if (month == null || month.isEmpty()){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                    caloriesView.setText(weekGoal + "");
                    caloriesConsumedView.setText("0");
                    caloriesBurnedView.setText("0");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    Double caloriesConsumed = month.getMealCalories();
                    Double caloriesBurned = month.getExerciseCalories();
                    Double calorieTotal = caloriesConsumed - caloriesBurned;

                    caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    Log.v(TAG, "Calories remaining saved as: " + (weekGoal - calorieTotal));

                    caloriesView.setText(weekGoal - calorieTotal + "");
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());

                    if (month.getMeals().size() > 0) DateFragments.createMealHeader(rootView, mealTable);
                    if (month.getExercises().size() > 0) DateFragments.createExerciseHeader(rootView, exerciseTable);

                    for (DayList.Meal meal : month.getMeals()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView)row.findViewById(R.id.row_name)).setText(meal.name);
                        ((TextView)row.findViewById(R.id.row_value)).setText(meal.calories.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(meal.quantity+"");

                        // Hide the quantity change buttons, can only be used on meals/exercises in a day
                        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
                        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

                        Log.d(TAG,"Adding "+meal.name+" with "+meal.calories+" calories to the table");
                        mealTable.addView(row);
                    }

                    for (DayList.Exercise exercise: month.getExercises()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView) row.findViewById(R.id.row_name)).setText(exercise.name);
                        ((TextView) row.findViewById(R.id.row_value)).setText(exercise.caloriesPerHour.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(exercise.hours.toString());

                        // Hide the quantity change buttons, can only be used on meals/exercises in a day
                        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
                        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

                        Log.d(TAG, "Adding " + exercise.name + " with " + exercise.caloriesPerHour + " calories to the table");
                        exerciseTable.addView(row);
                    }

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

            int numDaysInYear = MainActivity.today.getActualMaximum(Calendar.YEAR);

            if(MainActivity.dayList != null){
                DayList.Year year = MainActivity.dayList.getYear(MainActivity.today);
                TextView caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                TextView caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                TextView caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);
                TableLayout mealTable = (TableLayout) rootView.findViewById(R.id.yearMealTable);
                TableLayout exerciseTable = (TableLayout) rootView.findViewById(R.id.yearExerciseTable);
                Double weekGoal = MainActivity.goal * numDaysInYear;

                if (year == null || year.isEmpty()){
                    Log.d(TAG,"Data has been initialized, but no information is present for this day");
                    caloriesView.setText(weekGoal + "");
                    caloriesConsumedView.setText("0");
                    caloriesBurnedView.setText("0");
                }else {
                    Log.v(TAG,"Found data to display for this day");
                    Double caloriesConsumed = year.getMealCalories();
                    Double caloriesBurned = year.getExerciseCalories();
                    Double calorieTotal = caloriesConsumed - caloriesBurned;

                    caloriesBurnedView = (TextView) rootView.findViewById(R.id.caloriesBurnedView);
                    caloriesConsumedView = (TextView) rootView.findViewById(R.id.caloriesConsumedView);
                    caloriesView = (TextView) rootView.findViewById(R.id.caloriesView);

                    Log.v(TAG, "Calories remaining saved as: " + (weekGoal - calorieTotal));

                    caloriesView.setText(weekGoal - calorieTotal + "");
                    caloriesConsumedView.setText(caloriesConsumed.toString());
                    caloriesBurnedView.setText(caloriesBurned.toString());

                    if (year.getMeals().size() > 0) DateFragments.createMealHeader(rootView, mealTable);
                    if (year.getExercises().size() > 0) DateFragments.createExerciseHeader(rootView, exerciseTable);

                    for (DayList.Meal meal : year.getMeals()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView)row.findViewById(R.id.row_name)).setText(meal.name);
                        ((TextView)row.findViewById(R.id.row_value)).setText(meal.calories.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(meal.quantity+"");

                        // Hide the quantity change buttons, can only be used on meals/exercises in a day
                        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
                        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

                        Log.d(TAG,"Adding "+meal.name+" with "+meal.calories+" calories to the table");
                        mealTable.addView(row);
                    }

                    for (DayList.Exercise exercise: year.getExercises()){
                        TableRow row = (TableRow) LayoutInflater.from(rootView.getContext()).inflate(R.layout.row_in_table,null);
                        ((TextView) row.findViewById(R.id.row_name)).setText(exercise.name);
                        ((TextView) row.findViewById(R.id.row_value)).setText(exercise.caloriesPerHour.toString());
                        ((TextView) row.findViewById(R.id.row_quantity)).setText(exercise.hours.toString());

                        // Hide the quantity change buttons, can only be used on meals/exercises in a day
                        ((Button) row.findViewById(R.id.row_minus)).setVisibility(View.INVISIBLE);
                        ((Button) row.findViewById(R.id.row_plus)).setVisibility(View.INVISIBLE);

                        Log.d(TAG, "Adding " + exercise.name + " with " + exercise.caloriesPerHour + " calories to the table");
                        exerciseTable.addView(row);
                    }

                }

            }
            return rootView;
        }
    }
}
