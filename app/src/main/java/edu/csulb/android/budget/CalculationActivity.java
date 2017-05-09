package edu.csulb.android.budget;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences share;
    private Intent existingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        share = getSharedPreferences("AppPref", MODE_PRIVATE);
        float verify = share.getFloat("budget_pref", -1000);
        existingUser = new Intent(getApplicationContext(), BudgetProfile.class);
        if (verify != -1000) {
            startActivity(existingUser);
        }

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
                SharedPreferences.Editor editor = share.edit();
                editor.putFloat("income_pref", income);
                editor.putFloat("savings_pref", saving);
                editor.putFloat("groceries_pref", grocery);
                editor.putFloat("bills_pref", bill);
                editor.putFloat("budget_pref", budget);
                editor.putFloat("remainder_pref", budget);
                editor.apply();

                startActivity(existingUser);
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
}