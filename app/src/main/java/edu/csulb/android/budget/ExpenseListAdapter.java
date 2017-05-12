package edu.csulb.android.budget;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kyo on 5/12/17.
 */

public class ExpenseListAdapter extends ArrayAdapter<Expense> {
    private final Activity context;
    private List<Expense> expenses;
    private int[] colors = new int[] { Color.parseColor("#F0F0F0"), Color.parseColor("#D2E4FC") };

    public ExpenseListAdapter(Activity context, List<Expense> expenses) {
        super(context, R.layout.list_expense_element, expenses);
        this.context = context;
        this.expenses = expenses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_expense_element, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
            viewHolder.tvSpent = (TextView)convertView.findViewById(R.id.tvSpent);
            viewHolder.tvItem = (TextView)convertView.findViewById(R.id.tvItem);
            convertView.setBackgroundColor(position % 2 == 0 ? colors[0] : colors[1]);
            convertView.setTag(viewHolder);
        } else {
            // Recycle the already inflated view
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // Update the item view
        DecimalFormat df = new DecimalFormat("###.##");
        viewHolder.tvDate.setText(expenses.get(position).getToday());
        float spent = expenses.get(position).getSpent();
        String item = expenses.get(position).getItem();
        viewHolder.tvSpent.setText(df.format(spent));
        viewHolder.tvItem.setText(item);
        return convertView;
    }

    private static class ViewHolder {
        TextView tvDate;
        TextView tvSpent;
        TextView tvItem;
    }
}
