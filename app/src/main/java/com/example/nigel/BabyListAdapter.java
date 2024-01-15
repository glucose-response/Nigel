package com.example.nigel;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BabyListAdapter extends RecyclerView.Adapter<BabyListAdapter.ViewHolder>{

    private Map<Integer, Baby> originalList = new HashMap<>();
    private Map<Integer, Baby> filteredList = new HashMap<>(); // New list to store filtered results
    private AxisConfiguration commonAxisConfig;

    /**
     * Constructor for the adapter
     * @param babyList The list of babies to display
     */
    public BabyListAdapter(Map<Integer, Baby> babyList) {
        Log.d("BabyListAdapter", "BabyListAdapter: " + babyList.size());
        this.originalList = babyList;
        this.commonAxisConfig = new AxisConfiguration(0,10,0,1);
        this.filteredList = new HashMap<>(babyList); // Initialize filtered list with all items

        // Log.d("BabyListAdapter", "Baby 1: " + babyList.get(1).getId());
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTextView;
        public TextView personBirthdayTextView;
        public CombinedChart personChart;

        public ViewHolder(View itemView) {
            super(itemView);
            personNameTextView = itemView.findViewById(R.id.idTextView);
            personBirthdayTextView = itemView.findViewById(R.id.bebeTextView1);
            personChart = itemView.findViewById(R.id.personChart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        return new ViewHolder(view);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Baby> babyList = mapToList();
        Baby baby = babyList.get(position);
        // Log the name of the person to the TextView
        Log.d("BabyListAdapter", "onBindViewHolder: " +
                baby.getId() + " " +
                baby.getGestationalAge() + " " +
                baby.getBirthDate() + " " +
                baby.getWeight() + " " +
                baby.getNotes());
        holder.personNameTextView.setText(String.valueOf(baby.getId()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.personBirthdayTextView.setText(baby.dateOfBirthToString());
        }

        Log.d("BabyListAdapter", "Setting up person text " + holder.personNameTextView.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedActivity.class);
                intent.putExtra("Nigel ID", baby.getId());
                intent.putExtra("Gestational Age", baby.getGestationalAge());
                intent.putExtra("Date of Birth", baby.dateOfBirthToString());
                intent.putExtra("Weight", baby.getWeight());
                intent.putExtra("Notes", baby.getNotes());
                intent.putExtra("Days of life", baby.getAge());

                // Extract blood glucose entries
                ArrayList<Entry> bloodGlucoseEntries = new ArrayList<>();
                ArrayList<Entry> sweatGlucoseEntries = new ArrayList<>();
                ArrayList<Float> feedingTimes = new ArrayList<>();


                for (DataSample event : baby.getTimeSeriesData()) {
                    if (event instanceof BloodSample) {
                        BloodSample bloodSample = (BloodSample) event;
                        bloodGlucoseEntries.add(new Entry(bloodSample.getTimestamp(), bloodSample.getGlucoseValue()));
                    }
                    else if (event instanceof SweatSample){
                        SweatSample sweatsample = (SweatSample) event;
                        sweatGlucoseEntries.add(new Entry(sweatsample.getTimestamp(), sweatsample.getGlucoseValue()));
                    }
                    else if (event instanceof FeedingDataSample) {
                        FeedingDataSample feedingSample = (FeedingDataSample) event;
                        feedingTimes.add((float) feedingSample.getTimestamp());
                    }
                }


                // Add the extracted entries to the intent
                intent.putExtra("bloodGlucoseEntries", bloodGlucoseEntries);
                intent.putExtra("SweatGlucoseEntries", sweatGlucoseEntries);
                intent.putExtra("feedingTimes", feedingTimes);

                //intent.putExtra("Blood Samples", (Serializable) BloodSampleEntries.get(baby.getId()));

                v.getContext().startActivity(intent);
            }
        });

        // Customize this method based on your data and chart setup
        setupPersonChart(
                holder.personChart,
                baby,
                commonAxisConfig);
    }


    /**
     * Sets up the chart for a person
     *
     * @param combinedChart The chart to set up
     * @param baby          The person to set up the chart for
     * @param axisConfig    The axis configuration to use
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupPersonChart(CombinedChart combinedChart,
                                  Baby baby,
                                  AxisConfiguration axisConfig) {
        // Customize this method based on how you want to display the chart
        Log.d("BabyListAdapter", "Setting up chart " + combinedChart.getId());
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                // Assuming value is in milliseconds
                return dateFormat.format(new Date((long) value));
            }
        });

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
                limitLine.enableDashedLine(10f, 10f, 0f);
                // Set color to semi-transparent blue according to api level
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    limitLine.setLineColor(Color.argb(185, 170, 211, 225));
                } else {
                    limitLine.setLineColor(Color.BLUE);
                }
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



        // Set the axis limits for blood data
        ScatterDataSet bloodDataset = new ScatterDataSet(bloodSampleEntries, "Blood Glucose");
        bloodDataset.setColor(Color.argb(255, 190, 46, 23));
        bloodDataset.setAxisDependency(YAxis.AxisDependency.LEFT);
        ScatterData bloodData = new ScatterData(bloodDataset);

        // Set the axis data for sweat data
        LineDataSet sweatDataset = new LineDataSet(sweatSampleEntries, "Sweat Glucose");
        sweatDataset.setAxisDependency(YAxis.AxisDependency.RIGHT);
        sweatDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        sweatDataset.setCubicIntensity(0.1f);
        sweatDataset.setColor(Color.argb(255, 142, 186, 140));
        sweatDataset.setCircleColor(Color.argb(255, 142, 186, 140));
        sweatDataset.setCircleHoleColor(Color.argb(255, 142, 186, 140));
        LineData sweatData = new LineData(sweatDataset);

        // Clear the previous limit lines
        xAxis.removeAllLimitLines();
        // Now, iterate over the feeding times and create a vertical line for each
        for (LimitLine limitLine : feedingLines) {
            xAxis.addLimitLine(limitLine);
        }

        // Add feeding line label to legend
        Legend legend = combinedChart.getLegend();
        int feedingColor[] = new int[1];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            feedingColor[0] = Color.argb(185, 170, 211, 225);
        } else {
            feedingColor[0] = Color.BLUE;
        }
        String[] feedingLabel = new String[1];
        feedingLabel[0] = "Feeding Time";

        legend.setExtra(feedingColor, feedingLabel);

        // Combine the data
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

    /**
     * Filters the list of babies by name for the main page
     *
     * @param query The query to filter by
     */
    public void filterByName(String query) {
        Log.d("FILTER", "filterByName: " + query);
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.putAll(originalList); // If query is empty, show all babies
        } else {
            for (Baby baby : originalList.values()) {
                Log.d("FILTER", "filterByName: " + baby.getId());
                if (query.toLowerCase().contains(String.valueOf(baby.getId()).toLowerCase())) {
                    Log.d("FILTER", "baby added: " + baby.getId());
                    filteredList.put(baby.getId(), baby);
                }
            }
        }

        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }


    /**
     * Converts the map into a list
     * To be used for the filter
     */
    public List<Baby> mapToList(){
        List<Baby> babyList = new ArrayList<>();
        for (Map.Entry<Integer, Baby> entry : filteredList.entrySet()) {
            babyList.add(entry.getValue());
        }
        return babyList;
    }

    /**
     * Getters
     */
    public Map<Integer, Baby> getFilteredList() {
        return filteredList;
    }
    public Map<Integer, Baby> getOriginalList() {
        return originalList;
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * Sets the original list
     */
    public void setOriginalList(Map<Integer, Baby> newList) {
        Log.d("BabyListAdapter", "setOriginalList: " + newList.size());
        this.originalList = newList;
        this.filteredList = new HashMap<>(newList);
        notifyDataSetChanged();
    }

}
