package com.example.nigel;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

import java.io.IOException;
import java.time.LocalDate;
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
    private List<Baby> babyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BabyListAdapter adapter;
    private EditText searchEditText;
    private Map<Integer, Baby> dataset;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.content_main, container, false);
        progressBar = view.findViewById(R.id.progressBar);

        fetchDataInBackground(true);
        initializeUI(view);
        setupSearch(view);

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRefresh() {
        // Fetch data
        fetchDataInBackground(false);
    }
    private void initializeUI(@NonNull final View view) {

        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new BabyListAdapter(new HashMap<>());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


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

    private void showProgressBar(final boolean show) {
        if(getActivity() != null && progressBar != null) { // Check progressBar is not null
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }


    /**
     * Fetches data in the background with progressbar
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchDataInBackground(boolean showCentralProgressBar) {
        if (showCentralProgressBar) {
            showProgressBar(true); // Show loading
        }
        fetchDataUsingJSONParser();
    }

    /**
     * Searches the current database for the specified query
     */
    private void setupSearch(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        Button searchButton = view.findViewById(R.id.searchButton);

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

    /**
     * Fetches data from the server using the JSONParser class
     * Fills the dataset hashmap
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchDataUsingJSONParser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://nigel-c0b396b99759.herokuapp.com/";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                BabyApi babyApi = retrofit.create(BabyApi.class);

                Call<ResponseBody> call = babyApi.getData();
                try {
                    retrofit2.Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        final String jsonResponse = response.body().string();
                        final JSONParser parser = new JSONParser(jsonResponse);
                        dataset = parser.getData();
                        Log.d("My Log", dataset.toString());

                        // Updating the adapter with the fetched data should be done on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setOriginalList(dataset);
                                // Notify adapter about data set change on main/UI thread
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);// Stop the refresh animation
                                showProgressBar(false); // Hide loading
                            }
                        });
                    } else {
                        Log.e("MainBabyFragment", "Request unsuccessful: " + response.errorBody().string());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    showProgressBar(false); // Hide loading
                }
            }
        }).start();
    }


    /**
     * Function turns dataset hashmap into a list
     * @return List of Babies
     */
    private List<Baby> getBabiesFromDataset(Map<Integer, Baby> dataset) {
        List<Baby> babyList = new ArrayList<>();
        for (Map.Entry<Integer, Baby> entry : dataset.entrySet()) {
            babyList.add(entry.getValue());
        }
        return babyList;
    }

    /**
     * Function opens pop-up to add a baby object to the database
     */
    private void openAddBabyDialog() {
        AddBabyDialog addBabyDialog = new AddBabyDialog(dataset, getActivity(), new AddBabyDialog.OnAddBabyListener() {
            @Override
            public void onAddBaby() {}
        });
        addBabyDialog.show();
    }

}
