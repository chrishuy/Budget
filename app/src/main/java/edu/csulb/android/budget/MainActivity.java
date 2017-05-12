package edu.csulb.android.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    int[] images = {
            R.drawable.icn_expense,
            R.drawable.icn_calculator,
            R.drawable.icn_report,
            R.drawable.icn_statistic,
            R.drawable.icn_map,
            R.drawable.icn_profile
    };
    String[] titles = {
            "Expense",
            "Budget",
            "Report",
            "Statistic",
            "Map",
            "Profile"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLoginView();
            return;
        }
        GridAdapter adapter = new GridAdapter(MainActivity.this, titles, images);
        GridView grid = (GridView) findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentProfile = new Intent(MainActivity.this, ExpenseActivity.class);
                        startActivity(intentProfile);
                        break;
                    case 1:
                        Intent intentCalculation = new Intent(MainActivity.this, CalculationActivity.class);
                        startActivity(intentCalculation);
                        break;
                    case 2:
                        Intent intentData = new Intent(MainActivity.this, ReportActivity.class);
                        startActivity(intentData);
                        break;
                    case 3:
                        Intent intentStat = new Intent(MainActivity.this, StatisticActivity.class);
                        startActivity(intentStat);
                        break;
                    case 4:
                        Intent intentLocator = new Intent(MainActivity.this, LocationActivity.class);
                        startActivity(intentLocator);
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }
            }
        });
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
