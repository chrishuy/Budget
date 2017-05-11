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
    public static final String TABLE_BUDGET = "Budget";
    public static final String TABLE_EXPENSE = "Expense";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dbBudget";
    public static final String CN_ID = "id";
    public static final String CN_INCOME = "income";
    public static final String CN_SAVING = "saving";
    public static final String CN_GROCERY = "grocery";
    public static final String CN_BILL = "bill";
    public static final String CN_BUDGET = "budget";
    public static final String CN_REMAINDER = "remainder";
    public static final String CN_TODAY = "createDate";
    public static final String CN_SPENT = "spent";
    public static final String CN_ITEM = "item";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String sql = "CREATE TABLE " + TABLE_BUDGET + "("
                + CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CN_INCOME + " FLOAT, "
                + CN_SAVING + " FLOAT, "
                + CN_GROCERY + " FLOAT, "
                + CN_BILL + " FLOAT, "
                + CN_BUDGET + " FLOAT, "
                + CN_REMAINDER + " FLOAT, "
                + CN_TODAY + " VARCHAR(45))";
        String sql2 = "CREATE TABLE " + TABLE_EXPENSE + "("
                + CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CN_BUDGET + " INTEGER, "
                + CN_SPENT + " FLOAT, "
                + CN_ITEM + " TEXT, "
                + CN_TODAY + " VARCHAR(45))";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
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
        return db.insert(TABLE_BUDGET, null, values);
    }

    public long insertExpense(ContentValues values) {
        SQLiteDatabase db = this.getWritable();
        if (db == null) return 0;
        return db.insert(TABLE_EXPENSE, null, values);
    }

    public int deleteBudget(String[] selectionArgs) {
        SQLiteDatabase db = this.getWritable();
        if (db == null) return 0;
        if (selectionArgs == null) {
            return db.delete(TABLE_BUDGET, null , null);
        }
        return db.delete(TABLE_BUDGET, "id=?" , selectionArgs);
    }

    public int deleteExpense(String[] selectionArgs) {
        SQLiteDatabase db = this.getWritable();
        if (db == null) return 0;
        if (selectionArgs == null) {
            return db.delete(TABLE_EXPENSE, null , null);
        }
        return db.delete(TABLE_EXPENSE, "id=?" , selectionArgs);
    }

    public int updateBudget(ContentValues values, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritable();
        if (db == null) return 0;
        return db.update(TABLE_BUDGET, values, "id=?", selectionArgs);
    }

    public int updateExpense(ContentValues values, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritable();
        if (db == null) return 0;
        return db.update(TABLE_EXPENSE, values, "budget=?", selectionArgs);
    }

    // Getting all budget
    public Cursor getAllBudget() {
        SQLiteDatabase db = this.getReadable();
        if (db == null) return null;
        return db.query(TABLE_BUDGET, new String[] {CN_ID, CN_INCOME, CN_SAVING, CN_GROCERY, CN_BILL, CN_BUDGET, CN_REMAINDER, CN_TODAY}, null, null, null, null, null);
    }

    // Getting all spent
    public Cursor getAllExpense() {
        SQLiteDatabase db = this.getReadable();
        if (db == null) return null;
        return db.query(TABLE_EXPENSE, new String[] {CN_ID, CN_SPENT, CN_ITEM, CN_TODAY}, null, null, null, null, null);
    }
}
