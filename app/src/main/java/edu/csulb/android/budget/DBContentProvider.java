package edu.csulb.android.budget;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kyo on 5/5/17.
 */

public class DBContentProvider extends ContentProvider {
    // SQLiteOpenHelper
    private DBHelper dbHelper;
    // Authority is the symbolic name of the content provider
    private static final String AUTHORITY = "edu.csulb.android.budget.contentprovider";
    // Content URI from the authority by appending path to database table
    public static final Uri CONTENT_URI_BUDGET = Uri.parse("content://" + AUTHORITY + "/budget");
    public static final Uri CONTENT_URI_EXPENSE = Uri.parse("content://" + AUTHORITY + "/expense");
    // Constant to identify the requested operation
    private static final int BUDGET = 1;
    private static final int EXPENSE = 2;
    // UriMatcher to check for corresponding URI
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "budget", BUDGET);
        uriMatcher.addURI(AUTHORITY, "expense", EXPENSE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                cursor = dbHelper.getAllBudget();
                break;
            case EXPENSE:
                cursor = dbHelper.getAllExpense();
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri res;
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                res = Uri.parse(CONTENT_URI_BUDGET + "/" + dbHelper.insertBudget(values));
                break;
            case EXPENSE:
                res = Uri.parse(CONTENT_URI_EXPENSE + "/" + dbHelper.insertExpense(values));
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return res;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int res;
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                res = dbHelper.deleteBudget(selectionArgs);
                break;
            case EXPENSE:
                res = dbHelper.deleteExpense(selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return res;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int res;
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                res = dbHelper.updateBudget(values, selectionArgs);
                break;
            case EXPENSE:
                res = dbHelper.updateExpense(values, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return res;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
