package com.example.nigel.test;

import static org.junit.Assert.*;

import com.example.nigel.AccountSettings;
import com.microsoft.identity.client.IAccount;

import org.junit.Before;
import org.junit.Test;

public class AccountSettingsTest {

    private AccountSettings accountSettings;

    @Before
    public void setUp() {
        accountSettings = new AccountSettings();
    }

    @Test
    public void testGetmAccount() {
        assertNull(accountSettings.getmAccount()); // Assuming default is null
    }

    @Test
    public void testGetAuthenticationResult() {
        assertNull(accountSettings.getAuthenticationResult()); // Assuming default is null
    }

    @Test
    public void testGetmSingleAccountApp() {
        assertNull(accountSettings.getmSingleAccountApp()); // Assuming default is null
    }
}

