package edu.csulb.android.budget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kyo on 5/5/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Budget";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dbBudget";
    public static final String CN_ID = "id";
    public static final String CN_INCOME = "income";
    public static final String CN_SAVING = "saving";
    public static final String CN_GROCERY = "grocery";
    public static final String CN_BILL = "bill";
    public static final String CN_BUDGET = "budget";
    public static final String CN_TODAY = "createDate";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CN_INCOME+ " FLOAT, "
                + CN_SAVING + " FLOAT, "
                + CN_GROCERY + " FLOAT, "
                + CN_BILL + " FLOAT, "
                + CN_BUDGET + " FLOAT, "
                + CN_TODAY + " VARCHAR(45))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreate new db
        onCreate(db);
    }

    // Callback to return readable database
    private SQLiteDatabase getReadable() {
        SQLiteDatabase db;
        try {
            db = getReadableDatabase();
            return db;
        } catch (SQLiteException e) {
            Log.d("Error DB", "Database Opening exception for getReadable");
        }
        return null;
    }

    // Callback to return writable database
    private SQLiteDatabase getWritable() {
        SQLiteDatabase db;
        try {
            db = getWritableDatabase();
            return db;
        } catch (SQLiteException e) {
            Log.d("Error DB", "Database Opening exception for getWritable");
        }
        return null;
    }

    // Inserting new label into labels table
    public long insertBudget(ContentValues values) {
        SQLiteDatabase db = this.getWritable();
        if (db == null) return 0;
        return db.insert(TABLE_NAME, null, values);
    }

    public int deleteBudget(String[] selectionArgs) {
        SQLiteDatabase db = this.getReadable();
        if (db == null) return 0;
        if (selectionArgs == null) {
            return db.delete(TABLE_NAME, null , null);
        }
        return db.delete(TABLE_NAME, "id=?" , selectionArgs);
    }

    // Getting all budget
    public Cursor getAllBudget() {
        SQLiteDatabase db = this.getReadable();
        if (db == null) return null;
        return db.query(TABLE_NAME, new String[] {CN_ID, CN_INCOME, CN_SAVING, CN_GROCERY, CN_BILL, CN_BUDGET, CN_TODAY}, null, null, null, null, null);
    }
}
