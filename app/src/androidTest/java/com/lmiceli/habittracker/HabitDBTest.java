package com.lmiceli.habittracker;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.lmiceli.habittracker.model.DatabaseHelper;
import com.lmiceli.habittracker.model.Habit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class HabitDBTest extends ApplicationTestCase<Application> {

    private DatabaseHelper dbHelper;
    private static final String TAG = "HabitDBTest_";

    public HabitDBTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbHelper = DatabaseHelper.getInstance(mContext);
    }

    public void testCreateDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    public void testCreateHabit() {

        Habit habit = new Habit();
        habit.setDesc("Start programming on Android");
        dbHelper.addHabit(habit);
        Log.d(TAG, "testCreateHabit, id is: " + habit.getId());
        assertNotNull(habit.getId());
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}