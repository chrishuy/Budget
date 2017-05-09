package edu.csulb.android.budget;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Locations extends AppCompatActivity {

    private EditText eAddress;
    Button bLocate;
    private Intent resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);

        eAddress = (EditText) findViewById(R.id.etAddress);


    }
}
