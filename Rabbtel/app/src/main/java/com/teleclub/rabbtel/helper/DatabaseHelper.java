package com.teleclub.rabbtel.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teleclub.rabbtel.model.CallRecord;
import com.teleclub.rabbtel.model.Message;
import com.teleclub.rabbtel.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.CALL_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String messageTable = "CREATE TABLE IF NOT EXISTS " + Constants.CALL_TABLE +
                " (" + Constants.KEY_CALL_ID + " INTEGER PRIMARY KEY," +
                Constants.KEY_CALL_PHONE + " TEXT, " +
                Constants.KEY_CALL_ACCESS + " TEXT, " +
                Constants.KEY_CALL_TIME + " TEXT);";
        db.execSQL(messageTable);

        Log.d("SQLite", "Tables created");
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.CALL_TABLE);
        onCreate(db);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.CALL_TABLE);
        onCreate(db);
    }

    public ArrayList<CallRecord> getCallHistory() {
        ArrayList<CallRecord> callHistory = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Constants.CALL_TABLE;
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                CallRecord record = new CallRecord();

                if (c.getInt(c.getColumnIndexOrThrow(Constants.KEY_CALL_ID)) != -1)
                    record.setId(c.getInt(c.getColumnIndexOrThrow(Constants.KEY_CALL_ID)));
                if (c.getString(c.getColumnIndexOrThrow(Constants.KEY_CALL_PHONE)) != null)
                    record.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(Constants.KEY_CALL_PHONE)));
                if (c.getString(c.getColumnIndexOrThrow(Constants.KEY_CALL_ACCESS)) != null)
                    record.setAccessNumber(c.getString(c.getColumnIndexOrThrow(Constants.KEY_CALL_ACCESS)));
                if (c.getString(c.getColumnIndexOrThrow(Constants.KEY_CALL_TIME)) != null)
                    record.setCalledTime(c.getString(c.getColumnIndexOrThrow(Constants.KEY_CALL_TIME)));

                callHistory.add(record);
            } while (c.moveToNext());
        }

        Collections.sort(callHistory, new Comparator<CallRecord>() {
            @Override
            public int compare(CallRecord o1, CallRecord o2) {
                return o2.getCalledTime().compareTo(o1.getCalledTime());
            }
        });

        return callHistory;
    }

    public void addCallRecord(String phone, String accessNumber, String calledTime) {
        String insertQuery = "INSERT INTO " + Constants.CALL_TABLE +
                "(" + Constants.KEY_CALL_PHONE + ", " +
                Constants.KEY_CALL_ACCESS + ", " +
                Constants.KEY_CALL_TIME + ")" +
                " VALUES('" +
                phone + "', '" +
                accessNumber + "', '" +
                calledTime + "');";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertQuery);
        db.close();
    }

    public void updateTimeCallRecord(String phoneNumber, String calledTime) {
        String query = "UPDATE " + Constants.CALL_TABLE +
                " SET " + Constants.KEY_CALL_TIME + " = '" + calledTime +
                "' WHERE " + Constants.KEY_CALL_PHONE + " = '" + phoneNumber + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void removeCallRecord(int id) {
        String removeQuery = "DELETE FROM " + Constants.CALL_TABLE + " WHERE " + Constants.KEY_CALL_ID + "='" + id +"'";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(removeQuery);
        db.close();
    }

    public boolean isExistRecord(String phone) {
        boolean b;
        String query = "SELECT * FROM " + Constants.CALL_TABLE +
                " WHERE " + Constants.KEY_CALL_PHONE + " = '" + phone + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        b = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return b;
    }
}
