package com.example.nigel;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import org.json.JSONTokener;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainBabyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private Executor executor = Executors.newSingleThreadExecutor();
    private OkHttpClient client = new OkHttpClient();
    private Map<Integer, Baby> babyList = new HashMap<>();
    private RecyclerView recyclerView;
    private BabyListAdapter adapter;
    private EditText searchEditText;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
    @RequiresApi(api = Build.VERSION_CODES.O)
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

        Button addBabyButton = view.findViewById(R.id.addBabyButton);
        addBabyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBabyDialog();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
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
                int gestationalAge = jsonObject.getInt("Gestational Age");
                int timeOfBirth = jsonObject.getInt("Time of Birth");
                String notes = jsonObject.getString("Notes");
                // Now you can use the id and name as needed
                babyList.put(id,
                        new Baby(
                                id,
                                gestationalAge,
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

    // Replace this method with your actual data fetching logic
    private List<Entry> generateRandomTimeSeriesData() {
        List<Entry> data = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            data.add(new Entry(i, random.nextFloat()));
        }

        return data;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
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
                //jsonResponse = jsonResponse.substring(13, jsonResponse.length() - 2);
                babyList = parseJSONResponse(jsonResponse);
            } else {
                System.out.println("Request unsuccessful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return babyList;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Baby> parseJSONResponse(String responseBody){
        List<Baby> babyList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(responseBody);
            JSONArray jsonArray  = object.getJSONArray("profiles");
            //JSONArray jsonArray = new JSONArray(responseBody);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("NigelID");
                String[] dateOfBirth = jsonObject.getString("birthday").split("-");
                LocalDate dateOfBirthObject = LocalDate.of(Integer.parseInt(dateOfBirth[0]), Integer.parseInt(dateOfBirth[1]), Integer.parseInt(dateOfBirth[2]));
                double weight = (double) jsonObject.getDouble("birthWeight");
                double gestationalAge = jsonObject.getDouble("gestationalAge");
                String notes = jsonObject.getString("notes");

                // Now you can use the id and name as needed
                babyList.add(
                        new Baby(
                                id,
                                dateOfBirthObject,
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

    private void openAddBabyDialog() {
        AddBabyDialog addBabyDialog = new AddBabyDialog(getActivity(), new AddBabyDialog.OnAddBabyListener() {
            @Override
            public void onAddBaby() {}
        });
        addBabyDialog.show();
    }

}
