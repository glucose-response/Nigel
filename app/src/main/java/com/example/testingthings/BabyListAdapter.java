package com.example.testingthings;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
public class BabyListAdapter extends RecyclerView.Adapter<BabyListAdapter.ViewHolder>{

    private List<Baby> originalList;
    private List<Baby> filteredList; // New list to store filtered results
    private AxisConfiguration commonAxisConfig;

    public BabyListAdapter(List<Baby> babyList) {
        this.originalList = babyList;
        this.filteredList = this.originalList;
        this.commonAxisConfig = new AxisConfiguration(0,10,0,1);
        if (babyList != null) {
            this.filteredList = new ArrayList<>(babyList); // Initialize filtered list with all items
        } else {
            this.filteredList = new ArrayList<>(); // Initialize an empty list if bebeList is null
        }    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTextView;
        public LineChart personChart;


        public ViewHolder(View itemView) {
            super(itemView);
            personNameTextView = itemView.findViewById(R.id.nameTextView);
            personChart = itemView.findViewById(R.id.personChart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Baby baby = filteredList.get(position);

        holder.personNameTextView.setText(String.valueOf(baby.getId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedActivity.class);
                intent.putExtra("BEBE_KEY", baby.getId());
                v.getContext().startActivity(intent);
            }
        });

        // Customize this method based on your data and chart setup
        setupPersonChart(holder.personChart,
                baby.getTimeSeriesData(),
                commonAxisConfig);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private void setupPersonChart(LineChart chart,
                                  List<Entry> timeSeriesData,
                                  AxisConfiguration axisConfig) {
        // Customize this method based on how you want to display the chart
        // ...

        // Example setup:
        LineDataSet dataSet = new LineDataSet(timeSeriesData, "Person Data");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        // Set common axis configuration
        chart.getAxisLeft().setAxisMinimum(axisConfig.getMinY());
        chart.getAxisLeft().setAxisMaximum(axisConfig.getMaxY());

        chart.getXAxis().setAxisMinimum(axisConfig.getMinX());
        chart.getXAxis().setAxisMaximum(axisConfig.getMaxX());

        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh
    }

    public void filterByName(String query) {
        Log.d("FILTER", "filterByName: " + query);
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList); // If query is empty, show all babies
        } else {
            for (Baby baby : originalList) {
                Log.d("FILTER", "filterByName: " + baby.getId());
                if (String.valueOf(baby.getId()).toLowerCase().contains(query.toLowerCase())) {
                    Log.d("FILTER", "baby added: " + baby.getId());
                    filteredList.add(baby);
                }
            }
        }

        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public List<Baby> getFilteredList() {
        return filteredList;
    }

    public List<Baby> getOriginalList() {
        return originalList;
    }

}
