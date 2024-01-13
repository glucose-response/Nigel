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
                openAddBabyDialog(babyList);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchDataInBackground() {
        fetchDataUsingJSONParser();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchDataUsingJSONParser(){

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://nigel-c0b396b99759.herokuapp.com/";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url) // Replace with your base URL
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                BabyApi babyApi = retrofit.create(BabyApi.class);

                // Fetch profiles (assuming your endpoint returns JSON data)
                Call<ResponseBody> call = babyApi.getData(); // Assuming the response is a JSON string
                try {
                    retrofit2.Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonResponse = response.body().string();
                        JSONParser parser = new JSONParser(jsonResponse);
                        dataset = parser.getData();
                    } else {
                        System.out.println("Request unsuccessful");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        backgroundThread.start();


    }

    private void openAddBabyDialog(List<Baby> babyList) {
        AddBabyDialog addBabyDialog = new AddBabyDialog(babyList, getActivity(), new AddBabyDialog.OnAddBabyListener() {
            @Override
            public void onAddBaby() {}
        });
        addBabyDialog.show();
    }

}
