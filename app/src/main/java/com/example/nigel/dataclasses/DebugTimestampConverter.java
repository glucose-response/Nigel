package com.example.nigel.dataclasses;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DebugTimestampConverter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.ENGLISH);

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set your actual timezone
    }

    public static long convertToUnixTimestamp(String dateStr) {
        try {
            Date date = dateFormat.parse(dateStr);
            // Log.d("DebugTimestampConverter", "Original String: " + dateStr + " | Parsed Date: " + date + " | Timestamp: " + date.getTime());
            return date.getTime();
        } catch (ParseException e) {
            Log.e("DebugTimestampConverter", "Error parsing date: " + dateStr, e);
            return -1; // Return an invalid timestamp or handle this case as needed
        }
    }
}