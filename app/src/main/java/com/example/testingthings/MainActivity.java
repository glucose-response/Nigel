package com.example.testingthings;

import android.os.Bundle;

import com.github.mikephil.charting.data.Entry;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// MainActivity.java
public class MainActivity extends AppCompatActivity {

    private List<Bebe> bebeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate personList with data (replace this with your actual data)
        for (int i = 0; i < 10; i++) {
            bebeList.add(new Bebe(i, "Person " + String.valueOf(i), generateRandomTimeSeriesData()));
        }
        // ...

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BebeListAdapter adapter = new BebeListAdapter(bebeList);
        recyclerView.setAdapter(adapter);
    }

    // Replace this method with your actual data fetching logic
    private List<Entry> generateRandomTimeSeriesData() {
        List<Entry> data = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            data.add(new Entry(i, random.nextFloat()));
        }

        return data;
    }
}
