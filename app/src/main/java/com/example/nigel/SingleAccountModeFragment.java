package com.example.nigel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SignInParameters;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Implementation sample for 'Single account' mode.
 * <p>
 * If your app only supports one account being signed-in at a time, this is for you.
 * This requires "account_mode" to be set as "SINGLE" in the configuration file.
 * (Please see res/raw/auth_config_single_account.json for more info).
 * <p>
 * Please note that switching mode (between 'single' and 'multiple' might cause a loss of data.
 */
public class SingleAccountModeFragment extends Fragment {
    private static final String TAG = SingleAccountModeFragment.class.getSimpleName();

    /* UI & Debugging Variables */
    private Button signInButton;
    private TextView logTextView;
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
                        loadAccount();

                    }

                    @Override
                    public void onError(MsalException exception) {
                        displayError(exception);
                    }
                });

        return view;
    }



    /**
     * Initializes UI variables and callbacks.
     */
    private void initializeUI(@NonNull final View view) {
        signInButton = view.findViewById(R.id.loginButton);
        this.logTextView = view.findViewById(R.id.txt_logs);

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

    @Override
    public void onResume() {
        super.onResume();
        Log.println(Log.INFO, TAG, "ON RESUME CALL TIME: " + System.currentTimeMillis());
        /**
         * The account may have been removed from the device (if broker is in use).
         * Therefore, we want to update the account state by invoking loadAccount() here.
         */
        loadAccount();



    }

    /**
     * Extracts a scope array from a text field,
     * i.e. from "User.Read User.ReadWrite" to ["user.read", "user.readwrite"]
     */
    private String[] getScopes() {
        String txt = "User.Read openid";
        return txt.toLowerCase().split(" ");
    }

    /**
     * Load the currently signed-in account, if there's any.
     */
    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        // Check whether it is already signed in
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                settings.setmAccount(activeAccount);
                mAccount = settings.getmAccount();
                if (mAccount!=null) {
                    Log.d(TAG, "Account Updated: " + mAccount.toString());

                    // Check if there is a cached authentication result
                    mSingleAccountApp.acquireTokenSilentAsync(getScopes(), mAccount.getAuthority(), getAuthSilentCallback());
                }

//                updateUI();
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    showToastOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                displayError(exception);
            }
        });
    }

    /**
     * Callback used in for silent acquireToken calls.
     */
    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                settings.setmAccount(authenticationResult.getAccount());
                settings.setAuthenticationResult(authenticationResult);
                Log.d(TAG, "Successfully (silent) authenticated");
                Log.d(TAG, "Authentication Result: " + authenticationResult.toString());
                Log.d(TAG, "Token: " + authenticationResult.getAccessToken());

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }
        };
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
                startActivity(intent);

                /* call graph */
                // callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

//    /**
//     * Make an HTTP request to obtain MSGraph data
//     */
//    private void callGraphAPI(final IAuthenticationResult authenticationResult) {
//        MSGraphRequestWrapper.callGraphAPIUsingVolley(
//                getContext(),
//                authenticationResult.getAccessToken(),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        /* Successfully called graph, process data and send to UI */
//                        Log.d(TAG, "Response: " + response.toString());
//                        displayUserInfo(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "Error: " + error.toString());
//                        displayError(error);
//                    }
//                });
//    }

    //
    // Helper methods manage UI updates
    // ================================
    // displayGraphResult() - Display the graph response
    // displayError() - Display the graph response
    // updateSignedInUI() - Updates UI when the user is signed in
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    /**
     * Display the graph response
     */
    private void displayUserInfo(@NonNull final JSONObject userInfo) {
        try {
            String displayName = userInfo.getString("displayName");
            String mail = userInfo.getString("mail");
            String positionTitle = userInfo.getString("jobTitle");
            String userPrincipalName = userInfo.getString("userPrincipalName");
            String displayText = "Display Name: " + displayName + "\n" +
                    "Job Title: " + positionTitle + "\n" +
                    "Email: " + mail + "\n" +
                    "User Principal Name: " + userPrincipalName;
            logTextView.setText(displayText);

        } catch (Exception e) {
            Log.d(TAG, "Error processing user information: " + e.toString());
            displayError(new VolleyError("Error processing user information"));
        }
    }

    /**
     * Display the error message
     */
    private void displayError(@NonNull final Exception exception) {
        logTextView.setText(exception.toString());
    }

    /**
     * Updates UI based on the current account.
     */
    private void updateUI() {
        signInButton.setEnabled(mAccount == null);
    }

    /**
     * Updates UI when app sign out succeeds
     */
    private void showToastOnSignOut() {
        final String signOutText = "Signed Out.";
        Toast.makeText(getContext(), signOutText, Toast.LENGTH_SHORT)
                .show();
    }

}