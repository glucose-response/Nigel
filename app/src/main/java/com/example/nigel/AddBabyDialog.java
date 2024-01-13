package com.example.nigel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Used ChatGPT to create a dialog
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
    private List babyList;

    public AddBabyDialog(List babyList, @NonNull Activity context, OnAddBabyListener onAddBabyListener){
        super(context);
        this.context = context;
        this.babyList = babyList;
        this.onAddBabyListener = onAddBabyListener;
    }

    // This method is called when the dialog is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_baby);

        editTextBabyID = findViewById(R.id.editTextBabyID);
        editTextDOBDay = findViewById(R.id.editTextDOBDay);
        editTextDOBMonth = findViewById(R.id.editTextDOBMonth);
        editTextDOBYear = findViewById(R.id.editTextDOBYear);
        editTextGestAge = findViewById(R.id.editTextGestAge);
        editTextWeight = findViewById(R.id.editTextWeight);
        //spinnerGroup = findViewById(R.id.spinnerGroup);
        outputText = findViewById(R.id.outputText);
        addButton = findViewById(R.id.addButton);
        exitButton = findViewById(R.id.exitButton);
        editAdditionalNotes= findViewById(R.id.editAdditionalNotes);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        babyApi = retrofit.create(BabyApi.class);

        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String NigID = "";
                String DobDay = "";
                String DobMonth = "";
                String DobYear = "";
                String Age  = "";
                String Weight  = "";
                String notes = "";

                // Display details in the TextBox
                NigID = editTextBabyID.getText().toString();
                DobDay = editTextDOBDay.getText().toString();
                DobMonth = editTextDOBMonth.getText().toString();
                DobYear = editTextDOBYear.getText().toString();
                Age  = editTextGestAge.getText().toString();
                Weight  = editTextWeight.getText().toString();
                notes = editAdditionalNotes.getText().toString();


                boolean valid = checkEmpty(NigID, DobDay, DobMonth, DobYear, Age, Weight);
                if(valid){
                    valid = valid && !idExists(Integer.parseInt(NigID));
                    valid = valid && checkInput(Integer.parseInt(NigID), Integer.parseInt(DobDay), Integer.parseInt(DobMonth), Integer.parseInt(DobYear), Integer.parseInt(Age), Double.parseDouble(Weight));
                }


                if (valid){
                    // Convert Strings into Data
                    int nigID = Integer.parseInt(NigID);
                    LocalDate birthdayDate = LocalDate.of(Integer.parseInt(DobYear), Integer.parseInt(DobMonth), Integer.parseInt(DobDay));
                    String birthdayString = DobYear + "-" + DobMonth + "-" + DobDay;
                    double age  = Double.parseDouble(Age);
                    double weight  = Double.parseDouble(Weight);
                    // Create a Baby object with the entered data
                    Baby baby = new Baby(nigID, birthdayString, weight, age, notes);

                    // Send the Baby object in the PUT request
                    sendRequest(PUT, "addBaby", baby, 0);
                    // Display details in the TextBox
                    String details = "NigelID: " + NigID + "\nGestational Age: " + Age +
                            "\nDOB:" + birthdayString +
                            "\nWeight: " + Weight +
                            "\nAdditional Notes: " + notes;
                    details = details + "\nBaby added to the database, please close this tab";
                    outputText.setText("Added Succesfully");
                    resetFields();
                }

            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
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
     * @param type of HTTP request
     * @param method of HTTP request
     * @param baby the baby object to be added to the database
     */
    void sendRequest(String type, String method, Baby baby, int id) {
        Call<ResponseBody> call;

        if (type.equals(PUT) && method.equals("addBaby")){
            call = babyApi.addBaby(baby);}
        else {
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

    /**
     * This method checks if the ID already exist
     * @param id the ID to be checked
     * @return true if the ID already exists, false otherwise
     */
    private boolean idExists(int id){
        for (int i = 0; i < babyList.size(); i++){
            Baby baby = (Baby) babyList.get(i);
            if (baby.getId() == id){
                editTextBabyID.setError("The ID already exists");
                return true;
            }
        }
        return false;
    }

    // This is the interface that will be used to communicate with the activity that created the dialog
    public interface OnAddBabyListener {
        void onAddBaby();
    }
}
