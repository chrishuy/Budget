package edu.csulb.android.budget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText eIncome, eSavings, eGrocery, eBills;
    private TextView tBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eIncome = (EditText) findViewById(R.id.eMonthly);
        eSavings = (EditText) findViewById(R.id.eSaving);
        eGrocery = (EditText) findViewById(R.id.eGrocery);
        eBills = (EditText) findViewById(R.id.eBills);
        tBudget = (TextView) findViewById(R.id.tResults);
    }

    //When user clicks button
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bCalculate:
                //Outputs a toast message if any fields are empty
                if((eIncome.getText().length() == 0) || (eSavings.getText().length() == 0)
                        || (eGrocery.getText().length() == 0) || (eBills.getText().length() == 0))
                {
                    Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                //Take input in from edit texts
                float income = Float.parseFloat(eIncome.getText().toString());
                float savings = Float.parseFloat(eSavings.getText().toString());
                float grocery = Float.parseFloat(eGrocery.getText().toString());
                float bills = Float.parseFloat(eBills.getText().toString());

                //Set text view to display result
                tBudget.setText("Monthly Budget: " + Float.toString(WeeklyCalculator.weekBudget(income, bills, grocery, savings)));

        }
    }
}
