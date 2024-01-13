package com.example.nigel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {
    private LineChart skinGlucoseChart;
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

        // get values from BabyListAdapter
        int bebeInt = (int) getIntent().getSerializableExtra("BEBE_KEY");
        int gestationalAge = (int) getIntent().getSerializableExtra("Gestational Age");
        double weight = (double) getIntent().getSerializableExtra("Weight");
        long birthdate = getIntent().getLongExtra("Date of Birth", -1);

        // populate text views
        textView.setText("Person " + String.valueOf(bebeInt) + " Detail Activity");

        if (birthdate != -1) {
            String dateString = formatDate(birthdate);
            dateOfBirthTextView.setText("Date of Birth: " + dateString);
        } else {
            dateOfBirthTextView.setText("Date of Birth: N/A");
        }

        gestationalAgeTextView.setText("Gestational Age: "+String.valueOf(gestationalAge)+" weeks");

        birthWeightTextView.setText("Birth Weight: "+String.valueOf(weight)+" kg");



        skinGlucoseChart = findViewById(R.id.skinGlucoseChart);

    }

}
