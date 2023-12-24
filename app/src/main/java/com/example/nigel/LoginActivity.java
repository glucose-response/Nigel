package com.example.nigel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.microsoft.identity.client.IAccount;


public class LoginActivity extends AppCompatActivity
{
    private ConstraintLayout mContentMain;
    private SingleAccountModeFragment loginfragment;
    private LogoutFragment logoutFragment;
    private AccountSettings settings = new AccountSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentMain = findViewById(R.id.activity_main);
        settings.setLoginState(true);

        this.loginfragment = new SingleAccountModeFragment(settings);
        this.logoutFragment = new LogoutFragment(settings);

        if (settings.getLoginState()){
            displayFragment(loginfragment);
        }else {
            while (settings.getLoginState()==false){
                displayFragment(logoutFragment);
            }
            displayFragment(logoutFragment);
        }

    }


    private void displayFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(mContentMain.getId(),fragment)
                .commit();
    }
}
