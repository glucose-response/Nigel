package com.example.nigel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private AccountSettings settings;
    private ConstraintLayout mContentMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mContentMain = findViewById(R.id.main_layout);

        displayFragment(new MainBabyFragment());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setupSearch();

        // Add Baby button
        Button addBabyButton = findViewById(R.id.addBabyButton); // Replace with your actual Button ID

        // Set a click listener for the Add Baby button
        addBabyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBabyDialog();
            }
        });
    }

    //Tian's new code to open the dialog
    private void openAddBabyDialog() {
        AddBabyDialog addBabyDialog = new AddBabyDialog(this, new AddBabyDialog.OnAddBabyListener() {
            @Override
            public void onAddBaby(String babyID, String dob, String group) {
                // Handle the data received from the dialog if needed
                // For example, you can send the data to your server or update the UI
            }
        });
        addBabyDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    public void onRefresh() {
        // Fetch data
        fetchDataInBackground();
        }

    private void fetchDataInBackground() {
        // Perform data fetching from CSV file or server
        executor.execute(() -> {
            List<Baby> result = fetchDataFromDatabaseViaAPI();

            // Update UI with the fetched data
            runOnUiThread(() -> {
                babyList.clear();
                babyList.addAll(result);
                Log.d("MainActivity", "Bebe list size: " + babyList.size());

                adapter = new BabyListAdapter(babyList);
                recyclerView.setAdapter(adapter);

                swipeRefreshLayout.setRefreshing(false);

            });
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account_settings) {
            displayFragment(new LogoutFragment());
            return true;
        }
        if (id == R.id.home_button) {
            displayFragment(new MainBabyFragment());
            return true;
        }

    private List<Baby> fetchData() {
        // Implement the logic to fetch data from CSV file or server
        // Return the fetched data as a List<Bebe>
        // ...
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
    private List<Baby> fetchDataFromDatabaseViaAPI(){

        String url = "https://nigel-c0b396b99759.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BabyApi babyApi = retrofit.create(BabyApi.class);

        // Fetch profiles (assuming your endpoint returns JSON data)
        Call<ResponseBody> call = babyApi.getBabies(); // Assuming the response is a JSON string
        List<Baby> babyList = new ArrayList<>();

        try {
            retrofit2.Response<ResponseBody> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                jsonResponse = jsonResponse.substring(13, jsonResponse.length() - 2);
                babyList = parseJSONResponse(jsonResponse);
            } else {
                System.out.println("Request unsuccessful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return babyList;


    }

    private List<Baby> parseJSONResponse(String responseBody){
        List<Baby> babyList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(responseBody);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("NigelID");
                long dateOfBirth = jsonObject.getLong("DateOfBirth");
                double weight = (double) jsonObject.getDouble("BirthWeight");
                double gestationalAge = jsonObject.getDouble("GestationalAge");
                String notes = jsonObject.getString("Notes");

                // Now you can use the id and name as needed
                babyList.add(
                        new Baby(
                                id,
                                dateOfBirth,
                                weight,
                                gestationalAge,
                                notes,
                                generateRandomTimeSeriesData())
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return babyList;
    }
    private List<Baby> parseResponse(String responseBody) {
        // Implement logic to parse the response and convert it to a List<Bebe>
        // ...
        Log.d("MainActivity", "Response body: " + responseBody);
        List<Baby> babyList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(responseBody);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("Nigel ID");
                double weight = (double) jsonObject.getDouble("Birth Weight (kg)");
                String group = jsonObject.getString("Group");
                long timeOfBirth = jsonObject.getInt("Time of Birth");
                double gestationalAge = jsonObject.getDouble("Gestational Age");

                // Now you can use the id and name as needed
                babyList.add(
                        new Baby(
                                id,
                                timeOfBirth,
                                weight,
                                gestationalAge,
                                group,
                                generateRandomTimeSeriesData())
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return babyList;
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

        return super.onOptionsItemSelected(item);
    }

    private void displayFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),fragment)
                .commit();
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
