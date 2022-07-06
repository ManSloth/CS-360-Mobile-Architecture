package com.example.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;


public class WeightDatabase extends SQLiteOpenHelper {

    // create database file name
    private static final String DATABASE_NAME = "weights.db";
    private static final int VERSION = 1;

    public WeightDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // create weight table variables
    private static final class WeightTable {
        private static final String TABLE = "weights";
        private static final String COL_ID = "_id";
        private static final String COL_VALUE = "value";
        private static final String COL_DATE = "date";
    }

    // create login table variables
    private static final class LoginTable {
        private static final String TABLE = "login";
        private static final String LOG_ID = "_id";
        private static final String UNAME = "username";
        private static final String PWD = "password";
    }

    // create goal table variables
    private static final class GoalTable {
        private static final String TABLE = "goal";
        private static final String GOAL_ID = "_id";
        private static final String GOAL_VALUE = "value";
    }

    // if database doesn't exist, create it
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WeightTable.TABLE + " (" +
                WeightTable.COL_ID + " integer primary key autoincrement, " +
                WeightTable.COL_VALUE + " integer, " +
                WeightTable.COL_DATE + " text)");

        db.execSQL("create table " + LoginTable.TABLE + " (" +
                LoginTable.LOG_ID + " integer primary key autoincrement, " +
                LoginTable.UNAME + " text, " +
                LoginTable.PWD + " text )");

        db.execSQL("create table " + GoalTable.TABLE + " (" +
                GoalTable.GOAL_ID + " integer primary key autoincrement, " +
                GoalTable.GOAL_VALUE + " text )");

        Log.d("CREATION", "onCreate was called");
    }

    // if database does exist, update it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + WeightTable.TABLE);
        db.execSQL("drop table if exists " + LoginTable.TABLE);
        db.execSQL("drop table if exists " + GoalTable.TABLE);
        onCreate(db);
        Log.d("UPDATED", "onUpgrade was called");
    }

    // create a login account
    public long addLogin(String userName, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginTable.UNAME, userName);
        values.put(LoginTable.PWD, password);

        // username can't be blank
        if(userName.equals(""))
            return -3;

        // see if username exists
        boolean exists = getUsername((userName));

        if(!exists) {
            if(password.equals("") == true) { // password can't be blank
                Log.d("Exist", "Your password has to be longer than 0 characters!");
                return -1;
            }
            else { // successfully create account
                long loginID = db.insert(LoginTable.TABLE, null, values);
                Log.d("Exist", "User login created!");
                return loginID;
            }
        }
        else
        { // no duplicate accounts
            Log.d("Exist", "Username " + userName + " already exists!");
            return -2;
        }
    }

    // See if username exists
    public boolean getUsername(String userName) {
        SQLiteDatabase rdb = getReadableDatabase();

        String sql = "select * from " + LoginTable.TABLE + " where userName = ?";
        Cursor cursor = rdb.rawQuery(sql, new String[] {userName});


        int count = cursor.getCount();
        if(cursor != null && count > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // see if password matches
    public boolean checkPassword(String userName, String password) {
        SQLiteDatabase rdb = getReadableDatabase();

        String sql = "select * from " + LoginTable.TABLE + " where userName = ?";
        Cursor cursor = rdb.rawQuery(sql, new String[] {userName});
        int count = cursor.getCount();
        String correctPWD;

        if (cursor != null && count > 0) {
            cursor.moveToFirst();
            correctPWD = cursor.getString(2);
            if (password.equals(correctPWD)) {
                return true;
            } else {
                return false;
            }
        }

        else {
            return false;
        }
    }

    // add weight to database
    public long addWeight(int weight) {
        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        ContentValues values = new ContentValues();
        values.put(WeightTable.COL_VALUE, weight);
        values.put(WeightTable.COL_DATE, date);

        String sql = "select * from " + WeightTable.TABLE + " where date = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {date});

        int count = cursor.getCount();

        // if statement that allows to prevent double weight entries on the same day, but for
        // testing purposes both add a weight so you can spam the add button and fill the table
        if (cursor != null && count > 0) {
            Log.d("Exist", "Weight already added today! Put something in tomorrow!");
            long weightID = db.insert(WeightTable.TABLE, null, values);
            return -1;
        } else {
            long weightID = db.insert(WeightTable.TABLE, null, values);
            Log.d("Exist", "Weight added! weight: " + weight + ", On: " + date);
            return weightID;
        }
    }

    // returns a weight if we haven't reached the end of the table
    public String getWeight(int index) {
        SQLiteDatabase rdb = getReadableDatabase();
        Cursor  cursor = rdb.rawQuery("select * from " + WeightTable.TABLE,null);
        cursor.moveToPosition(index);

        if(!cursor.isAfterLast()) {
            return cursor.getString(1);
        }
        else return "Invalid";
    }

    // returns a date if we haven't reached the end of the table
    public String getDate(int index) {
        SQLiteDatabase rdb = getReadableDatabase();
        Cursor  cursor = rdb.rawQuery("select * from " + WeightTable.TABLE,null);
        cursor.moveToPosition(index);

        if(!cursor.isAfterLast()) {
            return cursor.getString(2);
        }
        else return "Invalid";
    }

    // returns a row id if we haven't reached the end of the table
    public String getID(int index) {
        SQLiteDatabase rdb = getReadableDatabase();

        Cursor  cursor = rdb.rawQuery("select * from " + WeightTable.TABLE,null);
        cursor.moveToPosition(index);

        if(!cursor.isAfterLast()) {
            return cursor.getString(0);
        }
        else return "Invalid";
    }

    // updates a weight when EditText is changed
    public void saveWeight(String index, String weight) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeightTable.COL_VALUE, weight);

        db.update(WeightTable.TABLE, values, "_id = ?", new String[]{index});
    }

    // updates date when EditText is changed
    public void saveDate(String index, String date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeightTable.COL_DATE, date);

        db.update(WeightTable.TABLE, values, "_id = ?", new String[]{index});

    }

    // Deletes a weight from the table
    public void deleteRow(String index) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(WeightTable.TABLE, WeightTable.COL_ID + "=?", new String[]{index});
    }

    // updates goal weight when EditText is changed
    public void updateGoal(String goal) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GoalTable.GOAL_VALUE, goal);
        Cursor cursor = db.rawQuery("select * from " + GoalTable.TABLE, null);

        long count = DatabaseUtils.queryNumEntries(db, GoalTable.TABLE);
        if (count > 0) {
            cursor.moveToFirst();
            db.update(GoalTable.TABLE, values, "_id = ?", new String[]{String.valueOf(1)});
        }
        else
            db.insert(GoalTable.TABLE, null, values);
    }

    // Get's the last weight in the table to represent your current weight
    public String getLastWeight() {
        SQLiteDatabase rdb = getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(rdb, WeightTable.TABLE);

        if (count >0) {
            Cursor cursor = rdb.rawQuery("select * from " + WeightTable.TABLE, null);
            cursor.moveToLast();

            return cursor.getString(1);
        }
        else
            return "???";
    }

    // retrieves the goal weight from the database
    public String getGoal() {
        SQLiteDatabase rdb = getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(rdb, GoalTable.TABLE);

        if (count >0) {
            Cursor cursor = rdb.rawQuery("select * from " + GoalTable.TABLE, null);
            cursor.moveToFirst();

            return cursor.getString(1);
        }
        else
            return "";
    }

}
