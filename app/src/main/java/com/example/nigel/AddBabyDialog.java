package com.example.nigel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Used Chatgpt to create a dialog
public class AddBabyDialog extends Dialog {

    private EditText editTextBabyID;
    private EditText editTextDOB;
    private EditText editTextAge;
    private Spinner spinnerGroup;
    private TextView outputText;
    private Button addButton;
    private String url = "https://nigel-c0b396b99759.herokuapp.com/";
    private String POST = "PUT";
    private String GET = "GET";
    private BabyApi babyApi;
    private OnAddBabyListener onAddBabyListener;

    public AddBabyDialog(@NonNull Context context, OnAddBabyListener onAddBabyListener) {
        super(context);
        this.onAddBabyListener = onAddBabyListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_baby);

        editTextBabyID = findViewById(R.id.editTextBabyID);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextAge = findViewById(R.id.editTextAge);
        spinnerGroup = findViewById(R.id.spinnerGroup);
        outputText = findViewById(R.id.outputText);
        addButton = findViewById(R.id.addButton);

  /*      // Set up the spinner with an array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.group_options, // Create an array resource in res/values/arrays.xml with your group options
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);*/
        // Example of setting text (a string) in TextView
        outputText.setText("Output: This is where you can place the values you get from the DataBase");

        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item change if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        babyApi = retrofit.create(BabyApi.class);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display details in the TextBox
                String NigID = editTextBabyID.getText().toString();
                String DoB = editTextDOB.getText().toString();
                String Age  = editTextAge.getText().toString();
                String selectedGroup = spinnerGroup.getSelectedItem().toString();


                if (NigID.isEmpty()) {
                    editTextBabyID.setError("The NigID cannot be empty");
                }else if (DoB.isEmpty()) {
                    editTextDOB.setError("The date of birth cannot be empty");
                }else if (Age.isEmpty()){
                    editTextAge.setError("The age cannot be empty");
                } else {
                    // Create a Baby object with the entered data
                    BabyDetails baby = new BabyDetails();
                    baby.setBabyID(NigID);
                    baby.setDob(DoB);
                    baby.setAge(Age);
                    baby.setGroup(selectedGroup);

                    // Send the Baby object in the POST request
                    sendRequest(POST, "addBaby", baby);

                    // Display details in the TextBox
                    String details = "NigelID: " + NigID + "\nGestational Age: " + Age + "\nDOB:" + DoB + "\nGroup: " + selectedGroup;
                    outputText.setText(details);
                }
            }
        });
    }

    void sendRequest(String type, String method, BabyDetails baby) {
        Call<ResponseBody> call;

        if (type.equals(POST)) {
            call = babyApi.addBaby(baby);
        } else {
            // Handle other types or methods if needed
            return;
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        new Handler(Looper.getMainLooper()).post(() -> outputText.setText(responseData));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case when the response body is null
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface OnAddBabyListener {
        void onAddBaby(String babyID, String dob, String group);
    }
}