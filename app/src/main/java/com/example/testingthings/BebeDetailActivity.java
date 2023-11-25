package com.example.testingthings;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BebeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textView = findViewById(R.id.nameTextView);
        int bebeInt = (int) getIntent().getSerializableExtra("BEBE_KEY");
        // Now you can use the 'bebe' variable to populate your views...

        textView.setText("Person " + String.valueOf(bebeInt) + " Detail Activity");

    }

}
