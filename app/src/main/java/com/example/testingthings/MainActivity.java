package com.example.testingthings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private RecyclerView recyclerView;
    private BebeListAdapter adapter;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate personList with data (replace this with your actual data)
        for (int i = 0; i < 10; i++) {
            bebeList.add(new Bebe(i, "Person " + String.valueOf(i), generateRandomTimeSeriesData()));
        }
        // ...

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BebeListAdapter(bebeList);
        recyclerView.setAdapter(adapter);

        setupSearch();
    }

    private void setupSearch() {
        searchEditText = findViewById(R.id.searchEditText); // Replace with your actual EditText ID
        Button searchButton = findViewById(R.id.searchButton); // Replace with your actual Button ID

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Search button clicked");
                String query = searchEditText.getText().toString();
                adapter.filterByName(query);
                recyclerView.setAdapter(adapter);
                Log.d("MainActivity", "Filtered List" + adapter.getFilteredList().toString());
            }
        });
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
