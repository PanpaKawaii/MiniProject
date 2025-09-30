package com.example.miniproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AccountDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Accounts.db";

    public static class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "accounts";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +
                    AccountEntry._ID + " INTEGER PRIMARY KEY," +
                    AccountEntry.COLUMN_NAME_USERNAME + " TEXT UNIQUE," +
                    AccountEntry.COLUMN_NAME_PASSWORD + " TEXT," +
                    AccountEntry.COLUMN_NAME_BALANCE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;

    public AccountDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
