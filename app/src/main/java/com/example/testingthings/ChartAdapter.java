package com.example.testingthings;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;
public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder> {

    private List<Person> personList;

    public ChartAdapter(List<Person> personList) {
        this.personList = personList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTextView;
        public LineChart personChart;

        public ViewHolder(View itemView) {
            super(itemView);
            personNameTextView = itemView.findViewById(R.id.personNameTextView);
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
        Person person = personList.get(position);

        holder.personNameTextView.setText(person.getName());

        // Customize this method based on your data and chart setup
        setupPersonChart(holder.personChart, person.getTimeSeriesData());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    private void setupPersonChart(LineChart chart, List<Entry> timeSeriesData) {
        // Customize this method based on how you want to display the chart
        // ...

        // Example setup:
        LineDataSet dataSet = new LineDataSet(timeSeriesData, "Person Data");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh
    }
}
