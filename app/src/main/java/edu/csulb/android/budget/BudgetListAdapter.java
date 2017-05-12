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

public class BudgetListAdapter extends ArrayAdapter<Item> {
    private final Activity context;
    private List<Item> items;
//    private int[] colors = new int[] { Color.parseColor("#F0F0F0"), Color.parseColor("#D2E4FC") };

    public BudgetListAdapter(Activity context, List<Item> items) {
        super(context, R.layout.list_budget_element, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_budget_element, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
            viewHolder.tvExpense = (TextView)convertView.findViewById(R.id.tvExpense);
            viewHolder.tvBudget = (TextView)convertView.findViewById(R.id.tvBudget);
            viewHolder.tvRemainder = (TextView)convertView.findViewById(R.id.tvRemainder);
//            convertView.setBackgroundColor(position % 2 == 0 ? colors[0] : colors[1]);
            convertView.setTag(viewHolder);
        } else {
            // Recycle the already inflated view
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // Update the item view
        DecimalFormat df = new DecimalFormat("###.##");
        viewHolder.tvDate.setText(items.get(position).getToday());
        float remainder = items.get(position).getRemainder();
        float budget = items.get(position).getBudget();
        float spent = budget - items.get(position).getRemainder();
        viewHolder.tvExpense.setText(df.format(spent));
        viewHolder.tvBudget.setText(df.format(budget));
        viewHolder.tvRemainder.setText(df.format(remainder));
        return convertView;
    }

    private static class ViewHolder {
        TextView tvDate;
        TextView tvExpense;
        TextView tvRemainder;
        TextView tvBudget;
    }
}

