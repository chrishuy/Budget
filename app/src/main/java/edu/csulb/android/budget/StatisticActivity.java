package edu.csulb.android.budget;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        MyData[] dataObjects = {
                new MyData(1, 2),
                new MyData(4, 5),
                new MyData(1, 2),
                new MyData(9, 4),
                new MyData(10, 7),
                new MyData(8, 3)
        };

        List<Entry> entries = new ArrayList<Entry>();

        for (MyData data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new Entry(data.getX(), data.getY()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.CYAN);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }

    private class MyData {
        float x;
        float y;
        MyData(float x, float y) {
            this.x = x;
            this.y = y;
        }
        public float getX() {return x;}
        public float getY() {return y;}
    }
}
