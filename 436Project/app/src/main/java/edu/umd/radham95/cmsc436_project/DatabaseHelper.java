package edu.umd.radham95.cmsc436_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raymond on 5/10/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "calorieManager";

    // Calorie table name
    private static final String TABLE_CALORIES = "calories";

    // Calorie Table Columns names
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_MEAL = "meals";
    private static final String KEY_EXER = "exercises";
    private static final String KEY_ID = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CALORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_YEAR + " TEXT,"
                + KEY_MONTH + " TEXT," + KEY_DAY + " TEXT,"
                + KEY_MEAL + " TEXT,"+ KEY_EXER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES);

        // Create tables again
        onCreate(db);
    }

    public void addDay(DayList.Day day){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_YEAR, day.parentYear);
        values.put(KEY_MONTH, day.parentMonth);
        values.put(KEY_DAY, day.thisDay);
        values.put(KEY_MEAL, day.mealsToString());
        values.put(KEY_EXER, day.exercisesToString());

        // insert into database
        db.insert(TABLE_CALORIES, null, values);
        db.close(); // Closing database connection
    }


    public DayList.Day getDay(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CALORIES, new String[]{KEY_ID,
                        KEY_YEAR, KEY_MONTH, KEY_DAY, KEY_MEAL, KEY_EXER}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DayList.Day day = MainActivity.dayList.createNewDay(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5));
        // return contact
        return day;
    }

    public List<DayList.Day> getAllDays(){
        List<DayList.Day> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CALORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DayList.Day day = MainActivity.dayList.createNewDay(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),cursor.getString(4),cursor.getString(5));

                // Adding contact to list
                contactList.add(day);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int getDayCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CALORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateDay(DayList.Day day){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_YEAR, day.parentYear);
        values.put(KEY_MONTH, day.parentMonth);
        values.put(KEY_DAY, day.thisDay);
        values.put(KEY_MEAL, day.mealsToString());
        values.put(KEY_EXER, day.exercisesToString());
        values.put(KEY_ID, day.getId());

        // updating row
        return db.update(TABLE_CALORIES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(day.getId()) });
    }

    public void deleteDay(DayList.Day day){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALORIES, KEY_ID + " = ?",
                new String[] { String.valueOf(day.getId()) });
        db.close();
    }
}
