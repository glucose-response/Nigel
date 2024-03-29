package com.example.nigel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.FragmentTransaction;

import com.microsoft.identity.client.IAccount;


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

    /**
     * Launching a fragment from another fragment as an implementation
     */
    @Override
    public void onLaunchLoginFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),new SingleAccountModeFragment())
                .commit();
    }
}
