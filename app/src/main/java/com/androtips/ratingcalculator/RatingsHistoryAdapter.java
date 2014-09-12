package com.androtips.ratingcalculator;

import java.text.DecimalFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RatingsHistoryAdapter extends BaseAdapter {

    private List<RatingRecord> items;
    private final Context context;

    DecimalFormat df = new DecimalFormat("0.00000000");

    public RatingsHistoryAdapter(Context context, List<RatingRecord> items) {
        this.context = context;
        this.items = items;
    }

    public void setItems(List<RatingRecord> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RatingRecord getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_card, null);

            holder = new ViewHolder();
            holder.average = (TextView) convertView.findViewById(R.id.average_rating);
            holder.details = (TextView) convertView.findViewById(R.id.rating_details);
            holder.date = (TextView) convertView.findViewById(R.id.record_date);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.average.setText(df.format(getItem(position).getAverage()));
        holder.details.setText(
                "★★★★★ " + getItem(position).getFiveStars() + "\n" +
                        "★★★★☆ " + getItem(position).getFourStars() + "\n" +
                        "★★★☆☆ " + getItem(position).getThreeStars() + "\n" +
                        "★★☆☆☆ " + getItem(position).getTwoStars() + "\n" +
                        "★☆☆☆☆ " + getItem(position).getOneStar() + "\n");

        holder.date.setText(getItem(position).getDate());

        return convertView;
    }

    private static class ViewHolder {
        private TextView average;
        private TextView details;
        private TextView date;
    }

}
