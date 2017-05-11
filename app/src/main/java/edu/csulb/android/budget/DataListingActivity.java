package edu.csulb.android.budget;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataListingActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_listing);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void initList() {
        final ListAdapter adapter = new ListAdapter(this, items);
        ListView listView = (ListView)findViewById(R.id.lvData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getBaseContext(), "Row : " + Integer.toString(items.get(position).getId()) + " is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = getItem(position);
                adapter.remove(item);
                adapter.notifyDataSetChanged();

                BudgetDeleteTask deleteTask = new BudgetDeleteTask();
                // Deleting the row from SQLite database table
                deleteTask.execute(item.getId());
                return false;
            }
        });
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Uri to the content provider
        Uri uri = DBContentProvider.CONTENT_URI_BUDGET;
        // Fetches all the entries from database
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        float income = 0, saving = 0, grocery = 0, bill = 0, remainder = 0, budget = 0;
        String today = "";
        // Get number of entries available in the database
        int count = data.getCount();
        int id = 0;
        //Move the current record pointer to the first row of the table
        data.moveToFirst();
        //Loop all entries in the database
        for (int i = 0; i < count; i++) {
            id = data.getInt(data.getColumnIndex(DBHelper.CN_ID));
            income = data.getFloat(data.getColumnIndex(DBHelper.CN_INCOME));
            saving = data.getFloat(data.getColumnIndex(DBHelper.CN_SAVING));
            grocery = data.getFloat(data.getColumnIndex(DBHelper.CN_GROCERY));
            bill = data.getFloat(data.getColumnIndex(DBHelper.CN_BILL));
            remainder = data.getFloat(data.getColumnIndex(DBHelper.CN_REMAINDER));
            budget = data.getFloat(data.getColumnIndex(DBHelper.CN_BUDGET));
            today = data.getString(data.getColumnIndex(DBHelper.CN_TODAY));
            items.add(new Item(id, income, saving, grocery, bill, remainder, budget, today));
            data.moveToNext();
        }
        Toast.makeText(getBaseContext(), "Count = " + Integer.toString(count), Toast.LENGTH_LONG).show();
        // List all items in descending order
        Collections.reverse(items);
        initList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class BudgetDeleteTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            // Deleting all the budget row stored in the database
            String[] selectionArgs = new String[]{Integer.toString(params[0])};
            getContentResolver().delete(DBContentProvider.CONTENT_URI_BUDGET, null, selectionArgs);
            return null;
        }
    }
}
