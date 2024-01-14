package com.example.nigel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** This class is used to configure the addBaby Dialog
 * Sourced: ChatGPT*/
public class AddBabyDialog extends Dialog{
    private Context context;
    private EditText editTextBabyID;
    private EditText editTextDOBDay;
    private EditText editTextDOBMonth;
    private EditText editTextDOBYear;
    private EditText editTextGestAge;
    private EditText editTextWeight;
    private EditText editAdditionalNotes;
    private TextView outputText;
    private Button addButton;
    private Button exitButton;
    private String url = "https://nigel-c0b396b99759.herokuapp.com/";
    private String PUT = "PUT";
    private String GET = "GET";
    private BabyApi babyApi;
    private OnAddBabyListener onAddBabyListener;
    private Map<Integer,Baby> babyMap;

    /**
     * This is the constructor for the dialog
     * @param babyMap the map of babies (all of the data)
     * @param context the context of the dialog
     * @param onAddBabyListener the listener for the dialog
     */
    public AddBabyDialog(Map<Integer, Baby> babyMap, @NonNull Activity context, OnAddBabyListener onAddBabyListener){
        super(context);
        this.context = context;
        this.babyMap = babyMap;
        this.onAddBabyListener = onAddBabyListener;
    }

    /**This method is called when the dialog is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_baby);

        // Define the variable for each item in the interface
        editTextBabyID = findViewById(R.id.editTextBabyID);
        editTextDOBDay = findViewById(R.id.editTextDOBDay);
        editTextDOBMonth = findViewById(R.id.editTextDOBMonth);
        editTextDOBYear = findViewById(R.id.editTextDOBYear);
        editTextGestAge = findViewById(R.id.editTextGestAge);
        editTextWeight = findViewById(R.id.editTextWeight);
        outputText = findViewById(R.id.outputText);
        addButton = findViewById(R.id.addButton);
        exitButton = findViewById(R.id.exitButton);
        editAdditionalNotes= findViewById(R.id.editAdditionalNotes);


        // Create a Retrofit object for communiation with the database
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Define the API for the Retrofit object
        babyApi = retrofit.create(BabyApi.class);

        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Default variables for each item in the interface
                String NigID = "";
                String DobDay = "";
                String DobMonth = "";
                String DobYear = "";
                String Age  = "";
                String Weight  = "";
                String notes = "";

                // Retrieve details in the TextBox
                NigID = editTextBabyID.getText().toString();
                DobDay = editTextDOBDay.getText().toString();
                DobMonth = editTextDOBMonth.getText().toString();
                DobYear = editTextDOBYear.getText().toString();
                Age  = editTextGestAge.getText().toString();
                Weight  = editTextWeight.getText().toString();
                notes = editAdditionalNotes.getText().toString();


                // Check if the input is valid
                boolean valid = checkEmpty(NigID, DobDay, DobMonth, DobYear, Age, Weight);
                if(valid){
                    valid = valid && !idExists(Integer.parseInt(NigID));
                    valid = valid && checkInput(Integer.parseInt(NigID), Integer.parseInt(DobDay), Integer.parseInt(DobMonth), Integer.parseInt(DobYear), Integer.parseInt(Age), Double.parseDouble(Weight));
                }

                // If the input is correct (not empty, reasonable values, id does not exist)
                if (valid){

                    // Convert Strings into Data
                    int nigID = Integer.parseInt(NigID);
                    String birthdayString = DobYear + "-" + DobMonth + "-" + DobDay;
                    double age  = Double.parseDouble(Age);
                    double weight  = Double.parseDouble(Weight);

                    // Create a Baby object with the entered data
                    Baby baby = new Baby(nigID, age, birthdayString, weight, notes);

                    // Send the Baby object in the PUT request
                    sendRequest(baby);

                    // Display details in the TextBox
                    String details = "NigelID: " + NigID + "\nGestational Age: " + Age +
                            "\nDOB:" + birthdayString +
                            "\nWeight: " + Weight +
                            "\nAdditional Notes: " + notes;

                    details = details + "\nBaby added to the database, please close this tab";
                    outputText.setText("Request send to server");
                    // Function to empty fields for the next set of data to input
                    resetFields();
                }

            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // Function closes the dialog
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    /**
     * This method resets the fields of the dialog
     */
    private void resetFields(){
        editTextBabyID.setText("");
        editTextDOBDay.setText("");
        editTextDOBMonth.setText("");
        editTextDOBYear.setText("");
        editTextGestAge.setText("");
        editTextWeight.setText("");
        editAdditionalNotes.setText("");
    }
    /**
     * This method checks if the input string is not empty
     * @param NigID the NigID of the baby
     * @param DobDay the day of birth of the baby
     * @param DobMonth the month of birth of the baby
     * @param DobYear the year of birth of the baby
     * @param Age the age of the baby
     * @param Weight the weight of the baby
     * @return true if the input is valid, false otherwise
     */
    private boolean checkEmpty(String NigID, String DobDay, String DobMonth, String DobYear, String Age, String Weight){
        if (NigID.isEmpty()) {
            outputText.setError("The NigID cannot be empty");
            return false;}
        if (DobDay.isEmpty()){
            outputText.setError("The day of birth cannot be empty");
            return false;}
        if (DobMonth.isEmpty()) {
            outputText.setError("The month of birth cannot be empty");
            return false;}
        if (DobYear.isEmpty()){
            outputText.setError("The year of birth cannot be empty");
            return false;}
        if (Age.isEmpty()){
            outputText.setError("The age cannot be empty");
            return false;}
        if (Weight.isEmpty()) {
            outputText.setError("The weight cannot be empty");
            return false;}
        return true;
    }
    /**
     * This method checks if the input is valid
     * @param NigID the NigID of the baby
     * @param DobDay the day of birth of the baby
     * @param DobMonth the month of birth of the baby
     * @param DobYear the year of birth of the baby
     * @param Age the age of the baby
     * @param Weight the weight of the baby
     * @return true if the input is valid, false otherwise
     */
    private boolean checkInput(int NigID, int DobDay, int DobMonth, int DobYear, int Age, double Weight){
        if (NigID < 0){
            editTextBabyID.setError("The NigID cannot be negative");
            return false;
        }
        else if (DobDay < 0 || DobDay > 31){
            editTextDOBDay.setError("The day of birth must be between 1 and 31");
            return false;
        }
        else if (DobMonth < 0 || DobMonth > 12){
            editTextDOBMonth.setError("The month of birth must be between 1 and 12");
            return false;
        }
        else if (DobYear < 0){
            editTextDOBYear.setError("The year of birth cannot be negative");
            return false;
        }
        else if (Age < 0){
            editTextGestAge.setError("The age cannot be negative");
            return false;
        }
        else if (Weight < 0){
            editTextWeight.setError("The weight cannot be negative");
            return false;
        }
        else{
            return true;
        }
    }

    /**This method sends a request to add the babys details to the server and database
     * @param baby the baby object to be added to the database
     */
    void sendRequest(Baby baby) {
        Call<ResponseBody> call = babyApi.addBaby(baby);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    try {
                        // Display the error message
                        String errorBody = response.errorBody().string();
                        Log.e("Error Body", errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.body() != null) {
                    try {
                        // Display the response message
                        String responseData = response.body().string();
                        new Handler(Looper.getMainLooper()).post(() -> outputText.setText(responseData));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {}
            }
            @Override
            // Display the error message
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    /**
     * This method checks if the ID already exist in the dataset (Map)
     * @param id the ID to be checked
     * @return true if the ID already exists, false otherwise
     */
    private boolean idExists(int id){
        Set<Integer> babyIDs = babyMap.keySet();
        for (int babyID : babyIDs){
            if (babyID == id){
                editTextBabyID.setError("The ID already exists");
                return true;
            }
        }
        return false;
    }

    /**This is the interface that will be used to communicate with the activity that created the dialog*/
    public interface OnAddBabyListener {
        void onAddBaby();
    }
}
