package edu.csulb.android.budget;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private List<Item> items = new ArrayList<>();
    private Map<Integer, List<Expense>> expenses = new HashMap<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportLoaderManager().initLoader(0, null, this);
        getSupportLoaderManager().initLoader(1, null, this);
        listView = (ListView)findViewById(R.id.lvData);
    }

    private void initList() {
        final BudgetListAdapter adapterBudget = new BudgetListAdapter(this, items);
        listView.setAdapter(adapterBudget);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
              if (expenses.isEmpty()) return;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.list_expense, parent, false);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Expense History");
                Integer budgetId = items.get(position).getId();
                ListView lv = (ListView)convertView.findViewById(R.id.lvExpense);
                final ExpenseListAdapter adapterExpense = new ExpenseListAdapter(ReportActivity.this, expenses.get(budgetId));
                lv.setAdapter(adapterExpense);
                alertDialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setMessage("Do you want to delete this record?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Item item = getItem(position);
                                adapterBudget.remove(item);
                                adapterBudget.notifyDataSetChanged();
                                BudgetDeleteTask deleteTask = new BudgetDeleteTask();
                                // Deleting the row from SQLite database table
                                deleteTask.execute(item.getId());
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Warning!");
                alert.show();
                return false;
            }
        });
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Get different Uri to the content provider
        Loader<Cursor> cursor;
        if (id == 0) {
            Uri uri = DBContentProvider.CONTENT_URI_BUDGET;
            cursor = new CursorLoader(this, uri, null, null, null, null);
        } else {
            Uri uri = DBContentProvider.CONTENT_URI_EXPENSE;
            cursor = new CursorLoader(this, uri, null, null, null, null);
        }
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 0) {
            float remainder, budget;
            String today;
            int id;
            // Get number of entries available in the database
            int count = data.getCount();
            //Move the current record pointer to the first row of the table
            data.moveToFirst();
            //Loop all entries in the database
            for (int i = 0; i < count; i++) {
                id = data.getInt(data.getColumnIndex(DBHelper.CN_ID));
                remainder = data.getFloat(data.getColumnIndex(DBHelper.CN_REMAINDER));
                budget = data.getFloat(data.getColumnIndex(DBHelper.CN_BUDGET));
                today = data.getString(data.getColumnIndex(DBHelper.CN_TODAY));
                items.add(new Item(id, remainder, budget, today));
                data.moveToNext();
            }
            // List all items in descending order
            Collections.reverse(items);
        } else {
            float spent;
            String item, today;
            int id, budgetId;
            int count = data.getCount();
            //Move the current record pointer to the first row of the table
            data.moveToFirst();
            //Loop all entries in the database
            for (int i = 0; i < count; i++) {
                budgetId = data.getInt(data.getColumnIndex(DBHelper.CN_BUDGET_ID));
                spent = data.getFloat(data.getColumnIndex(DBHelper.CN_SPENT));
                item = data.getString(data.getColumnIndex(DBHelper.CN_ITEM));
                today = data.getString(data.getColumnIndex(DBHelper.CN_TODAY));
                id = data.getInt(data.getColumnIndex(DBHelper.CN_ID));
                Expense expense = new Expense(id, spent, item, today);
                if (expenses.containsKey(budgetId)) {
                    expenses.get(budgetId).add(expense);
                } else {
                    List<Expense> list = new ArrayList<>();
                    list.add(expense);
                    expenses.put(budgetId, list);
                }
                data.moveToNext();
            }
        }
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
            getContentResolver().delete(DBContentProvider.CONTENT_URI_EXPENSE, null, selectionArgs);
            getContentResolver().delete(DBContentProvider.CONTENT_URI_BUDGET, null, selectionArgs);
            return null;
        }
    }
}
