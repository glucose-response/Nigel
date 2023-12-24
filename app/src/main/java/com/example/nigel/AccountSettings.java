package com.example.nigel;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

public class AccountSettings {
    private IAccount mAccount;
    private IAuthenticationResult authenticationResult;
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private boolean LoginState;

    /* Get variables */
    public IAccount getmAccount() {
        return mAccount;
    }
    public IAuthenticationResult getAuthenticationResult(){
        return authenticationResult;
    }
    public ISingleAccountPublicClientApplication getmSingleAccountApp(){
        return mSingleAccountApp;
    }
    public boolean getLoginState(){
        return LoginState;
    }

    /* Set Variables */
    public void setmAccount(IAccount mAccount) {
        this.mAccount = mAccount;  // Could be a null
    }

    public void setAuthenticationResult(IAuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }

    public void setmSingleAccountApp(ISingleAccountPublicClientApplication mSingleAccountApp) {
        this.mSingleAccountApp = mSingleAccountApp;
    }

    public void setLoginState(boolean loginState) {
        this.LoginState = loginState;
    }
}
