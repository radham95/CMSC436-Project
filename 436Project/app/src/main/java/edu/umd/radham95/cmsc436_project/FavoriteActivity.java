package edu.umd.radham95.cmsc436_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_food);

        final EntryAdapter adapter = MainActivity.mAdapter;
        final ListView favoriteList;

        favoriteList = (ListView) findViewById(R.id.listView);

        List<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < adapter.getFavoritesCount(); i++) {
            Meal m = (Meal) adapter.getFavoriteItem(i);
            arrayList.add(m.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                arrayList);

        favoriteList.setAdapter(arrayAdapter);

        favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String foodItem = (String) favoriteList.getItemAtPosition(position);
                Meal fav_meal = adapter.getFavoriteItemByLabel(foodItem);

                Intent intent = new Intent();
                intent.putExtra("Label", fav_meal.getName());
                intent.putExtra("Calories", fav_meal.getCalories());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
