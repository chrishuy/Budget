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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private float maxBudget, spent;
    private ProgressBar pBudget;
    private EditText eSpend;
    private TextView tProgress;
    private int currentRecord;

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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String today = dateFormat.format(new Date());
                float amount = Float.parseFloat(eSpend.getText().toString());
                spent -= amount;
                // Update the last row only with the new spent value
                ContentValues cvBudget = new ContentValues();
                cvBudget.put(DBHelper.CN_REMAINDER, spent);
                ContentValues cvExpense = new ContentValues();
                cvExpense.put(DBHelper.CN_SPENT, amount);
                cvExpense.put(DBHelper.CN_TODAY, today);
                BudgetUpdateTask updateTask = new BudgetUpdateTask(currentRecord);
                updateTask.execute(cvBudget, cvExpense);
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
        currentRecord = data.getInt(data.getColumnIndex(DBHelper.CN_ID));
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
            // Add value to budgetId column (foreign key)
            params[1].put(DBHelper.CN_BUDGET_ID, index);
            getContentResolver().insert(DBContentProvider.CONTENT_URI_EXPENSE, params[1]);
            return null;
        }
    }
}
