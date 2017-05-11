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
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/budget");
    // Constant to identify the requested operation
    private static final int BUDGET = 1;
    // UriMatcher to check for corresponding URI
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "budget", BUDGET);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return dbHelper.getAllBudget();
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return Uri.parse(CONTENT_URI + "/" + dbHelper.insertBudget(values));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return dbHelper.deleteBudget(selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case BUDGET:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return dbHelper.updateBudget(values, selectionArgs);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
