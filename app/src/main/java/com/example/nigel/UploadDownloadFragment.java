package com.example.nigel;

// Used Chatgpt

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class UploadDownloadFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.upload_download_test, container, false);

        // Find buttons by their IDs
        Button templateBtn = view.findViewById(R.id.btnDownloadTemplate);
        Button uploadBtn = view.findViewById(R.id.btnUploadData);
        Button allDatabtn = view.findViewById(R.id.btnDownloadAllData);

        // Set click listeners for each button
        templateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button1 click
                // You can add code here to perform an action when button1 is clicked
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button2 click
                // You can add code here to perform an action when button2 is clicked
            }
        });

        allDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button3 click
                // You can add code here to perform an action when button3 is clicked
            }
        });

        return view;
    }
}
