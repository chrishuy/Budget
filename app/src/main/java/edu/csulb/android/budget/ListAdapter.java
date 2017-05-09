package edu.csulb.android.budget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kyo on 5/5/17.
 */

public class ListAdapter extends ArrayAdapter<Item> {
    private final Activity context;
    private List<Item> items;

    public ListAdapter(Activity context, List<Item> items) {
        super(context, R.layout.list_element, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_element, null, true);

        DecimalFormat df = new DecimalFormat("###.##");
        TextView txtDate = (TextView) rowView.findViewById(R.id.txtDate);
        txtDate.setText(items.get(position).getToday());
        TextView txtIncome = (TextView) rowView.findViewById(R.id.txtIncome);
        txtIncome.setText(df.format(items.get(position).getIncome()));
        TextView txtSaving = (TextView) rowView.findViewById(R.id.txtSaving);
        txtSaving.setText(df.format(items.get(position).getSaving()));
        TextView txtGrocery = (TextView) rowView.findViewById(R.id.txtGrocery);
        txtGrocery.setText(df.format(items.get(position).getGrocery()));
        TextView txtBill = (TextView) rowView.findViewById(R.id.txtBill);
        txtBill.setText(df.format(items.get(position).getBill()));
        TextView txtBudget = (TextView) rowView.findViewById(R.id.txtBudget);
        txtBudget.setText(df.format(items.get(position).getBudget()));
        return rowView;
    }
}

