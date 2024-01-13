package com.example.nigel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



public class LoginActivity extends AppCompatActivity implements LoginBufferFragment.OnFragmentInteractionListener
{
    private ConstraintLayout mContentMain;
    private AccountSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentMain = findViewById(R.id.activity_main);
        this.settings = (AccountSettings) this.getApplication();

        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),new LoginBufferFragment())
                .commit();
        // main login fragment is launched from this buffer fragment after checking if there is any account logged in
    }


    private void displayFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),fragment)
                .commit();
    }
    @Override
    public void onLaunchLoginFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),new SingleAccountModeFragment())
                .commit();
    }
}
