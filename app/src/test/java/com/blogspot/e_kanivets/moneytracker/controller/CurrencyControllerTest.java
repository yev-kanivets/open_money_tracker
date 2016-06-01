package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 4/22/16.
 *
 * @author Evgenii Kanivets
 */
public class CurrencyControllerTest {

    @Test
    public void testReadDefaultCurrency() throws Exception {
        AccountController accountMock = Mockito.mock(AccountController.class);
        PreferenceController prefsMock = Mockito.mock(PreferenceController.class);
        CurrencyController currencyController = new CurrencyController(accountMock, prefsMock);

        Mockito.when(prefsMock.readDefaultCurrency()).thenReturn(null);
        Mockito.when(accountMock.readDefaultAccount()).thenReturn(null);
        assertEquals("NON", currencyController.readDefaultCurrency());

        Account account = new Account(1, "a1", 100, "ACM", decimals);
        Mockito.when(prefsMock.readDefaultCurrency()).thenReturn(null);
        Mockito.when(accountMock.readDefaultAccount()).thenReturn(account);
        assertEquals(account.getCurrency(), currencyController.readDefaultCurrency());

        Mockito.when(prefsMock.readDefaultCurrency()).thenReturn("SOS");
        Mockito.when(accountMock.readDefaultAccount()).thenReturn(account);
        assertEquals("SOS", currencyController.readDefaultCurrency());
    }
}