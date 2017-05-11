package edu.csulb.android.budget;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BudgetProfile extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private ProgressBar pBudget;
    private float maxBudget, spent;
    private EditText eSpend;
    private TextView tProgress;
    private BudgetUpdateTask updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_profile);
        getSupportLoaderManager().initLoader(0, null, this);

        eSpend = (EditText) findViewById(R.id.eSpent);
        tProgress = (TextView) findViewById(R.id.tProgress);
        pBudget = (ProgressBar) findViewById(R.id.progressBar);
        pBudget.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSubmit:
                if (eSpend.getText().length() == 0 || Float.parseFloat(eSpend.getText().toString()) > spent) {
                    Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_LONG).show();
                    return;
                }
                spent -= Float.parseFloat(eSpend.getText().toString());
                // Update the last row only with the new spent value
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.CN_REMAINDER, spent);
                updateTask.execute(contentValues);
                updateProgressBar();
                eSpend.setText("");
                break;
            case R.id.bSettings:
                Intent reset = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(reset);
                Log.d("Settings", "Settings button pressed");
                break;
            case R.id.bLocations:
                Intent locator = new Intent(getApplicationContext(), Locations.class);
                startActivity(locator);
                Log.d("Locations", "Locations button pressed");
                break;
        }
    }

    private void updateProgressBar() {
        pBudget.setProgress((int)((spent/maxBudget) * 100));
        tProgress.setText(spent + "/" + maxBudget);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DBContentProvider.CONTENT_URI;
        // Fetches all the entries from database
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) return;
        // Get the last entry
        data.moveToLast();
        spent = data.getFloat(data.getColumnIndex(DBHelper.CN_REMAINDER));
        maxBudget = data.getFloat(data.getColumnIndex(DBHelper.CN_BUDGET));
        int id = data.getInt(data.getColumnIndex(DBHelper.CN_ID));
        updateTask = new BudgetUpdateTask(id);
        updateProgressBar();
        Toast.makeText(this, Float.toString(spent), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class BudgetUpdateTask extends AsyncTask<ContentValues, Void, Void> {
        private int index;

        BudgetUpdateTask(int index) {
            this.index = index;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            // Setting up values of the clicked location to insert into the database
            String[] selectionArgs = new String[]{Integer.toString(index)};
            getContentResolver().update(DBContentProvider.CONTENT_URI, params[0], null, selectionArgs);
            return null;
        }
    }
}
