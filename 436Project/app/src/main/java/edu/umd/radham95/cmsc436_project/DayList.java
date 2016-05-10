package edu.umd.radham95.cmsc436_project;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Raymond on 4/26/16.
 * Underlying data structures
 */
public class DayList {
    private final static String TAG = "DayList";
    private final static int DEFAULT_QUANTITY = 1;
    private final static double DEFAULT_TIME = 1.0;
    private static int NEXT_ID = 0;

    private TreeMap<Integer, Year> list;

    public class Meal{
        String name;
        Double calories;
        int quantity;

        private Meal(String name, Double calories){
            this.name = name;
            this.calories = calories;
            this.quantity = DEFAULT_QUANTITY;
        }

        protected int changeQuantity(int change){
            quantity += change;
            return quantity;
        }
    }

    public class Exercise{
        String name;
        Double caloriesPerHour;
        Double hours;

        private Exercise(String name, Double calories){
            this.name = name;
            this.caloriesPerHour = calories;
            this.hours = DEFAULT_TIME;
        }

        private Exercise(String name, Double caloriesPerHour, Double hours){
            this.name = name;
            this.caloriesPerHour = caloriesPerHour;
            this.hours = hours;
        }

        protected double changeTime(double change){
            hours += change;
            return hours;
        }
    }

    protected class Year extends TreeMap<Integer,Month>{
        int thisYear;

        private Year(int year){
            super();
            thisYear = year;
        }

        public Collection<Meal> getMeals(){
            LinkedList<Meal> ret = new LinkedList<>();

            for (Month m: this.values()){
                ret.addAll(m.getMeals());
            }
            return ret;
        }

        public Collection<Exercise> getExercises(){
            LinkedList<Exercise> ret = new LinkedList<>();

            for (Month m: this.values()){
                ret.addAll(m.getExercises());
            }
            return ret;
        }

        public double getMealCalories(){
            Double ret = 0.0;

            for (Month m: this.values()){
                ret += m.getMealCalories();
            }
            return ret;
        }

        public double getExerciseCalories(){
            Double ret = 0.0;

            for (Month m: this.values()){
                ret += m.getExerciseCalories();
            }
            return ret;
        }
    }

    protected class Month extends TreeMap<Integer,Week>{
        // Need to know so we can find the first of month's even
        // in middle of week
        int thisMonth;
        int parentYear;

        private Month(int year, int month){
            super();
            parentYear = year;
            thisMonth = month;
        }

        public Collection<Meal> getMeals(){
            LinkedList<Meal> ret = new LinkedList<>();

            for (Week w: this.values()){
                ret.addAll(w.getMeals());
            }
            return ret;
        }

        public Collection<Exercise> getExercises(){
            LinkedList<Exercise> ret = new LinkedList<>();

            for (Week w: this.values()){
                ret.addAll(w.getExercises());
            }
            return ret;
        }

        public double getMealCalories(){
            Double ret = 0.0;

            for (Week w: this.values()){
                ret += w.getMealCalories();
            }
            return ret;
        }

        public double getExerciseCalories(){
            Double ret = 0.0;

            for (Week w: this.values()){
                ret += w.getExerciseCalories();
            }
            return ret;
        }
    }

    protected class Week extends TreeMap<Integer,Day>{
        private Week(){
            super();
        }

        public Collection<Meal> getMeals(){
            LinkedList<Meal> ret = new LinkedList<>();

            for (Day d: this.values()){
                ret.addAll(d.meals.values());
            }
            return ret;
        }

        public Collection<Exercise> getExercises(){
            LinkedList<Exercise> ret = new LinkedList<>();

            for (Day d: this.values()){
                ret.addAll(d.exercises.values());
            }
            return ret;
        }

        public double getMealCalories(){
            Double ret = 0.0;

            for (Day d: this.values()){
                ret += d.getMealCalories();
            }
            return ret;
        }

        public double getExerciseCalories(){
            Double ret = 0.0;

            for (Day d: this.values()){
                ret += d.getExerciseCalories();
            }
            return ret;
        }
    }

    public class Day implements Comparable<Day>{
        Calendar cal;
        Hashtable<String,Meal> meals;
        Hashtable<String,Exercise> exercises;
        int parentYear;
        int parentMonth;
        int thisDay;
        int id;

        protected Day(int year, int month, int day){
            cal = new GregorianCalendar();
            cal.clear();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            meals = new Hashtable<>();
            exercises = new Hashtable<>();
            parentYear = year;
            parentMonth = month;
            thisDay = day;
            id = NEXT_ID;
            NEXT_ID++;
        }

        public int getId(){
            return id;
        }

        public void setId(int id){
            this.id = id;
        }

        public void dbUpdate(){
            Log.d(TAG,"Updating database");
            MainActivity.db.updateDay(this);
        }

        public String mealsToString(){
            String ret = "";
            for (Meal m: meals.values()){
                ret+= m.name+","+m.calories+","+m.quantity+" ";
            }
            return ret;
        }

        public String exercisesToString(){
            String ret = "";
            for (Exercise e: exercises.values()){
                ret+= e.name+","+e.caloriesPerHour+","+e.hours+" ";
            }
            return ret;
        }

        /*
        Returns false if the entry already existed and quantity is increased
         */
        public boolean addMeal(String name, Double calories){
            if (meals.containsKey(name)){
                Log.i(TAG,"Meal already exists in database for today, increasing quantity");
                Meal meal = meals.get(name);
                meal.quantity++;
                if (meal.calories != calories){
                    Log.i(TAG,"Different calories recorded for the meals, saving new value of "+
                    calories+" and deleting old value of "+meal.calories);
                    meal.calories = calories;
                }
                dbUpdate();
                return false;
            }else{
                Log.i(TAG,"Creating new meal entry for today");
                Meal meal = new Meal(name,calories);
                meals.put(name,meal);
                dbUpdate();
                return true;
            }
        }

        public boolean addMeal(Intent intent){
            String name = intent.getStringExtra("Label");
            Double calories = Double.parseDouble(intent.getStringExtra("Calories"));
            return addMeal(name,calories);
        }

        private void addMeal(Meal meal){
            meals.put(meal.name,meal);
            dbUpdate();
        }

        public boolean addExercise(String name, Double calories){
            if (exercises.containsKey(name)){
                Log.i(TAG,"Exercise already exists in database for today, increasing quantity");
                Exercise exercise = exercises.get(name);
                exercise.hours += 1;
                if (exercise.caloriesPerHour != calories){
                    Log.i(TAG,"Different calories recorded for the exercises, saving new value of "+
                            calories+" and deleting old value of "+exercise.caloriesPerHour);
                    exercise.caloriesPerHour = calories;
                }
                dbUpdate();
                return false;
            }else{
                Log.i(TAG, "Creating new exercise entry for today");
                Exercise exercise = new Exercise(name,calories);
                exercises.put(name,exercise);
                dbUpdate();
                return true;
            }
        }

        public boolean addExercise(String name, Double caloriesPerHour, Double hours){
            if (exercises.containsKey(name)){
                Log.i(TAG,"Exercise already exists in database for today, increasing quantity");
                Exercise exercise = exercises.get(name);
                exercise.hours += hours;
                if (exercise.caloriesPerHour != caloriesPerHour){
                    Log.i(TAG,"Different calories recorded for the exercises, saving new value of "+
                            caloriesPerHour+" and deleting old value of "+exercise.caloriesPerHour);
                    exercise.caloriesPerHour = caloriesPerHour;
                }
                dbUpdate();
                return false;
            }else{
                Log.i(TAG, "Creating new exercise entry for today");
                Exercise exercise = new Exercise(name,caloriesPerHour,hours);
                exercises.put(name,exercise);
                dbUpdate();
                return true;
            }
        }

        private void addExercise(Exercise exercise){
            exercises.put(exercise.name,exercise);
            dbUpdate();
        }

        public boolean addExercise(Intent intent) {
            String name = intent.getStringExtra("Exercise");
            Double calories = Double.parseDouble(intent.getStringExtra("Calories"));
            return addExercise(name, calories);
        }

        public double getMealCalories(){
            if (meals.size() == 0) return 0.0;

            Double ret = 0.0;
            for (Meal m: meals.values()){
                ret += m.calories * m.quantity;
            }

            return ret;
        }

        public Double getExerciseCalories(){
            if (exercises.size() == 0) return 0.0;

            Double ret = 0.0;
            for (Exercise e: exercises.values()){
                ret += e.caloriesPerHour * e.hours;
            }

            return ret;
        }

        public int changeMealQuantity(String mealName, int change){
            Meal meal = meals.get(mealName);
            if (meal == null){
                Log.e(TAG, "Cannot find meal when trying to change quantity");
                return -1;
            }else {
                int ret = meal.changeQuantity(change);
                if (meal.quantity <= 0){
                    this.meals.remove(mealName);
                    return 0;
                }
                return ret;
            }
        }

        public double changeExerciseTime(String exerciseName, double change){
            Exercise ex = exercises.get(exerciseName);
            if (ex == null){
                Log.e(TAG, "Cannot find exercise when trying to change time");
                return -1;
            }else{
                double ret = ex.changeTime(change);
                if (ex.hours <= 0.0){
                    this.exercises.remove(exerciseName);
                    return 0.0;
                }
                return ret;
            }
        }

        @Override
        public int compareTo(Day other){
            int year = cal.get(Calendar.YEAR);
            int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            int otherYear = other.cal.get(Calendar.YEAR);
            int otherDayOfYear = other.cal.get(Calendar.DAY_OF_YEAR);

            if (otherYear < year){
                return -1;
            }else if (otherYear > year){
                return 1;
            }else{
                if (otherDayOfYear < dayOfYear){
                    return -1;
                }else if (otherDayOfYear > dayOfYear){
                    return 1;
                }else{
                    return 0;
                }
            }
        }

        @Override
        public boolean equals(Object other){
            if (other instanceof Day){
                Day otherDay = (Day) other;
                if (compareTo(otherDay) == 0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }

        @Override
        public int hashCode(){
            int year = cal.get(Calendar.YEAR);
            int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            return year * 366 + dayOfYear;
        }
    }

    public DayList(){
        Log.d(TAG, "Creating new data structure, either for the first time, or with clear");
        list = new TreeMap<>();
    }

    private void addDay(Day day){
        int year = day.cal.get(Calendar.YEAR);
        int month = day.cal.get(Calendar.MONTH);
        int week = day.cal.get(Calendar.WEEK_OF_MONTH);
        int dayOfWeek = day.cal.get(Calendar.DAY_OF_WEEK);

        Year yearMap = list.get(year);
        if (yearMap == null){
            yearMap = new Year(year);
            list.put(year, yearMap);
        }

        Month monthMap = yearMap.get(month);
        if (monthMap == null){
            monthMap = new Month(year, month);
            yearMap.put(month, monthMap);
        }

        Week weekMap = monthMap.get(week);
        if (weekMap == null){
            weekMap = new Week();
            monthMap.put(week, weekMap);
        }

        // Now we have our week
        weekMap.put(dayOfWeek,day);
    }

    private int size(){
        // Returns the number of entries in the data structure
        int size = 0;

        if (list.size() == 0) return 0;

        for (Year y: list.values()){
            for (Month m: y.values()){
                for (Week w: m.values()){
                    size += w.size();
                }
            }
        }
        return size;
    }


    // Also adds day to structure and database
    private Day createNewDay(int year, int month, int day){
        Log.d(TAG, "Creating new day");
        Day newDay = new Day(year,month,day);
        addDay(newDay);
        Log.d(TAG,"Adding to database");
        MainActivity.db.addDay(newDay);
        return newDay;
    }

    // This one does not add to database, allows the database to add back to model
    public Day createNewDay(int id, int year, int month, int day, String meals, String exercises){
        Day ret = new Day(year,month,day);
        addDay(ret);
        ret.setId(id);

        for (String m: meals.split(" ")){
            Log.d(TAG,"Found Meal: "+m);
            String[] attrb = m.split(",");
            Meal meal = new Meal(attrb[0], Double.parseDouble(attrb[1]));
            meal.quantity = Integer.parseInt(attrb[2]);
            ret.addMeal(meal);
        }

        for (String e: exercises.split(" ")){
            Log.d(TAG,"Found Exercise: "+e);
            String[] attrb = e.split(",");
            Exercise ex = new Exercise(attrb[0], Double.parseDouble(attrb[1]));
            ex.hours = Double.parseDouble(attrb[2]);
            ret.addExercise(ex);
        }

        return ret;
    }

    public Day createNewDay(Calendar cal){
        return createNewDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    private Collection<Day> days(){
        Collection<Day> days = new LinkedList<>();

        if (list.size() == 0) return days;

        for (Year y: list.values()){
            for (Month m: y.values()){
                for (Week w: m.values()){
                    for (Day d: w.values()){
                        days.add(d);
                    }
                }
            }
        }
        return days;
    }


    public Day find(Calendar cal){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        Year yearMap = list.get(year);
        if (yearMap == null){
            yearMap = new Year(year);
            list.put(year, yearMap);
        }

        Month monthMap = yearMap.get(month);
        if (monthMap == null){
            monthMap = new Month(year, month);
            yearMap.put(month, monthMap);
        }

        Week weekMap = monthMap.get(week);
        if (weekMap == null){
            weekMap = new Week();
            monthMap.put(week, weekMap);
        }

        Day ret = weekMap.get(day);
        if (ret == null){
            ret = new Day(year,month,cal.get(Calendar.DAY_OF_MONTH));
            weekMap.put(day,ret);
        }

        return ret;
    }

    public Year getYear(Calendar cal){
        int year = cal.get(Calendar.YEAR);

        if (list.size() == 0) return null;
        return list.get(year);
    }

    public Month getMonth(Calendar cal){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);

        if (list.size() == 0) return null;

        Year yearMap= list.get(year);
        if (yearMap == null){
            yearMap = new Year(year);
            list.put(year, yearMap);
        }
        return yearMap.get(month);
    }

    public Week getWeek(Calendar cal){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int week = cal.get(Calendar.WEEK_OF_MONTH);

        if (list.size() == 0) return null;

        Year yearMap= list.get(year);
        if (yearMap == null){
            yearMap = new Year(year);
            list.put(year, yearMap);
        }
        Month monthMap = yearMap.get(month);
        if (monthMap == null){
            monthMap = new Month(year, month);
            yearMap.put(month, monthMap);
        }
        return monthMap.get(week);
    }

}
