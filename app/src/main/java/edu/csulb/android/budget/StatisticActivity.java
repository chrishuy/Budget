package edu.csulb.android.budget;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>  {
    List<BarEntry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DBContentProvider.CONTENT_URI_EXPENSE;
        // Fetches all the entries from database
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) return;
        // Get the last entry
        float spent;
        // Get number of entries available in the database
        int count = data.getCount();
        //Move the current record pointer to the first row of the table
        data.moveToFirst();
        for (int i = 0; i < count; i++) {
            spent = data.getInt(data.getColumnIndex(DBHelper.CN_SPENT));
            entries.add(new BarEntry(i, spent));
            data.moveToNext();
        }
        plot();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void plot() {
        BarChart barChart = (BarChart)findViewById(R.id.chart);
        BarDataSet barDataSet = new BarDataSet(entries, "Spent Money");
        barDataSet.setValueTextColor(Color.RED);
        barDataSet.setValueTextSize(13f);
        BarData barData = new BarData(barDataSet);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setData(barData);
        barChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
        barChart.invalidate();
    }
}
