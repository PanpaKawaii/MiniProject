package com.example.miniproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccountManager {

    private static AccountManager instance;
    private AccountDbHelper dbHelper;

    private AccountManager(Context context) {
        dbHelper = new AccountDbHelper(context.getApplicationContext());
        addDefaultAccounts(); // Add default accounts on first creation
    }

    public static synchronized AccountManager getInstance(Context context) {
        if (instance == null) {
            instance = new AccountManager(context.getApplicationContext());
        }
        return instance;
    }

    private void addDefaultAccountIfNotExists(String username, String password, int balance) {
        if (!usernameExists(username)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_USERNAME, username);
            values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_PASSWORD, password);
            values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_BALANCE, balance);
            db.insert(AccountDbHelper.AccountEntry.TABLE_NAME, null, values);
            // db.close(); // Not closing writable db here as it might be reused by getInstance
        }
    }

    private void addDefaultAccounts() {
        addDefaultAccountIfNotExists("user1", "pass1", 1000);
        addDefaultAccountIfNotExists("user2", "pass2", 1000);
        addDefaultAccountIfNotExists("user3", "pass3", 1000);
    }

    public boolean usernameExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                AccountDbHelper.AccountEntry._ID
        };
        String selection = AccountDbHelper.AccountEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                AccountDbHelper.AccountEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        // db.close(); // Not closing readable db here as it might be reused by getInstance
        return exists;
    }

    public boolean addUser(String username, String password) {
        if (usernameExists(username)) {
            return false; // Username already exists
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_USERNAME, username);
        values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_PASSWORD, password);
        values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_BALANCE, 1000);

        long newRowId = db.insert(AccountDbHelper.AccountEntry.TABLE_NAME, null, values);
        // db.close();
        return newRowId != -1;
    }

    public int updateBalance(String username, int newBalance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountDbHelper.AccountEntry.COLUMN_NAME_BALANCE, newBalance);
        String selection = AccountDbHelper.AccountEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        db.update(
                AccountDbHelper.AccountEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        // db.close();
        return newBalance;
    }

    public int getMoney(String username) {
        int money = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                AccountDbHelper.AccountEntry.COLUMN_NAME_BALANCE
        };
        String selection = AccountDbHelper.AccountEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                AccountDbHelper.AccountEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                money = cursor.getInt(cursor.getColumnIndexOrThrow(AccountDbHelper.AccountEntry.COLUMN_NAME_BALANCE));
            }
            cursor.close();
        }
        // db.close();
        return money;
    }


    public boolean isValidUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                AccountDbHelper.AccountEntry.COLUMN_NAME_PASSWORD
        };
        String selection = AccountDbHelper.AccountEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                AccountDbHelper.AccountEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String storedPassword = null;
        if (cursor.moveToFirst()) {
            storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(AccountDbHelper.AccountEntry.COLUMN_NAME_PASSWORD));
        }
        cursor.close();
        // db.close();
        return storedPassword != null && storedPassword.equals(password);
    }
}
