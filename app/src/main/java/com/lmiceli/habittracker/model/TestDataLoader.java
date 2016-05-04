package com.lmiceli.habittracker.model;

import android.content.Context;

/**
 * Created by lmiceli on 27/04/2016.
 */
public class TestDataLoader {

    public static void loadTestData(Context context){
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

        Habit habit = new Habit();
        habit.setDesc("Mega Fake");
//        habits.add(habit);

    }

}
