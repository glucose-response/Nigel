package com.example.nigel;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.example.nigel.dataclasses.BloodSample;
import com.example.nigel.dataclasses.DataSample;
import com.example.nigel.dataclasses.SweatSample;
import com.example.nigel.dataclasses.FeedingDataSample;

public class JSONParser {

    private String response;
    private Map<Integer, Baby> dataset;

    public JSONParser(String response) {
        this.response = response;
        this.dataset = new HashMap<>();
    }
    /**
     * Parses the JSON response from the server into a Map
     *
     * @return Map of Babies with their entire data set
     * @throws JSONException If there is an error while parsing the JSON response
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<Integer,Baby> getData() throws JSONException {

        // Assuming jsonString is your JSON response string
        JSONObject jsonResponse = new JSONObject(response);

        // Extract the "data" array
        JSONArray jsonArray = jsonResponse.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            System.out.println(i);

            // Define the JSON object of interest
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // Define the babys attributes
            int id = jsonObject.getInt("NigelID");
            double weight = (double) jsonObject.getDouble("birthWeight");
            String[] dateOfBirth = jsonObject.getString("birthday").split("-");
            LocalDate birthday = LocalDate.of(Integer.parseInt(dateOfBirth[0]), Integer.parseInt(dateOfBirth[1]), Integer.parseInt(dateOfBirth[2]));
            double gestationalAge = jsonObject.getDouble("gestationalAge");
            String notes = jsonObject.getString("notes");

            // Define the babys data samples
            List<BloodSample> bloodSamples = parseBloodSample(jsonObject.getJSONObject("blood"),id);
            List<SweatSample> sweatSamples = parseSweatSample(jsonObject.getJSONObject("sweat"),id);
            List<FeedingDataSample> feedingDataSamples = parseFeedingDataSample(jsonObject.getJSONObject("feeding"),id);

            // Combine all data samples into one list
            ArrayList<DataSample> dataSamples = new ArrayList();
            dataSamples.addAll(bloodSamples);
            dataSamples.addAll(sweatSamples);
            dataSamples.addAll(feedingDataSamples);

            // Create a new baby object and add it to the dataset
            Baby baby = new Baby(id,gestationalAge, birthday, weight, notes, dataSamples);
            dataset.put(baby.getId(),baby);
        }
        return dataset;
    }

    /**
     * Parses a JSON response containing blood samples into a list of BloodSample objects
     *
     * @param response The JSON response containing blood samples
     * @param NigelID The identifier associated with Nigel
     * @return A list of BloodSample objects parsed from the JSON response
     * @throws JSONException If there is an error while parsing the JSON response
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<BloodSample> parseBloodSample(JSONObject response, int NigelID) throws JSONException {

        List<BloodSample> bloodSamples = new ArrayList<>();
        Iterator<String> it = response.keys();

        while (it.hasNext()) {
            String date = it.next();
            JSONArray records = response.getJSONArray(date);
            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);
                String sampleType = record.getString("Biochem/Blood Gas");
                double calcium = record.getDouble("Calcium Alb corrected");
                double calciumTotal = record.getDouble("Calcium Total");
                double chloride = record.getDouble("Chloride");
                int clinicianID = record.getInt("ClinicianID");
                float glucose = (float) record.getDouble("Glucose");
                float lactate = (float) record.getDouble("Lactate");
                double potassium = record.getDouble("Potassium");
                double sodium = record.getDouble("Sodium");
                String source = record.getString("Source (art/cap/venous)");
                String timestamps = record.getString("Timestamp");
                long timestamp = parseTimestampToUnix(timestamps);

                // Create a BloodSample Object
                BloodSample bloodSample = new BloodSample(timestamp,NigelID, glucose, (float) lactate);
                bloodSamples.add(bloodSample);
            }
        }
        return bloodSamples;
    }

    /**
     * Parses a JSON response containing sweat samples into a list of SweatSample objects
     *
     * @param response The JSON response containing sweat samples
     * @param NigelID The identifier associated with Nigel
     * @return A list of SweatSample objects parsed from the JSON response
     * @throws JSONException If there is an error while parsing the JSON response
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<SweatSample> parseSweatSample(JSONObject response, int NigelID) throws JSONException {

        List<SweatSample> sweatSamples = new ArrayList<>();
        Iterator<String> it = response.keys();

        while (it.hasNext()) {
            String date = it.next();
            JSONArray records = response.getJSONArray(date);
            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);
                float calcium = (float) record.getDouble("Calcium");
                float chloride = (float) record.getDouble("Chloride");
                int clinicianID = record.getInt("ClinicianID");
                double duration = record.getDouble("Duration /h");
                float glucose = (float) record.getDouble("Glucose /mM");
                float lactate = (float) record.getDouble("Lactate");
                float potassium = (float) record.getDouble("Potassium");
                float sodium = (float) record.getDouble("Sodium");
                String timestamps = record.getString("Timestamp");
                long timestamp = parseTimestampToUnix(timestamps);

                // Create a SweatSample Object
                SweatSample sweatSample = new SweatSample(timestamp,NigelID, glucose, sodium, lactate, potassium, chloride, calcium);
                sweatSamples.add(sweatSample);
            }
        }
        return sweatSamples;
    }

    /**
     * Parses a JSON response containing feeding data samples into a list of FeedingDataSample objects
     *
     * @param response The JSON response containing feeding data samples
     * @param NigelID The identifier associated with Nigel
     * @return A list of FeedingDataSample objects parsed from the JSON response
     * @throws JSONException If there is an error while parsing the JSON response
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<FeedingDataSample> parseFeedingDataSample(JSONObject response, int NigelID) throws JSONException {

        // Empty list to be filled with FeedingDataSamples
        List<FeedingDataSample> FeedingDataSamples = new ArrayList<>();
        Iterator<String> it = response.keys();

        while (it.hasNext()) {
            String date = it.next();
            JSONArray records = response.getJSONArray(date);
            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);
                String type = record.getString("Type feeding ");
                String timestamps = record.getString("Timestamp");
                long timestamp = parseTimestampToUnix(timestamps);

                // Create a FeedingDataSample Object
                FeedingDataSample fed = new FeedingDataSample(timestamp,NigelID, type);
                FeedingDataSamples.add(fed);
            }
        }
        return FeedingDataSamples;
    }

    /**
     * Parses a JSON-formatted date string to a Unix timestamp
     *
     * @param timestampString The JSON date string to be parsed
     * @return The Unix timestamp representing the parsed date, or -1 if parsing fails
     * @throws IllegalArgumentException If the provided timestampString is in an invalid format
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static long parseTimestampToUnix(String timestampString) {
        SimpleDateFormat jsonDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        jsonDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = jsonDateFormat.parse(timestampString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception
            return -1; // Return a meaningful default value or handle the error appropriately
        }
    }

    /**Getters*/
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Map<Integer, Baby> getDataset() {
        return dataset;
    }

    public void setDataset(Map<Integer, Baby> dataset) {
        this.dataset = dataset;
    }
}
