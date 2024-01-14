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
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import org.json.JSONObject;


public class LogoutFragment extends Fragment {
    private static final String TAG = SingleAccountModeFragment.class.getSimpleName();
    private Button signOutButton;
    private TextView logTextView;

    private AccountSettings settings;
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Get AccountSettings from global
        this.settings = (AccountSettings) requireActivity().getApplication();
        this.mSingleAccountApp = settings.getmSingleAccountApp();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.logout_test, container, false);
        initializeUI(view);
        callGraphAPI(settings.getAuthenticationResult());
        return view;
    }

        /**
         * Initializes UI variables and callbacks.
         */
    private void initializeUI(@NonNull final View view) {
        this.signOutButton = view.findViewById(R.id.logoutButton);
        this.logTextView = view.findViewById(R.id.txt_logs);


        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mSingleAccountApp != null) {
                    mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                        /**
                         * Removes the signed-in account and cached tokens from this app (or device, if the device is in shared mode).
                         */
                        @Override
                        public void onSignOut() {
                            settings.setmAccount(null);
                            updateUI();
                            showToastOnSignOut();

                            if (getActivity() != null) {
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onError(@NonNull MsalException exception) {
                            displayError(exception);
                        }
                    });
                }
            }
        });
    }

    private void updateUI() {
        signOutButton.setEnabled(settings.getmAccount() != null);
    }

    private void callGraphAPI(final IAuthenticationResult authenticationResult) {
        if (authenticationResult != null && authenticationResult.getAccessToken() != null) {
            Log.e(TAG, "Authentication result success");
            MSGraphRequestWrapper.callGraphAPIUsingVolley(
                    requireContext(),
                    authenticationResult.getAccessToken(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            /* Successfully called graph, process data and send to UI */
                            Log.d(TAG, "Response: " + response.toString());
                            displayUserInfo(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error: " + error.toString());
                            displayError(error);
                        }
                    });
        } else {
            // Handle the case where authenticationResult is null
            Log.e(TAG, "Authentication result is null");
        }
    }
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
     * Display "Signed out" after logout button clicked
     */
    private void showToastOnSignOut() {
        final String signOutText = "Signed Out.";
        Toast.makeText(getContext(), signOutText, Toast.LENGTH_SHORT)
                .show();
    }
}
