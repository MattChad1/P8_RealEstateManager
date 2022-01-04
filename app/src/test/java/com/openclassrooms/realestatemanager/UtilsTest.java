package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Calendar;

public class UtilsTest {

    @Test
    public void convertDollarToEuro() {
        int entry = 10;
        int expected = 8;
        assertEquals(expected, Utils.convertDollarToEuro(entry));
    }

    @Test
    public void convertEuroToDollar() {
        int entry = 10;
        int expected = 12;
    }

    @Test
    public void getTodayDate() {
        String dateResult = Utils.getTodayDate();
        assertTrue(dateResult.matches("\\d{2}/\\d{2}/\\d{4}"));

        Calendar now = Calendar.getInstance();
        DecimalFormat mFormat= new DecimalFormat("00");
        assertEquals(mFormat.format(now.get(Calendar.DAY_OF_MONTH)) + "/" + mFormat.format((now.get(Calendar.MONTH) + 1)) + "/" + now.get(Calendar.YEAR) , dateResult);

    }
}