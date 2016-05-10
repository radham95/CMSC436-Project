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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Radha.
 */

public class MealFragment extends Fragment {
    static ViewGroup rootView;
    private static final String TAG = "Meal Fragment";
    private EditText mCalories;
    private EditText mLabel;
    private CheckBox mCheck;
    static private final int FAVORITE_REQUEST = 3;

    public MealFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "Meal Fragment selected");

        rootView = (ViewGroup) inflater.inflate(R.layout.add_meal, container, false);

        mLabel = (EditText) rootView.findViewById(R.id.foodItemEditText);
        mCalories = (EditText) rootView.findViewById(R.id.caloriesEditText);
        mCheck = (CheckBox) rootView.findViewById(R.id.FavoriteCheckBox);

        final Button submitButton = (Button) rootView.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = getLabel();
                String calories = getCalories();

                Intent data = new Intent();
                data.putExtra("Label", label);
                data.putExtra("Calories", calories);

                if (mCheck.isChecked()) {
                    data.putExtra("Checked", "true");
                } else {
                    data.putExtra("Checked", "false");
                }

                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });

        final ImageButton favoriteButton = (ImageButton) rootView.findViewById(R.id.Favorite_list);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivityForResult(intent, FAVORITE_REQUEST);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == FAVORITE_REQUEST && resultCode == Activity.RESULT_OK) {
            Meal meal = new Meal(intent);
            String mealLabel = meal.getName();
            String mealCalories = meal.getCalories();

            mLabel.setText(mealLabel);
            mCalories.setText(mealCalories);
        }
    }

    private String getLabel() {
        return mLabel.getText().toString();
    }

    private String getCalories() {
        return mCalories.getText().toString();
    }

}
