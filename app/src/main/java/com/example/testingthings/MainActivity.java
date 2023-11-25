package com.example.testingthings;

import android.os.Bundle;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingthings.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// MainActivity.java
public class MainActivity extends AppCompatActivity {

    private List<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate personList with data (replace this with your actual data)
        personList.add(new Person("Person 1", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 2", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 3", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 4", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 5", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 6", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 7", generateRandomTimeSeriesData()));
        personList.add(new Person("Person 8", generateRandomTimeSeriesData()));
        // ...

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChartAdapter adapter = new ChartAdapter(personList);
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
