package com.example.login;

/**
 * Used when i had sqlite as the database
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UsersLoginRegister.db";
    private static final int DB_VERSION = 1;
    private static final String COL_1 = "ID";
    private static final String COL_2 = "username";
    private static final String COL_3 = "password";
    private static final String USER_TABLE_NAME = "users";
    private static final String RECORD_TABLE_NAME = "RecordUsers";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    //add user
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);
        db.insert("Users", null, contentValues);
        db.close();
        return true;
    }

    //add user
    public boolean addUserRecord(String username, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert("RecordUsers", null, contentValues);
        db.close();
        return true;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //user table
        String sqlUsers = "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);";

        //user attendance record table
        String sqlRecordUsers = "CREATE TABLE RecordUsers(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, date TEXT, time TEXT, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id));";

        db.execSQL(sqlUsers);
        db.execSQL(sqlRecordUsers);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sqlUsers = "DROP TABLE IF EXISTS users";
        String sqlRecordUsers = "DROP TABLE IF EXISTS RecordUsers";

        db.execSQL(sqlRecordUsers);
        db.execSQL(sqlUsers);
        onCreate(db);

    }

    //checking if user exists
    public boolean checkUser(String username, String password) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    //checking if user exists for manual attendance
    public boolean checkEmail(String username) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }


    //getting user info from database into a list


    //public Cursor getRecordData(String s){
    public Cursor getRecordData() {
        SQLiteDatabase db = getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT * FROM " + RECORD_TABLE_NAME, null);

        return db.rawQuery("SELECT * FROM " + RECORD_TABLE_NAME, null);

    }


   //getting specific user info
   public Cursor getSpecificData(String s) {
        SQLiteDatabase db = getReadableDatabase();
        String[] params = new String[]{ s };

      //  Cursor cursor = db.rawQuery("SELECT * FROM " + RECORD_TABLE_NAME, null);

        /*
         To get details of specific users
         */
       //Cursor cursor = db.rawQuery("SELECT * FROM " + RECORD_TABLE_NAME + " WHERE COL_2 == ?", new String[]{s + " "});


        return db.rawQuery("SELECT * FROM " + RECORD_TABLE_NAME + " WHERE COL_2 == ?", params);

        //return db.rawQuery("SELECT * FROM RECORD_TABLE_NAME WHERE COL_2 = ?", params);

    }


 /*   public boolean rowIdExists(String StrId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select COL_2 from " + USER_TABLE_NAME
                + " where id=?", new String[]{StrId});
        boolean exists = (cursor.getCount() > 0);
    cursor.close();
    db.close();
        return exists;
    }

    public boolean getData(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT COL_2 FROM contacts where COL_2= "+ s +"", null );
        return true;
    }*/


}