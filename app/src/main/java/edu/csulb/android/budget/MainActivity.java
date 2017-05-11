package edu.csulb.android.budget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    int[] images = {
            R.drawable.icn_expense,
            R.drawable.icn_calculator,
            R.drawable.icn_statistic,
            R.drawable.icn_about
    };
    String[] titles = {
            "Expense",
            "Budget",
            "Statistic",
            "About"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

//        Button btnCalculation = (Button)findViewById(R.id.btnCalculation);
//        btnCalculation.setShadowLayer(4,0,0, Color.BLACK);
//        Button btnData = (Button)findViewById(R.id.btnData);
//        btnData.setShadowLayer(4,0,0, Color.BLACK);

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLoginView();
        } else {
//            mUserId = mFirebaseUser.getUid();
//
//            // Set up ListView
//            final ListView listView = (ListView) findViewById(R.id.listView);
//            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//            listView.setAdapter(adapter);
//
//            // Add items via the Button and EditText at the bottom of the view.
//            final EditText text = (EditText) findViewById(R.id.todoText);
//            final Button button = (Button) findViewById(R.id.addButton);
//            button.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    mDatabase.child("users").child(mUserId).child("items").push().child("title").setValue(text.getText().toString());
//                    text.setText("");
//                }
//            });
//
//            // Use Firebase to populate the list.
//            mDatabase.child("users").child(mUserId).child("items").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    adapter.add((String) dataSnapshot.child("title").getValue());
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    adapter.remove((String) dataSnapshot.child("title").getValue());
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

            GridAdapter adapter = new GridAdapter(MainActivity.this, titles, images);
            GridView grid = (GridView)findViewById(R.id.grid_view);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            Intent intentProfile = new Intent(MainActivity.this, BudgetProfile.class);
                            startActivity(intentProfile);
                            break;
                        case 1:
                            Intent intentCalculation = new Intent(MainActivity.this, CalculationActivity.class);
                            startActivity(intentCalculation);
                            break;
                        case 2:
                            Intent intentData = new Intent(MainActivity.this, DataListingActivity.class);
                            startActivity(intentData);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            loadLoginView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
