package com.example.nigel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.data.Entry;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private AccountSettings settings;
    private ConstraintLayout mContentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mContentMain = findViewById(R.id.main_layout);

        displayFragment(new MainBabyFragment());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account_settings) {
            displayFragment(new LogoutFragment());
            return true;
        }
        if (id == R.id.home_button) {
            displayFragment(new MainBabyFragment());
            return true;
        }
        if (id == R.id.upload_download) {
            displayFragment(new UploadDownloadFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(), fragment)
                .addToBackStack(null)  // Add this line to add the transaction to the back stack
                .commit();
    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Check if there are any fragments in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the top fragment from the back stack
            fragmentManager.popBackStack();
        } else {
            // If no fragments in the back stack, let the system handle the back button
            super.onBackPressed();
        }
    }
}
