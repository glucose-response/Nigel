package com.example.nigel;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {
    private LineChart glucoseChart;
    // Helper method to format the date
    private String formatDate(long timestamp) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Convert the timestamp to a Date object and format it.
        return formatter.format(new Date(timestamp));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // setup text views
        TextView textView = findViewById(R.id.nameTextView);
        TextView dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView);
        TextView gestationalAgeTextView = findViewById(R.id.gestationalAgeTextView);
        TextView birthWeightTextView = findViewById(R.id.birthWeightTextView);
        TextView futureNotesEditText = findViewById(R.id.futureNotesEditText);

        /*
        // Get whole baby object from BabyListAdapter
        Baby baby = (Baby) getIntent().getSerializableExtra("Baby object");
        // int bebeInt = (int) getIntent().getSerializableExtra("BEBE_KEY");
        int bebeInt = baby.getId();
        int gestationalAge = baby.getGestationalAge();
        double weight = baby.getWeight();
        long birthdate = getIntent().getLongExtra("Date of Birth", -1);

         */
        //Baby baby = (Baby) getIntent().getSerializableExtra("Baby object");
        int bebeInt = (int) getIntent().getSerializableExtra("Nigel ID");
        double gestationalAge = (double) getIntent().getSerializableExtra("Gestational Age");
        String birthdate = (String) getIntent().getSerializableExtra("Date of Birth");
        double weight = (double) getIntent().getSerializableExtra("Weight");
        String notes = (String) getIntent().getSerializableExtra("Notes");

        // Retrieve the Blood, Sweat and Feeding entries
        ArrayList<Entry> bloodGlucoseEntries = (ArrayList<Entry>) getIntent().getSerializableExtra("bloodGlucoseEntries");
        ArrayList<Entry> sweatGlucoseEntries = (ArrayList<Entry>) getIntent().getSerializableExtra("SweatGlucoseEntries");
        ArrayList<Long> feedingTimes = (ArrayList<Long>) getIntent().getSerializableExtra("FeedingTimes");

        // Populate text views
        textView.setText("Person " + String.valueOf(bebeInt) + " Detail Activity");
        gestationalAgeTextView.setText("Gestational Age: " + String.valueOf(gestationalAge) + " weeks");
        birthWeightTextView.setText("Birth Weight: " + String.valueOf(weight) + " kg");
        dateOfBirthTextView.setText("Date of Birth: " + birthdate);
        futureNotesEditText.setText(notes);

        /*if (birthdate != -1) {
            String dateString = formatDate(birthdate);
            dateOfBirthTextView.setText("Date of Birth: " + dateString);
        } else {
            dateOfBirthTextView.setText("Date of Birth: N/A");
        }*/

        // Onto the graphing
        glucoseChart = findViewById(R.id.glucoseChart);
        configureChart(glucoseChart, bloodGlucoseEntries, sweatGlucoseEntries , feedingTimes);
    }


    private void configureChart(LineChart chart, ArrayList<Entry> bloodGlucoseEntries,ArrayList<Entry> sweatGlucoseEntries, ArrayList<Long> feedingTimes) {
        // Setup the X and Y axis configurations
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                // Assuming the value is in milliseconds since epoch
                return dateFormat.format(new Date((long) value));
            }
        });

        xAxis.removeAllLimitLines();
        if (feedingTimes != null && !feedingTimes.isEmpty()) {
            for (Long feedingTime : feedingTimes) {
                // Assuming feedingTime is in milliseconds since epoch and corresponds to the X values
                LimitLine limitLine = new LimitLine(feedingTime.floatValue()*1000f);
                limitLine.setLineColor(Color.BLACK);
                limitLine.setLineWidth(1f);
                // Add any other setup for limitLine such as label, text size, etc.
                xAxis.addLimitLine(limitLine);
            }

        }

        // Create the dataset for blood glucose if entries are available
        if (bloodGlucoseEntries != null && !bloodGlucoseEntries.isEmpty()) {
            LineDataSet bloodDataSet = new LineDataSet(bloodGlucoseEntries, "Blood Glucose");
            bloodDataSet.setColor(Color.RED);
            bloodDataSet.setCircleColor(Color.RED);
            bloodDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            // Configure more properties of the dataset as needed

            LineData data = new LineData(bloodDataSet);
            chart.setData(data);
        }

        if (sweatGlucoseEntries != null && !sweatGlucoseEntries.isEmpty()) {
            LineDataSet sweatDataSet = new LineDataSet(sweatGlucoseEntries, "Sweat Glucose");
            sweatDataSet.setColor(Color.GREEN);
            sweatDataSet.setCircleColor(Color.GREEN);
            sweatDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

            LineData data = chart.getData();
            if (data != null) {
                data.addDataSet(sweatDataSet);
            } else {
                data = new LineData(sweatDataSet);
                chart.setData(data);
            }

        }

        chart.invalidate();

    }


}
