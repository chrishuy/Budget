package edu.csulb.android.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kyo on 5/10/17.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private final int[] images;
    private final String[] titles;

    public GridAdapter(Context context, String[] titles, int[] images) {
        this.context = context;
        this.titles = titles;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_element, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView)convertView.findViewById(R.id.grid_item_image);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.grid_item_label);
            convertView.setTag(viewHolder);
        } else {
            // Recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Update the item view
        viewHolder.ivIcon.setImageResource(images[position]);
        viewHolder.tvTitle.setText(titles[position]);
        return convertView;
    }

    // The view holder design pattern prevents using findViewById()
    // repeatedly in the getView() method of the adapter.
    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }
}
