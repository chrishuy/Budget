package edu.csulb.android.budget;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class LocationActivity extends AppCompatActivity {

    private EditText eAddress;
    Button bLocate;
    private Intent resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        eAddress = (EditText) findViewById(R.id.etAddress);

    }
}
