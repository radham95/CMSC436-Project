package edu.umd.radham95.cmsc436_project;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by Raymond on 4/26/16.
 * Underlying data structures
 */
public class Model {
    public final static Double DEFAULT_EXERCISE_TIME = 1.0;
    public final static String MODEL_KEY = "MODEL";
    public final static String TAG = "Model";
    double calPerDay = 2000;

    /**
     * Converts date in the format of year, month, day to the number
     * day of the year
     *
     * @param year eg 2016
     * @param month 1-12
     * @param day 1-31
     * @return an integer representing the day of the year (1-366)
     */
    public static int dayMonthYear2DayOfYear(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * IDK why the people who wrote the gregorian calendar class didnt make this
     * static
     *
     * See GregorianCalendar.isLeapYear()
     */
    public static boolean isLeapYear(int year){
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * Data structure that represents a food input.
     */
    class Meal{
        String name;
        Double calories;

        public Meal(String name, Double calories){
            this.name = name;
            this.calories = calories;
        }
    }

    /**
     * Data structure that represents an exercise input
     */
    class Exercise{
        String name;
        Double caloriesPerHour;
        Double hours;

        // can either just give an input for calories or a calorie rate and time
        public Exercise(String name, Double calories){
            this.name = name;
            this.caloriesPerHour = calories;
            this.hours = DEFAULT_EXERCISE_TIME;
        }

        public Exercise(String name, Double caloriesPerHour, Double numHours){
            this.name = name;
            this.caloriesPerHour = caloriesPerHour;
            this.hours = numHours;
        }
    }

    /**
     * Data structure to represent all the activities in a day
     *
     * Calendar represents the date
     * todaysMeals represents the amount of food eaten on that date
     * todaysExercise represents the amount of exercise on that date
     */
    class Day implements Comparable<Day>{
        GregorianCalendar calendar;
        TreeSet<Meal> todaysMeals;
        TreeSet<Exercise> todaysExercise;

        private Day(){
            calendar = new GregorianCalendar();
            todaysMeals = new TreeSet<>();
            todaysExercise = new TreeSet<>();
        }

        private Day(int year, int month, int dayOfMonth){
            calendar = new GregorianCalendar(year,month,dayOfMonth);
            todaysMeals = new TreeSet<>();
            todaysExercise = new TreeSet<>();
        }

        private Day(int year, int dayOfYear){
            calendar = new GregorianCalendar();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            todaysMeals = new TreeSet<>();
            todaysExercise = new TreeSet<>();
        }

        public boolean addMeal(Meal meal){
            return todaysMeals.add(meal);
        }

        public boolean addExercise(Exercise ex){
            return todaysExercise.add(ex);
        }

        int dayOfWeek(){
            return calendar.get(Calendar.DAY_OF_WEEK);
        }

        /**
         * Calculate the total calorie allotment for this day
         *
         * @return caloric intake (Calories consumed - calories burned)
         */
        public Double getCalories(){
            Double ret = 0.0;

            for(Meal meal: todaysMeals){
                ret += meal.calories;
            }

            for(Exercise ex: todaysExercise){
                ret -= (ex.caloriesPerHour * ex.hours);
            }

            return ret;
        }

        public Double getCaloriesConsumed(){
            Double ret = 0.0;

            for(Meal meal: todaysMeals){
                ret += meal.calories;
            }
            return ret;
        }

        public Double getCaloriesBurned(){
            Double ret = 0.0;

            for(Exercise ex: todaysExercise){
                ret += (ex.caloriesPerHour * ex.hours);
            }
            return ret;
        }

        /**
         * Necessary for sorting
         *
         * Not that this differs from a Calendar compare because we dont have smaller time intervals
         * than a day.  This means that 00:01 and 23:59 on the same day are equal days for us.
         * Also, its okay to compare day of the year rather than month and day separately because
         * all dates should be apart of the same gregorian calendar system and leap years remain consistent
         * @param another - another day object
         * @return standard compareTo int
         */
        @Override
        public int compareTo(@NonNull Day another) {
            int year =  this.calendar.get(Calendar.YEAR);
            int anotherYear = another.calendar.get(Calendar.YEAR);

            if (year > anotherYear){
                return -1;
            }else if (year < anotherYear){
                return 1;
            }else{
                int day = this.calendar.get(Calendar.DAY_OF_YEAR);
                int anotherDay = another.calendar.get(Calendar.DAY_OF_YEAR);

                if (day > anotherDay){
                    return -1;
                }else if(day < anotherDay){
                    return 1;
                }else{
                    return 0;
                }
            }
        }
    }

    /**
     * I wanted to call this calendar but then I needed to rely heavily on Java's calendar class
     * This is just a collection of days
     *
     * Currently days are sorted by 'name' in a hashmap where their name or key is given by the
     * function getKey()
     *
     * I also realize there are not 366 days in a year but if I say that there are 355 days in a year
     * then I cannot produce a 1-to-1 map for every day in the key function
     */
    class DayList{
        HashMap<Long,Day> byName;

        public DayList(){
            byName = new HashMap<>();
        }

        public Long getKey(Day day){
            return getKey(day.calendar.get(Calendar.YEAR), day.calendar.get(Calendar.DAY_OF_YEAR));
        }

        /**
         * Returns the key to look up day in hashmap
         * @param year eg 2016
         * @param dayOfYear 1-366
         * @return key for hashmap
         */
        public Long getKey(int year, int dayOfYear){
            return (long) year * 366 + (long) dayOfYear;
        }

        /**
         * Creates a new Day object and adds it into this data structure
         * A day should not really need to be created unless you are using this method
         * The day constructor has been marked private in order to persuade you to create
         * one with this function
         * @param year eg 2016
         * @param dayOfYear 1-366
         * @return a new Day with the specified date and added to the calendar
         */
        public Day createNewDay(int year,int dayOfYear){
            Day today = new Day(year,dayOfYear);
            Long key = getKey(today);
            byName.put(key,today);
            return today;
        }

        public Day find(int year, int month, int day){
            return find(year, Model.dayMonthYear2DayOfYear(year,month,day));
        }

        /**
         * Searches the data structure for a day corresponding to this date
         * @param year eg 2016
         * @param dayOfYear 1-366
         * @return found day object or null if none currently exists
         */
        public Day find(int year, int dayOfYear){
            Long key = ((long) year) * 366 + (long) dayOfYear;
            return byName.get(key);
        }

        /**
         * Same as others, this one takes a calendar input instead
         * @param cal - calendar input
         * @return found day object or null if none exists
         */
        public Day find(Calendar cal){
            return find(cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));
        }

        /**
         * Given a specific date, this returns the first date corresponding to that week
         * If one does not exists it creates one
         * @param year eg 2016
         * @param dayOfYear 1-366
         * @return a day object representing the first sunday of the week
         */
        private Day findSunday(int year, int dayOfYear){
            Calendar today = Calendar.getInstance();
            today.set(Calendar.YEAR,year);
            today.set(Calendar.DAY_OF_YEAR,dayOfYear);
            int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);

            dayOfYear -= dayOfWeek - Calendar.SUNDAY;

            // Handle overflow
            // Note Jan 1 = dayOfYear 1
            if (dayOfYear < 1){
                dayOfYear += 365;
                year -= 1;
                if (new GregorianCalendar().isLeapYear(year)){
                    dayOfYear += 1;
                }
            }

            Day sunday = find(year, dayOfYear);

            if (sunday == null){
                sunday = createNewDay(year,dayOfYear);
            }

            return sunday;
        }

        /**
         * Given a specific date, this finds the first day of the month before this date
         * If one does not exists it creates one
         * @param year eg 2016
         * @param dayOfYear 1-366
         * @return a day object representing the first day of the month
         */
        private Day findFirstOfMonth(int year, int dayOfYear){
            Calendar today = Calendar.getInstance();
            today.set(Calendar.YEAR,year);
            today.set(Calendar.DAY_OF_YEAR,dayOfYear);
            int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);

            dayOfYear -= dayOfMonth - 1;

            // Handle overflow
            // Note Jan 1 = dayOfYear 1
            if (dayOfYear < 1){
                dayOfYear += 365;
                year -= 1;
                if (new GregorianCalendar().isLeapYear(year)){
                    dayOfYear += 1;
                }
            }

            Day first = find(year, dayOfYear);

            if (first == null){
                first = createNewDay(year, dayOfYear);
            }

            return first;
        }

        /**
         * Given a specific date, finds the first day of the given year
         * This is easy to do yourself since dayOfTheYear is obviously 0,
         * but this method will create a day if one does not exist
         * @param year eg 2016
         * @return a day object representing Jan 1 of the given year
         */
        private Day findFirstOfYear(int year){
            int dayOfYear = 1;

            Day first = find(year, dayOfYear);

            if (first == null){
                first = createNewDay(year,dayOfYear);
            }

            return first;
        }

        public LinkedList<Day> getWeek(Calendar cal){
            return getWeek(cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));
        }

        /**
         * Given a date, this returns a list of Days associated with that week
         * @param year  eg 2016
         * @param dayOfYear 1-366
         * @return a LinkedList of days representing a week, max length 7
         */
        public LinkedList<Day> getWeek(int year, int dayOfYear){
            LinkedList<Day> list = new LinkedList<>();

            Day sunday = findSunday(year, dayOfYear);

            if (sunday.calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                Log.e(TAG, "Failed to find correct sunday.  Year:"+year+" Day of Year:"+dayOfYear);
                return null;
            }

            ArrayList<Day> byTime = new ArrayList<>(byName.values());
            Collections.sort(byTime);

            int i = byTime.indexOf(sunday);

            Calendar weekLater = (Calendar) sunday.calendar.clone();
            weekLater.add(Calendar.DAY_OF_YEAR,7);
            //For sanity
            weekLater.add(Calendar.DAY_OF_YEAR,-1);
            weekLater.set(Calendar.HOUR,23);
            weekLater.set(Calendar.MINUTE, 59);
            weekLater.set(Calendar.SECOND, 59);

            Day cur;
            for (; i < 7; i++){
                try {
                    cur = byTime.get(i);
                    if (cur != null && cur.calendar.compareTo(weekLater) < 0) {
                        list.add(cur);
                    }
                }catch(IndexOutOfBoundsException e){
                    Log.d(TAG,"Last day exceeds bounds");
                }
            }

            return list;
        }

        public LinkedList<Day> getMonth(Calendar cal){
            return getMonth(cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));
        }

        /**
         * Given a date, this returns a list of Days associated with that month
         * @param year  eg 2016
         * @param dayOfYear 1-366
         * @return a LinkedList of days representing a week, max length 31
         */
        public LinkedList<Day> getMonth(int year, int dayOfYear){
            LinkedList<Day> list = new LinkedList<>();

            Day first = findFirstOfMonth(year, dayOfYear);

            if (first.calendar.get(Calendar.DAY_OF_MONTH) != 1){
                Log.e(TAG, "Failed to find correct first of month.  Year:"+year+" Day of Year:"+dayOfYear);
                return null;
            }

            ArrayList<Day> byTime = new ArrayList<>(byName.values());
            Collections.sort(byTime);

            int i = byTime.indexOf(first);

            Calendar monthLater = (Calendar) first.calendar.clone();
            monthLater.add(Calendar.MONTH,1);
            //For sanity
            monthLater.add(Calendar.DAY_OF_YEAR,-1);
            monthLater.set(Calendar.HOUR,23);
            monthLater.set(Calendar.MINUTE, 59);
            monthLater.set(Calendar.SECOND, 59);

            Day cur;
            for (; i < 31; i++){
                try{
                    cur = byTime.get(i);
                    if (cur != null && cur.calendar.compareTo(monthLater) < 0){
                        list.add(cur);
                    }
                }catch(IndexOutOfBoundsException e){
                    Log.d(TAG,"Last day exceeds bounds");
                }
            }

            return list;
        }

        public LinkedList<Day> getYear(Calendar cal){
            return getYear(cal.get(Calendar.YEAR));
        }

        /**
         * Given a date, this returns a list of Days associated with that year
         * @param year eg 2016
         * @return a LinkedList of days representing a week, max length 366
         */
        public LinkedList<Day> getYear(int year){
            LinkedList<Day> list = new LinkedList<>();

            Day first = findFirstOfYear(year);

            if (first.calendar.get(Calendar.DAY_OF_YEAR) != 1){
                Log.e(TAG, "Failed to find correct first of year.  Year:"+year);
                return null;
            }

            ArrayList<Day> byTime = new ArrayList<>(byName.values());
            Collections.sort(byTime);

            int i = byTime.indexOf(first);

            Calendar monthLater = (Calendar) first.calendar.clone();
            monthLater.add(Calendar.YEAR,1);
            //For sanity
            monthLater.add(Calendar.DAY_OF_YEAR,-1);
            monthLater.set(Calendar.HOUR,23);
            monthLater.set(Calendar.MINUTE, 59);
            monthLater.set(Calendar.SECOND, 59);

            Day cur;
            for (; i < 366; i++){
                try {
                    cur = byTime.get(i);
                    if (cur != null && cur.calendar.compareTo(monthLater) < 0) {
                        list.add(cur);
                    }
                }catch(IndexOutOfBoundsException e){
                    Log.d(TAG,"Last day exceeds bounds");
                }
            }

            return list;
        }
    }

    static HashMap<String, Meal> favoriteMeals;
    static HashMap<String, Exercise> favoriteExercises;
    static DayList dayList;

    /**
     * Should only be called once per user really
     */
    public Model(){
        favoriteMeals = new HashMap<>();
        favoriteExercises = new HashMap<>();
        dayList = new DayList();
    }

    /**
     * Searches the saved favoriteMeals for the given string
     * @param name key to hashmap
     * @return corresponding Meal, or Null if not found
     */
    public Meal findFavoriteMeal(String name){
        return favoriteMeals.get(name);
    }

    /**
     * Saves a Meal as favorite
     * @param meal - Meal to be added
     * @return true, false if failed
     */
    public boolean addFavoriteMeal(Meal meal){
        if (favoriteMeals.containsValue(meal)) {
            return false;
        }else{
            favoriteMeals.put(meal.name, meal);
            return true;
        }
    }

    /**
     * Searches the saved favoriteExercise for the given string
     * @param name - key to hashmap
     * @return corresponding Exercise, or Null if not found
     */
    public Exercise findFavoriteExercise(String name){
        return favoriteExercises.get(name);
    }

    /**
     * Saves a Meal as favorite
     * @param ex - Exercise to be added
     * @return true, false if failed
     */
    public boolean addFavoriteExercise(Exercise ex){
        if (favoriteExercises.containsValue(ex)) {
            return false;
        }else{
            favoriteExercises.put(ex.name, ex);
            return true;
        }
    }
}
