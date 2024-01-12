package com.example.nigel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



public class LoginActivity extends AppCompatActivity
{
    private ConstraintLayout mContentMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentMain = findViewById(R.id.activity_main);
        displayFragment(new SingleAccountModeFragment());
    }


    private void displayFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),fragment)
                .commit();
    }
}
