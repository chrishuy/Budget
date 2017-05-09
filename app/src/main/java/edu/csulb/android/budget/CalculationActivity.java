package edu.csulb.android.budget;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculationActivity extends Activity {
    private EditText eIncome, eSavings, eGrocery, eBills;
    private TextView tBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        eIncome = (EditText) findViewById(R.id.eMonthly);
        eSavings = (EditText) findViewById(R.id.eSaving);
        eGrocery = (EditText) findViewById(R.id.eGrocery);
        eBills = (EditText) findViewById(R.id.eBills);
        tBudget = (TextView) findViewById(R.id.tResults);
    }

    //When user clicks button
    public void onClickCalculate(View view) {
        float income, saving, grocery, bill, budget;
        //Outputs a toast message if any fields are empty
        if((eIncome.getText().length() == 0) || (eSavings.getText().length() == 0)
                || (eGrocery.getText().length() == 0) || (eBills.getText().length() == 0)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show();
            return;
        }
        switch (view.getId()) {
            case R.id.bCalculate:
                //Take input in from edit texts
                income = Float.parseFloat(eIncome.getText().toString());
                saving = Float.parseFloat(eSavings.getText().toString());
                grocery = Float.parseFloat(eGrocery.getText().toString());
                bill = Float.parseFloat(eBills.getText().toString());
                budget = WeeklyCalculator.weekBudget(income, bill, grocery, saving);
                //Set text view to display result
//                tBudget.setText("Monthly Budget: " + Float.toString());
                Toast.makeText(this, Float.toString(budget), Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSave:
                income = Float.parseFloat(eIncome.getText().toString());
                saving = Float.parseFloat(eSavings.getText().toString());
                grocery = Float.parseFloat(eGrocery.getText().toString());
                bill = Float.parseFloat(eBills.getText().toString());
                budget = WeeklyCalculator.weekBudget(income, bill, grocery, saving);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String today = dateFormat.format(new Date());
//                today.replace(' ', '_');

                // Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.CN_INCOME, income);
                contentValues.put(DBHelper.CN_SAVING, saving);
                contentValues.put(DBHelper.CN_GROCERY, grocery);
                contentValues.put(DBHelper.CN_BILL, bill);
                contentValues.put(DBHelper.CN_BUDGET, budget);
                contentValues.put(DBHelper.CN_TODAY, today);

                // Creating an instance of LocationInsertTask
                BudgetInsertTask insertTask = new BudgetInsertTask();
                insertTask.execute(contentValues);
                Toast.makeText(getBaseContext(), today + ": New data is added", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class BudgetInsertTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... params) {
            // Setting up values of the clicked location to insert into the database
            getContentResolver().insert(DBContentProvider.CONTENT_URI, params[0]);
            return null;
        }
    }

    private class BudgetDeleteTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... params) {
            // Deleting all the locations stored in the database
            getContentResolver().delete(DBContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }
}
