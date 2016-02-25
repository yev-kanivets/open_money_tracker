package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.model.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class ReportTest {
    private String currency;
    private IExchangeRateProvider rateProvider;

    @Before
    public void setUp() throws Exception {
        currency = "UAH";
        rateProvider = new TestProvider();
    }

    @After
    public void tearDown() throws Exception {
        currency = null;
        rateProvider = null;
    }

    @Test
    public void testForNulls() throws Exception {
        IReport report;

        Period period = new Period(new Date(1), new Date());
        List<Record> recordList = new ArrayList<>();

        try {
            report = new Report(null, period, recordList, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new Report(currency, null, recordList, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new Report(currency, period, null, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new Report(currency, period, recordList, null);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new Report(null, null, null, null);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new Report(currency, period, recordList, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNotNull(report);
    }

    @Test
    public void testGetCurrency() throws Exception {
        Period period = new Period(new Date(1), new Date());
        List<Record> recordList = new ArrayList<>();

        IReport report = new Report(currency, period, recordList, rateProvider);

        assertEquals(currency, report.getCurrency());

        currency = "KHI";
        report = new Report(currency, period, recordList, rateProvider);

        assertEquals(currency, report.getCurrency());
    }

    @Test
    public void testGetPeriod() throws Exception {
        Period period = new Period(new Date(1), new Date());
        List<Record> recordList = new ArrayList<>();

        IReport report = new Report(currency, period, recordList, rateProvider);

        assertEquals(period, report.getPeriod());

        period = new Period(new Date(3), new Date(100));
        report = new Report(currency, period, recordList, rateProvider);

        assertEquals(period, report.getPeriod());
    }

    @Test
    public void testGetTotal() throws Exception {
        Period period = new Period(new Date(1), new Date());

        List<Record> recordList = new ArrayList<>();
        recordList.add(new Record(0, Record.TYPE_INCOME, "1", "1", 10, 1, "USD"));
        recordList.add(new Record(1, Record.TYPE_EXPENSE, "2", "1", 2, 2, "UAH"));
        recordList.add(new Record(2, Record.TYPE_INCOME, "3", "1", 5, 1, "UAH"));
        recordList.add(new Record(3, Record.TYPE_EXPENSE, "4", "1", 10, 2, "USD"));

        IReport report = new Report(currency, period, recordList, rateProvider);

        double expectedTotal = 10 * 4 - 2 + 5 - 10 * 4;
        assertEquals(expectedTotal, report.getTotal(), 0.0000000001);
    }

    @Test
    public void testGetTotalIncome() throws Exception {
        Period period = new Period(new Date(1), new Date());

        List<Record> recordList = new ArrayList<>();
        recordList.add(new Record(0, Record.TYPE_INCOME, "1", "1", 10, 1, "USD"));
        recordList.add(new Record(1, Record.TYPE_EXPENSE, "2", "1", 2, 2, "UAH"));
        recordList.add(new Record(2, Record.TYPE_INCOME, "3", "1", 5, 1, "UAH"));
        recordList.add(new Record(3, Record.TYPE_EXPENSE, "4", "1", 10, 2, "USD"));

        IReport report = new Report(currency, period, recordList, rateProvider);

        double expectedTotal = 10 * 4 + 5;
        assertEquals(expectedTotal, report.getTotalIncome(), 0.0000000001);
    }

    @Test
    public void testGetTotalExpense() throws Exception {
        Period period = new Period(new Date(1), new Date());

        List<Record> recordList = new ArrayList<>();
        recordList.add(new Record(0, Record.TYPE_INCOME, "1", "1", 10, 1, "USD"));
        recordList.add(new Record(1, Record.TYPE_EXPENSE, "2", "1", 2, 2, "UAH"));
        recordList.add(new Record(2, Record.TYPE_INCOME, "3", "1", 5, 1, "UAH"));
        recordList.add(new Record(3, Record.TYPE_EXPENSE, "4", "1", 10, 2, "USD"));

        IReport report = new Report(currency, period, recordList, rateProvider);

        double expectedTotal = -2 - 10 * 4;
        assertEquals(expectedTotal, report.getTotalExpense(), 0.0000000001);
    }

    @Test
    public void testGetSummary() throws Exception {

    }

    private static class TestProvider implements IExchangeRateProvider {

        @Nullable
        @Override
        public ExchangeRate getRate(@Nullable Record record) {
            if (record == null) return null;

            String fromCurrency = record.getCurrency();
            switch (fromCurrency) {
                case "USD":
                    return new ExchangeRate(1, "USD", "UAH", 4);

                case "AFN":
                    return new ExchangeRate(0, "AFN", "UAH", 3);

                default:
                    return null;

            }
        }
    }
}