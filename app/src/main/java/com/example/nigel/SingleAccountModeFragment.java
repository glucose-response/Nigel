package com.example.nigel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SignInParameters;
import com.microsoft.identity.client.exception.MsalException;

import java.util.Arrays;

/**
 * Implementation for 'Single account' mode.
 * Adapted the identity-platform specific methods from the sample provided by the tutorial :
 * "https://learn.microsoft.com/en-us/entra/identity-platform/tutorial-v2-android"
 */
public class SingleAccountModeFragment extends Fragment {
    private static final String TAG = SingleAccountModeFragment.class.getSimpleName();

    /* UI & Debugging Variables */
    private Button signInButton;
    private AccountSettings settings;


    /* Azure AD Variables */
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.login_test, container, false);
        initializeUI(view);
        this.settings = (AccountSettings) requireActivity().getApplication();

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(getContext(),
                R.raw.auth_config_single_account,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        /**
                         * This app assumes that the app is only going to support one account.
                         * This requires "account_mode" : "SINGLE" in the config json file.
                         **/
                        mSingleAccountApp = application;
                        settings.setmSingleAccountApp(application);
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.d(TAG, exception.toString());
                    }
                });

        return view;
    }



    /**
     * Initializes UI variables and callbacks.
     */
    private void initializeUI(@NonNull final View view) {
        signInButton = view.findViewById(R.id.loginButton);
       // this.logTextView = view.findViewById(R.id.txt_logs);

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mSingleAccountApp == null) {
                    return;
                }
                final SignInParameters signInParameters = SignInParameters.builder()
                        .withActivity(getActivity())
                        .withLoginHint(null)
                        .withScopes(Arrays.asList(getScopes()))
                        .withCallback(getAuthInteractiveCallback())
                        .build();
                mSingleAccountApp.signIn(signInParameters);
            }
        });
    }

    /**
     * Extracts a scope array from a text field
     */
    private String[] getScopes() {
        String txt = "User.Read openid";
        return txt.toLowerCase().split(" ");
    }

    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                // Log the time of this function
                Log.d(TAG, "TIME OF ON SUCCESS " + System.currentTimeMillis());
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "Authentication Result: " + authenticationResult.toString());
                Log.d(TAG, "Token: " + authenticationResult.getAccessToken());

                /* Update account */
                settings.setmAccount(authenticationResult.getAccount());
                settings.setAuthenticationResult(authenticationResult);
                mAccount = authenticationResult.getAccount();
                Log.d(TAG, "Account Updated: " + mAccount.toString());
                updateUI();

                /* Launch the MainActivity */
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();

            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /**
     * Updates UI based on the current account.
     */
    private void updateUI() {
        signInButton.setEnabled(mAccount == null);
    }

}