package edu.csulb.android.budget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BudgetProfile extends AppCompatActivity {
    private ProgressBar pBudget;
    private int progressStatus;
    private float max, spent;
    private EditText eSpend;
    private TextView tProgress;
    SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_profile);
        pBudget = (ProgressBar) findViewById(R.id.progressBar);
        share = getSharedPreferences("AppPref", MODE_PRIVATE);
        max = share.getFloat("budget_pref", -1000);
        spent = share.getFloat("remainder_pref", -1000);
        eSpend = (EditText) findViewById(R.id.eSpent);
        tProgress = (TextView) findViewById(R.id.tProgress);

        progressStatus = (int)((spent/max) * 100);
        pBudget.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        tProgress.setText(spent + "/" + max);
        pBudget.setProgress(progressStatus);
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bSubmit:
                if(eSpend.getText().length() == 0 || Float.parseFloat(eSpend.getText().toString()) > spent)
                {
                    Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_LONG).show();
                }
                else
                {
                    spent = spent - Float.parseFloat(eSpend.getText().toString());
                    SharedPreferences.Editor editor = share.edit();
                    editor.putFloat("remainder_pref", spent);
                    editor.commit();
                    progressStatus = (int)((spent/max) * 100);
                    pBudget.setProgress(progressStatus);
                    //pBudget.incrementProgressBy(-(int)(Float.parseFloat(eSpend.getText().toString())/max));
                    tProgress.setText(spent + "/" + max);
                    eSpend.setText("");
                }
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
}
