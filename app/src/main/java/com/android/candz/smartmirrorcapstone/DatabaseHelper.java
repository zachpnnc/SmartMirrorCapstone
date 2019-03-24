package com.android.candz.smartmirrorcapstone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_NAME = "user_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "USERNAME";
    private static final String COL3 = "PASSWORD";
    private static final String COL4 = "ZIPCODE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                " USERNAME TEXT, PASSWORD TEXT, ZIPCODE TEXT)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String username, String password, String zipcode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, username);
        contentValues.put(COL3, password);
        contentValues.put(COL4, zipcode);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor getData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return sqLiteDatabase.rawQuery(query, null);
    }

    public boolean isEmpty() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            return true;
        }

        cursor.close();

        return false;
    }

    public boolean searchName(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = " + "'" + name + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean searchPassword(String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL3 + " FROM " + TABLE_NAME +
                " WHERE " + COL3 + " = " + "'" + password + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkLogin(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL3 + " FROM " + TABLE_NAME +
                " WHERE " + COL1 + " = " + "'" + id + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            return false;
        }
        cursor.close();
        return true;
    }

    public Cursor getItemID(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = " + "'" + name + "'";
        return sqLiteDatabase.rawQuery(query, null);
    }

    public void updateName(String oldName, String newName, int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" +
                id + "'" + " AND " + COL2 + " = '" + oldName + "'";
        sqLiteDatabase.execSQL(query);
    }

    public void deleteUser(String name, int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL1 + " = '" + id + "'" + " AND " + COL2 + "='" + name + "'";
        sqLiteDatabase.execSQL(query);
    }
}
