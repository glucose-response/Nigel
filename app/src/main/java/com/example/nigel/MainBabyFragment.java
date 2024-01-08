package com.example.nigel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nigel.csv.CSVHandler;
import com.example.nigel.dataclasses.BloodSample;
import com.example.nigel.dataclasses.DebugTimestampConverter;
import com.example.nigel.dataclasses.FeedingDataSample;
import com.example.nigel.dataclasses.SweatSample;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainBabyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private Executor executor = Executors.newSingleThreadExecutor();
    private OkHttpClient client = new OkHttpClient();
    private Map<Integer, Baby> babyList = new HashMap<>();
    private RecyclerView recyclerView;
    private BabyListAdapter adapter;
    private EditText searchEditText;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.content_main, container, false);
        fetchDataInBackground();
        initializeUI(view);
        setupSearch(view);

        return view;
    }
    @Override
    public void onRefresh() {
        // Fetch data
        fetchDataInBackground();
    }
    private void initializeUI(@NonNull final View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }
    private void fetchDataInBackground() {
        // Perform data fetching from CSV file or server
        executor.execute(() -> {
            Map<Integer, Baby> result = fetchData(true);

            // Update UI with the fetched data
            requireActivity().runOnUiThread(() -> {
                if (result != null) {
                    babyList.clear();
                    babyList.putAll(result);
                    Log.d("MainBabyFragment", "Baby list size: " + babyList.size());

                    adapter = new BabyListAdapter(babyList);
                    recyclerView.setAdapter(adapter);

                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    // Handle the case where 'result' is null
                    Log.e("MainBabyFragment", "Error: Result is null");
                }
            });
        });
    }

    private Map<Integer, Baby> fetchData(boolean useLocalCSV) {
        // Implement the logic to fetch data from CSV file or server
        // Return the fetched data as a List<Bebe>
        // ...

        if (useLocalCSV) {

            List<String[]> bloodSampleCSV = CSVHandler.readCSVFromAssets(getActivity(), "bloodsamplelist.csv");
            List<String[]> sweatSampleCSV = CSVHandler.readCSVFromAssets(getActivity(), "sweatsamplelist.csv");
            List<String[]> feedingCSV = CSVHandler.readCSVFromAssets(getActivity(), "feedinglist.csv");
            List<String[]> babiesCSV = CSVHandler.readCSVFromAssets(getActivity(), "babieslist.csv");

            Map<Integer, Baby> fetchedBabyList = new HashMap<>();
            Log.d("MainActivity", "CSV's Read");

            for (String[] row : babiesCSV) {
                long birthDate = DebugTimestampConverter.convertToUnixTimestamp(row[2]);
                int babyID = Integer.parseInt(row[0]);
                Baby baby = new Baby(
                        babyID,
                        String.valueOf(row[1]),
                        birthDate,
                        Double.parseDouble(row[3]),
                        row[4]);
                fetchedBabyList.put(babyID, baby);
            }

            for (String[] row : bloodSampleCSV) {
                try {
                    long timestamp = DebugTimestampConverter.convertToUnixTimestamp(row[1]);
                    int babyID = Integer.parseInt(row[0]);
                    float glucoseValue = Float.parseFloat(row[5]);
                    float lactateValue = Float.parseFloat(row[6]);
                    BloodSample bloodSample = new BloodSample(
                            timestamp,
                            babyID,
                            glucoseValue,
                            lactateValue
                    );
                    try{
                        fetchedBabyList.get(babyID).insertEvent(bloodSample);
                    }catch (NullPointerException e){
                        Log.d("BloodSampleList", "Baby ID not found");
                    }


                } catch (NumberFormatException e) {
                    Log.d("BloodSampleList", "Error converting to float");
                }
            }

            Log.d("MainActivity", "Adding Sweat Samples");
            // Add sweat sample rows to their corresponding babies
            for (String[] row : sweatSampleCSV) {
                Log.d("MainActivity", row[0] + " " + row[1] + " " + row[2] + " " + row[3] + " " + row[4] + " " + row[5] + " " + row[6] + " " + row[7] + " " + row[8] + " " + row[9]);
                int babyID = Integer.parseInt(row[0]);
                long timestamp = DebugTimestampConverter.convertToUnixTimestamp(row[1]);
                float glucoseValue = Float.parseFloat(row[4]);
                float sodiumValue = Float.parseFloat(row[5]);
                float lactateValue = Float.parseFloat(row[6]);
                float potassiumValue = Float.parseFloat(row[7]);
                float chlorideValue = Float.parseFloat(row[8]);
                float calciumValue = Float.parseFloat(row[9]);

                SweatSample sweatSample = new SweatSample(
                        timestamp,
                        babyID,
                        glucoseValue,
                        sodiumValue,
                        lactateValue,
                        potassiumValue,
                        chlorideValue,
                        calciumValue
                );

                try{
                    fetchedBabyList.get(babyID).insertEvent(sweatSample);
                } catch (NullPointerException e){
                    Log.d("SweatSampleList", "Baby ID not found");
                }
            }

            for (String[] row : feedingCSV) {
                int babyID = Integer.parseInt(row[0]);
                long timestamp = DebugTimestampConverter.convertToUnixTimestamp(row[1]);
                String type = row[2];
                FeedingDataSample feeding = new FeedingDataSample(
                        timestamp,
                        babyID,
                        type);
                fetchedBabyList.get(babyID).insertEvent(feeding);
            }

            Log.d("MainActivity", "Babies, BloodSamples, and SweatSamples Added");
            Log.d("BabyList", fetchedBabyList.toString());

            return fetchedBabyList;
        } else {
            String url = "https://jaminhu19.pythonanywhere.com/api/data";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                // Synchronous request (executes on the background thread)
                Response response = client.newCall(request).execute();
                Log.d("MainActivity", "Response: " + response.toString());

                if (response.isSuccessful()) {
                    // Parse the response and return the data as a List<Bebe>
                    return parseResponse(response.body().string());
                } else {
                    // Handle unsuccessful response
                    return null;
                }
            } catch (IOException e) {
                // Handle exceptions
                e.printStackTrace();
                return null;
            }
        }
    }




    private Map<Integer, Baby> parseResponse(String responseBody) {
        // Implement logic to parse the response and convert it to a List<Bebe>
        // ...
        Log.d("MainActivity", "Response body: " + responseBody);
        Map<Integer, Baby> babyList = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(responseBody);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("Nigel ID");
                float weight = (float) jsonObject.getDouble("Birth Weight (kg)");
                String group = jsonObject.getString("Group");
                int timeOfBirth = jsonObject.getInt("Time of Birth");
                String notes = jsonObject.getString("Notes");
                // Now you can use the id and name as needed
                babyList.put(id,
                        new Baby(
                                id,
                                group,
                                timeOfBirth,
                                weight,
                                notes)
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return babyList;
    }

    private void setupSearch(View view) {
        searchEditText = view.findViewById(R.id.searchEditText); // Replace with your actual EditText ID
        Button searchButton = view.findViewById(R.id.searchButton); // Replace with your actual Button ID

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

}
