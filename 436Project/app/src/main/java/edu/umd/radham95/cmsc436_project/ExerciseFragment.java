package edu.umd.radham95.cmsc436_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Radha.
 */
public class ExerciseFragment extends Fragment {

    static ViewGroup rootView;
    private static final String TAG = "Exercise Fragment";
    private EditText mExercise;
    private EditText mCaloriesBurned;

    public ExerciseFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "Exercise Fragment selected");

        rootView = (ViewGroup) inflater.inflate(R.layout.add_exercise, container, false);

        mExercise = (EditText) rootView.findViewById(R.id.exerciseEditText);
        mCaloriesBurned = (EditText) rootView.findViewById(R.id.caloriesBurnedEditText);

        final Button submitButton = (Button) rootView.findViewById(R.id.submitExercise);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = getLabel();
                String calories = getCalories();

                Intent data = new Intent();
                data.putExtra("Exercise", label);
                data.putExtra("Calories", calories);

                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });

        return rootView;
    }

    private String getLabel() {
        return mExercise.getText().toString();
    }

    private String getCalories() {
        return mCaloriesBurned.getText().toString();
    }

}
