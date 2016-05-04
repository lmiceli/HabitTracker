package com.lmiceli.habittracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmiceli on 20/04/2016.
 * TODO: first:
 * sqlite con open helper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "habitsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_HABIT = "habit";
//    private static final String TABLE_USERS = "users";

    // Post Table Columns
    private static final String KEY_HABIT_ID = "id";
//    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_HABIT_DESC = "desc";
    private static final String KEY_HABIT_LAT = "lat";
    private static final String KEY_HABIT_LNG = "lng";
    private static final String KEY_HABIT_DONE = "done";


    // Singleton instance
    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_HABIT_TABLE = "CREATE TABLE " + TABLE_HABIT +
                "(" +
                KEY_HABIT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
//                KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                KEY_HABIT_DESC + " TEXT," +
                KEY_HABIT_LAT + " FLOAT," +
                KEY_HABIT_LNG + " FLOAT," +
                KEY_HABIT_DONE + " INTEGER" +
                ")";

//        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
//                "(" +
//                KEY_USER_ID + " INTEGER PRIMARY KEY," +
//                KEY_USER_NAME + " TEXT," +
//                KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
//                ")";

        db.execSQL(CREATE_HABIT_TABLE);
//        db.execSQL(CREATE_USERS_TABLE);

    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABIT);
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    // TODO if necessary, create some abstraction for this.
    // Update if id present
    public void addHabit(Habit habit) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_HABIT_DESC, habit.getDesc());

            long id = db.insertOrThrow(TABLE_HABIT, null, values);

            if (id > -1){
                habit.setId(id);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add habit to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
//        String POSTS_SELECT_QUERY =
//                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
//                        TABLE_POSTS,
//                        TABLE_USERS,
//                        TABLE_POSTS, KEY_POST_USER_ID_FK,
//                        TABLE_USERS, KEY_USER_ID);
//
        // SELECT * FROM HABIT
        String HABIT_SELECT_QUERY =
                        String.format("SELECT * FROM %s",
                                TABLE_HABIT);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(HABIT_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
//                    User newUser = new User();
//                    newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
//                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));

                    Habit newHabit = new Habit();
                    newHabit.setDesc(cursor.getString(cursor.getColumnIndex(KEY_HABIT_DESC)));
                    newHabit.setLat(cursor.getFloat(cursor.getColumnIndex(KEY_HABIT_LAT)));
                    newHabit.setLng(cursor.getFloat(cursor.getColumnIndex(KEY_HABIT_LNG)));
//                    newHabit.user = newUser;
                    habits.add(newHabit);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get habits from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return habits;
    }

}
