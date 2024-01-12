package com.example.nigel;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nigel.dataclasses.BloodSample;
import com.example.nigel.dataclasses.DataSample;
import com.example.nigel.dataclasses.FeedingDataSample;
import com.example.nigel.dataclasses.SweatSample;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BabyListAdapter extends RecyclerView.Adapter<BabyListAdapter.ViewHolder>{

    private Map<Integer, Baby> originalList = new HashMap<>();
    private Map<Integer, Baby> filteredList = new HashMap<>(); // New list to store filtered results
    private AxisConfiguration commonAxisConfig;

    public BabyListAdapter(Map<Integer, Baby> babyList) {
        Log.d("BabyListAdapter", "BabyListAdapter: " + babyList.size());
        this.originalList = babyList;
        this.filteredList = this.originalList;
        this.commonAxisConfig = new AxisConfiguration(0,10,0,1);
        this.filteredList = new HashMap<>(babyList); // Initialize filtered list with all items

        Log.d("BabyListAdapter", "Baby 1: " + babyList.get(1).getId());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTextView;
        public CombinedChart personChart;

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
        Baby baby = filteredList.get(position+1);
        // Log the name of the person to the TextView
        Log.d("BabyListAdapter", "onBindViewHolder: " +
                baby.getId() + " " +
                baby.getGroup() + " " +
                baby.getBirthDate() + " " +
                baby.getWeight() + " " +
                baby.getNotes() + " " +
                baby.getTimeSeriesData().size() + " " +
                baby.getTimeSeriesData().get(0).getTimestamp());
        holder.personNameTextView.setText(String.valueOf(baby.getId()));
        Log.d("BabyListAdapter", "Setting up person text " + holder.personNameTextView.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedActivity.class);
                intent.putExtra("Nigel ID", baby.getId());
                v.getContext().startActivity(intent);
            }
        });

        // Customize this method based on your data and chart setup
        setupPersonChart(
                holder.personChart,
                baby,
                commonAxisConfig);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private void setupPersonChart(CombinedChart combinedChart,
                                  Baby baby,
                                  AxisConfiguration axisConfig) {
        // Customize this method based on how you want to display the chart
        Log.d("BabyListAdapter", "Setting up chart " + combinedChart.getId());
        XAxis xAxis = combinedChart.getXAxis();
        // Categorize the events
        ArrayList<Entry> bloodSampleEntries = new ArrayList<>();
        ArrayList<Entry> sweatSampleEntries = new ArrayList<>();
        ArrayList<LimitLine> feedingLines = new ArrayList<>();

        // Log size of baby's timeSeries data
        Log.d("BabyListAdapter", "Baby " + baby.getId() + " timeSeriesData size: " + baby.getTimeSeriesData().size());
        for (DataSample timeSeriesEvent : baby.getTimeSeriesData()) {
            if (timeSeriesEvent instanceof BloodSample) {
                bloodSampleEntries.add(new Entry(timeSeriesEvent.getTimestamp(), ((BloodSample) timeSeriesEvent).getGlucoseValue()));
                Log.d("BabyListAdapter", "BloodSample event at " + timeSeriesEvent.getTimestamp());
            } else if (timeSeriesEvent instanceof SweatSample) {
                sweatSampleEntries.add(new Entry(timeSeriesEvent.getTimestamp(), ((SweatSample) timeSeriesEvent).getGlucoseValue()));
                Log.d("BabyListAdapter", "SweatSample event at " + timeSeriesEvent.getTimestamp());
            } else if (timeSeriesEvent instanceof FeedingDataSample) {
                LimitLine limitLine = new LimitLine(timeSeriesEvent.getTimestamp());
                limitLine.setLineWidth(1f); // Set the width of the vertical line
                // Set color to semi-transparent blue according to api level
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    limitLine.setLineColor(Color.argb(255, 0, 0, 255));
                } else {
                    limitLine.setLineColor(Color.BLUE);
                }
                // limitLine.enableDashedLine(10f, 10f, 0f);
                feedingLines.add(limitLine);
                Log.d("BabyListAdapter", "Feeding event at " + timeSeriesEvent.getTimestamp());


            } else {
                Log.d("MainActivity", "Unknown event type");
            }
        }

        // Set the axis limits
        // Log SweatSampleEntries size
        Log.d("BabyListAdapter", "SweatSampleEntries size: " + sweatSampleEntries.size() +
                " BloodSampleEntries size: " + bloodSampleEntries.size());

        /*LineData sweatData = new LineData(sweatDataset);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(sweatData);

        combinedChart.setData(combinedData);

        combinedChart.getAxisLeft().setAxisMinimum(axisConfig.getMinY());
        combinedChart.getAxisLeft().setAxisMaximum(axisConfig.getMaxY());

        combinedChart.getXAxis().setAxisMinimum(axisConfig.getMinX());
        combinedChart.getXAxis().setAxisMaximum(axisConfig.getMaxX());

        combinedChart.invalidate();*/



        ScatterDataSet bloodDataset = new ScatterDataSet(bloodSampleEntries, "Blood Glucose");
        bloodDataset.setColor(Color.argb(255, 255, 0, 0));
        bloodDataset.setAxisDependency(YAxis.AxisDependency.LEFT);
        ScatterData bloodData = new ScatterData(bloodDataset);


        LineDataSet sweatDataset = new LineDataSet(sweatSampleEntries, "Sweat Glucose");
        sweatDataset.setAxisDependency(YAxis.AxisDependency.RIGHT);
        sweatDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        sweatDataset.setCubicIntensity(0.1f);
        sweatDataset.setColor(Color.argb(255, 0, 255, 0));
        sweatDataset.setCircleColor(Color.argb(255, 0, 255, 0));
        sweatDataset.setCircleHoleColor(Color.argb(255, 0, 255, 0));
        LineData sweatData = new LineData(sweatDataset);

        // Clear the previous limit lines
        xAxis.removeAllLimitLines();
        // Now, iterate over the feeding times and create a vertical line for each
        for (LimitLine limitLine : feedingLines) {
            xAxis.addLimitLine(limitLine);
        }

        CombinedData combinedData = new CombinedData();
        combinedData.setData(bloodData);
        combinedData.setData(sweatData);

        combinedChart.setData(combinedData);

        // Removed Gridlines
        combinedChart.getAxisLeft().setDrawGridLines(false);
        combinedChart.getAxisRight().setDrawGridLines(false);
        combinedChart.getXAxis().setDrawGridLines(false);

        // Refresh the chart to show changes
        combinedChart.invalidate();
    }

    public void filterByName(String query) {
        Log.d("FILTER", "filterByName: " + query);
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.putAll(originalList); // If query is empty, show all babies
        } else {
            for (Baby baby : originalList.values()) {
                Log.d("FILTER", "filterByName: " + baby.getId());
                if (String.valueOf(baby.getId()).toLowerCase().contains(query.toLowerCase())) {
                    Log.d("FILTER", "baby added: " + baby.getId());
                    filteredList.put(baby.getId(), baby);
                }
            }
        }

        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public Map<Integer, Baby> getFilteredList() {
        return filteredList;
    }

    public Map<Integer, Baby> getOriginalList() {
        return originalList;
    }

}
