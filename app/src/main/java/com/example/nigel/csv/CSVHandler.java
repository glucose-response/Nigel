package com.example.nigel.csv;

import android.content.Context;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CSVHandler {

    public static List<String[]> readCSVFromAssets(Context context, String fileName) {
        List<String[]> resultList = null;

        try {
            // Open an InputStream to the file in assets
            Log.d("CSVHandler", "Reading CSV: " + fileName);
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader isr = new InputStreamReader(inputStream);

            // Use the OpenCSV CSVReader with the InputStreamReader
            CSVReader csvReader = new CSVReader(isr);

            // Read all data at once
            resultList = csvReader.readAll();
            Log.d("CSVHandler", "Size of list: " + resultList.size());
            // Delete empty rows
            for (String[] result : resultList) {
                if (result[0].equals("")) {
                    Log.d("CSVHandler", "Empty row found");
                    resultList.remove(result);
                }
            }
            resultList.remove(0); // Remove the header row
            // Close the reader to prevent memory leaks
            csvReader.close();
            Log.d("CSVHandler", "Reduced size of list: " + resultList.size());


        } catch (IOException | CsvException e) {
            e.printStackTrace();  // Handle exceptions properly in your app
        }

        return resultList;
    }
}
