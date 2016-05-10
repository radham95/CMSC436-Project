package edu.umd.radham95.cmsc436_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radha on 4/27/16.
 */
public class EntryAdapter extends BaseAdapter {
    public final List<Meal> mEntries = new ArrayList<>();
    public static List<Meal> mFavorites = new ArrayList<>();
    private final Context mContext;

    public EntryAdapter(Context context) {

        mContext = context;
    }

    public void add(Meal meal) {

        mEntries.add(meal);
        notifyDataSetChanged();

    }

    public void clear() {
        mEntries.clear();
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {

        return mEntries.size();
    }

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    @Override
    public Object getItem(int pos) {

        return mEntries.get(pos);

    }

    public void addFavorite(Meal meal) {
        mFavorites.add(meal);
    }

    public int getFavoritesCount() {
        return mFavorites.size();
    }

    public Object getFavoriteItem(int pos) {
        return mFavorites.get(pos);
    }

    public Meal getFavoriteItemByLabel(String label) {
        for (int i = 0; i < mFavorites.size(); i++) {
            if ((mFavorites.get(i)).getName().equals(label)) {
                return mFavorites.get(i);
            }
        }

        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the current entry
        final Meal entry = mEntries.get(position);


        //Inflate the View for this entry from entries.xml
        RelativeLayout itemLayout = (RelativeLayout)convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemLayout = (RelativeLayout) inflater.inflate(R.layout.entries, null);
        }

        // Fill in specific Meal data

        //Display Label in TextView
        final TextView labelView = (TextView)itemLayout.findViewById(R.id.entry_label);
        labelView.setText(entry.getName());

        //Display Calories in TextView
        final TextView calorieView = (TextView)itemLayout.findViewById(R.id.entry_calorie);
        calorieView.setText(entry.getCalories());

        // Return the View
        return itemLayout;

    }
}


class Meal {
    public static final String ITEM_SEP = System.getProperty("line.separator");
    private String name = new String();
    private String calories = new String();

    public Meal(String name, String calories){

        this.name = name;
        this.calories = calories;
    }

    public Meal(Intent intent) {

        name = intent.getStringExtra("Label");
        calories = intent.getStringExtra("Calories");
    }

    public String getName() {
        return name;
    }

    public String getCalories() { return calories; }

}
