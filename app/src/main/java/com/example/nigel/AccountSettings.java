package com.example.nigel;

import android.app.Application;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

import java.io.Serializable;

public class AccountSettings extends Application {
    private IAccount mAccount;
    private IAuthenticationResult authenticationResult;
    private ISingleAccountPublicClientApplication mSingleAccountApp;

    /** Get variables */
    public IAccount getmAccount() {
        return mAccount;
    }
    public IAuthenticationResult getAuthenticationResult(){
        return authenticationResult;
    }
    public ISingleAccountPublicClientApplication getmSingleAccountApp(){
        return mSingleAccountApp;
    }

    /** Set Variables */
    public void setmAccount(IAccount mAccount) {
        this.mAccount = mAccount;  // Could be a null
    }

    public void setAuthenticationResult(IAuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }
    public void setmSingleAccountApp(ISingleAccountPublicClientApplication mSingleAccountApp) {
        this.mSingleAccountApp = mSingleAccountApp;
    }
}
