package com.example.testingthings;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.List;
public class BebeListAdapter extends RecyclerView.Adapter<BebeListAdapter.ViewHolder>{

    private List<Bebe> bebeList;
    private AxisConfiguration commonAxisConfig;

    public BebeListAdapter(List<Bebe> bebeList) {
        this.bebeList = bebeList;
        this.commonAxisConfig = new AxisConfiguration(0,10,0,1);
    }

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
        Bebe bebe = bebeList.get(position);

        holder.personNameTextView.setText(bebe.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BebeDetailActivity.class);
                intent.putExtra("BEBE_KEY", bebe.getId());
                v.getContext().startActivity(intent);
            }
        });

        // Customize this method based on your data and chart setup
        setupPersonChart(holder.personChart,
                bebe.getTimeSeriesData(),
                commonAxisConfig);
    }

    @Override
    public int getItemCount() {
        return bebeList.size();
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

}
