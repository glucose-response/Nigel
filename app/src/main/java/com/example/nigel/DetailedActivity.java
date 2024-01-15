package com.example.nigel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**Class defines the individual baby page*/
public class DetailedActivity extends AppCompatActivity {
    private LineChart glucoseChart;

    /**Helper method to format the date*/
    private String formatDate(long timestamp) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Convert the timestamp to a Date object and format it.
        return formatter.format(new Date(timestamp));
    }
    /**
     * Method automatically called when page opens*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // Retrieve views
        ImageButton backButton = findViewById(R.id.backButton);
        TextView textView = findViewById(R.id.nameTextView);
        TextView dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView);
        TextView gestationalAgeTextView = findViewById(R.id.gestationalAgeTextView);
        TextView birthWeightTextView = findViewById(R.id.birthWeightTextView);
        TextView daysOfLifeTextView = findViewById(R.id.daysOfLifeTextView);
        TextView futureNotesEditText = findViewById(R.id.futureNotesEditText);
        TextView chartTitleTextView = findViewById(R.id.chartTitleTextView);

        /*
        // Get whole baby object from BabyListAdapter
        Baby baby = (Baby) getIntent().getSerializableExtra("Baby object");
        // int bebeInt = (int) getIntent().getSerializableExtra("BEBE_KEY");
        int bebeInt = baby.getId();
        int gestationalAge = baby.getGestationalAge();
        double weight = baby.getWeight();
        long birthdate = getIntent().getLongExtra("Date of Birth", -1);

         */
        // Get individual baby attributes from BabyListAdapter via Intent
        // Setup the back button for home page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the intent to navigate back to the MainActivity
                Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                // If you don't want to keep this activity on the stack, clear it
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // If you want to close this activity
            }
        });


        //Baby baby = (Baby) getIntent().getSerializableExtra("Baby object");
        int bebeInt = (int) getIntent().getSerializableExtra("Nigel ID");
        double gestationalAge = (double) getIntent().getSerializableExtra("Gestational Age");
        String birthdate = (String) getIntent().getSerializableExtra("Date of Birth");
        double weight = (double) getIntent().getSerializableExtra("Weight");
        String daysOfLife = (String) getIntent().getSerializableExtra("Days of life");
        String notes = (String) getIntent().getSerializableExtra("Notes");

        // Retrieve the Blood, Sweat and Feeding entries
        ArrayList<Entry> bloodGlucoseEntries = (ArrayList<Entry>) getIntent().getSerializableExtra("bloodGlucoseEntries");
        ArrayList<Entry> sweatGlucoseEntries = (ArrayList<Entry>) getIntent().getSerializableExtra("SweatGlucoseEntries");
        ArrayList<Long> feedingTimes = (ArrayList<Long>) getIntent().getSerializableExtra("FeedingTimes");

        // Populate text views
        textView.setText("Person " + String.valueOf(bebeInt) + " Detail Activity");
        dateOfBirthTextView.setText("Date of Birth: " + birthdate);
        gestationalAgeTextView.setText("Gestational Age: " + String.valueOf(gestationalAge) + " weeks");
        birthWeightTextView.setText("Birth Weight: " + String.valueOf(weight) + " kg");
        daysOfLifeTextView.setText("Days of life: "+daysOfLife);
        futureNotesEditText.setText(notes);
        chartTitleTextView.setText("Glucose Measurements (Blood and Sweat)");

        /*if (birthdate != -1) {
            String dateString = formatDate(birthdate);
            dateOfBirthTextView.setText("Date of Birth: " + dateString);
        } else {
            dateOfBirthTextView.setText("Date of Birth: N/A");
        }*/

        // Onto the graphing
        CombinedChart glucoseChart = findViewById(R.id.glucoseChart);
        configureChart(glucoseChart, bloodGlucoseEntries, sweatGlucoseEntries , feedingTimes);
    }


    /**
     * Method creates the charts in the individual view*/
    private void configureChart(CombinedChart combinedChart, ArrayList<Entry> bloodGlucoseEntries, ArrayList<Entry> sweatGlucoseEntries, ArrayList<Long> feedingTimes) {
        // Setup the X and Y axis configurations
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long milliseconds = (long) value * 1000; // Convert seconds to milliseconds
                return new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(new Date(milliseconds));
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
        ScatterDataSet bloodDataSet = new ScatterDataSet(bloodGlucoseEntries, "Blood Glucose");
        bloodDataSet.setColor(Color.argb(255, 190, 46, 23));
        bloodDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ScatterData bloodData = new ScatterData(bloodDataSet);


        LineDataSet sweatDataSet = new LineDataSet(sweatGlucoseEntries, "Sweat Glucose");
        sweatDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        sweatDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        sweatDataSet.setCubicIntensity(0.1f);
        sweatDataSet.setColor(Color.argb(255, 142, 186, 140));
        sweatDataSet.setCircleColor(Color.argb(255, 142, 186, 140));
        sweatDataSet.setCircleHoleColor(Color.argb(255, 142, 186, 140));
        LineData sweatData = new LineData(sweatDataSet);


        CombinedData combinedData = new CombinedData();
        combinedData.setData(bloodData);
        combinedData.setData(sweatData);

        combinedChart.setData(combinedData);
        combinedChart.invalidate();

    }


}
