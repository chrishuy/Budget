package edu.csulb.android.budget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText eIncome, eSavings, eGrocery, eBills;
    private TextView tBudget;
    SharedPreferences share;
    private Intent existingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        share = getSharedPreferences("AppPref", MODE_PRIVATE);
        float verify = share.getFloat("budget_pref", -1000);
        existingUser = new Intent(getApplicationContext(), BudgetProfile.class);
        if(verify != -1000)
        {
            startActivity(existingUser);
        }

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
                float cIncome = Float.parseFloat(eIncome.getText().toString());
                float cSavings = Float.parseFloat(eSavings.getText().toString());
                float cGrocery = Float.parseFloat(eGrocery.getText().toString());
                float cBills = Float.parseFloat(eBills.getText().toString());
                float cBudget = WeeklyCalculator.weekBudget(cIncome, cBills, cGrocery, cSavings);

                //Set text view to display result
                SharedPreferences.Editor editor = share.edit();
                editor.putFloat("income_pref", cIncome);
                editor.putFloat("savings_pref", cSavings);
                editor.putFloat("groceries_pref", cGrocery);
                editor.putFloat("bills_pref", cBills);
                editor.putFloat("budget_pref", cBudget);
                editor.putFloat("remainder_pref", cBudget);
                editor.commit();

                startActivity(existingUser);
        }
    }
}
