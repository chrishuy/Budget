package edu.csulb.android.budget;

import android.content.ContentValues;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private float maxBudget, spent;
    private ProgressBar pBudget;
    private EditText eSpend;
    private TextView tProgress;
    private BudgetUpdateTask updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportLoaderManager().initLoader(0, null, this);

        Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setShadowLayer(4,0,0, Color.BLACK);

        eSpend = (EditText) findViewById(R.id.edtSpent);
        tProgress = (TextView) findViewById(R.id.tvProgress);
        pBudget = (ProgressBar) findViewById(R.id.pbProgress);
        pBudget.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
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
        }
    }

    private void updateProgressBar() {
        pBudget.setProgress((int)((spent/maxBudget) * 100));
        tProgress.setText(spent + "/" + maxBudget);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DBContentProvider.CONTENT_URI_BUDGET;
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
            getContentResolver().update(DBContentProvider.CONTENT_URI_BUDGET, params[0], null, selectionArgs);
            return null;
        }
    }
}
