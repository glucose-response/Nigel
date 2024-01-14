package com.example.nigel;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
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
        // Get baby object from BabyListAdapter
        Baby baby = (Baby) getIntent().getSerializableExtra("Baby object");
        // int bebeInt = (int) getIntent().getSerializableExtra("BEBE_KEY");
        int bebeInt = baby.getId();
        int gestationalAge = baby.getGestationalAge();
        double weight = baby.getWeight();
        long birthdate = getIntent().getLongExtra("Date of Birth", -1);

         */
        //Baby baby = (Baby) getIntent().getSerializableExtra("Baby object");
        int bebeInt = (int) getIntent().getSerializableExtra("BEBE_KEY");
        long birthdate = getIntent().getLongExtra("Date of Birth", -1);
        int gestationalAge = (int) getIntent().getIntExtra("Gestational Age", -1);
        double weight = (double) getIntent().getSerializableExtra("Weight");
        String notes = (String) getIntent().getSerializableExtra("notes");


        // Populate text views
        textView.setText("Person " + String.valueOf(bebeInt) + " Detail Activity");
        gestationalAgeTextView.setText("Gestational Age: " + String.valueOf(gestationalAge) + " weeks");
        birthWeightTextView.setText("Birth Weight: " + String.valueOf(weight) + " kg");
        futureNotesEditText.setText(notes);

        if (birthdate != -1) {
            String dateString = formatDate(birthdate);
            dateOfBirthTextView.setText("Date of Birth: " + dateString);
        } else {
            dateOfBirthTextView.setText("Date of Birth: N/A");
        }

        // Onto the graphing
        glucoseChart = findViewById(R.id.glucoseChart);
        configureChart(glucoseChart);
    }

    private void configureChart(LineChart chart) {
        // Example axis configuration
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false); // remove grid lines
        xAxis.setDrawAxisLine(true);
        //... other axis configurations

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // remove grid lines
        //... other axis configurations

        // Set the same for the right axis if necessary
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false); // remove grid lines
        //... other axis configurations

        // Prepare the data sets (without actual data for now)
        LineDataSet bloodDataSet = new LineDataSet(null, "Blood Glucose");
        LineDataSet sweatDataSet = new LineDataSet(null, "Sweat Glucose");
        // Configure data set appearance
        bloodDataSet.setColor(Color.RED);
        sweatDataSet.setColor(Color.GREEN);
        //... other data set configurations

        // Combine the data
        LineData data = new LineData();
        data.addDataSet(bloodDataSet);
        // Set the combined data to the chart
        chart.setData(data);

        // Any additional chart configurations
        // For example, if you want to animate the chart:
        chart.animateX(1000);

        // Refresh the chart
        chart.invalidate();

    }

}
