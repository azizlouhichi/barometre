package com.example.barometre;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherItem> weatherItemList;

    public WeatherAdapter(List<WeatherItem> weatherItemList) {
        this.weatherItemList = weatherItemList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherItem currentItem = weatherItemList.get(position);
        holder.label.setText(currentItem.getLabel());
        holder.value.setText(currentItem.getValue());
        holder.icon.setImageResource(currentItem.getIc());
        holder.detail.setText(currentItem.getDetail());
        // Add more logic to set different icons based on the data
    }

    @Override
    public int getItemCount() {
        return weatherItemList.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView label;
        TextView value;
        ImageView icon;

        TextView detail;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            value = itemView.findViewById(R.id.value);
            icon = itemView.findViewById(R.id.icon);
            detail = itemView.findViewById(R.id.detail);
        }
    }
}