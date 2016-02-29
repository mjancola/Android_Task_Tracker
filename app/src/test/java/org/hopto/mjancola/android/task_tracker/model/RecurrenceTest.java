package org.hopto.mjancola.android.task_tracker.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by mjancola on 2/20/16.
 */
public class RecurrenceTest {

    private static final Long TEST_COMPLETED = 99l;
    private static final Long TEST_NEVER_COMPLETED = 0l;
    private static final Integer TEST_VALUE_ONE = 1;
    private static final int TEST_DAYOFWEEK = Calendar.MONDAY;
    private static final Integer TEST_DAYOFMONTH = 1;
    private static final Integer TEST_DAYOFYEAR = 0101;  // jan 1
    private static final Long TEST_ONE_HOUR = 1000l*60l*60l;
    private static ArrayList<Integer> TEST_ARRAY_1 = new ArrayList<Integer>();

    @Before
    public void setUp() {
        TEST_ARRAY_1.add(TEST_VALUE_ONE);
    }

    @Test
    public void testNotNull() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, null, null);
        assertNotNull("new recurrence shall not be null", testRecurrence);
    }

    @Test
    public void testValidConstruction() {
        Recurrence testRecurrence;
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, null, TEST_COMPLETED);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, TEST_ARRAY_1, TEST_COMPLETED);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weekly, TEST_ARRAY_1, TEST_COMPLETED);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, TEST_ARRAY_1, TEST_COMPLETED);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Yearly, TEST_ARRAY_1, TEST_COMPLETED);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Days, TEST_ARRAY_1, TEST_COMPLETED);
        assertNotNull("valid contruction failed", testRecurrence);
    }

    @Test
    public void testEmptyLastCompleted() {
        Recurrence testRecurrence;
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, null, null);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, TEST_ARRAY_1, null);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weekly, TEST_ARRAY_1, null);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, TEST_ARRAY_1, null);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Yearly, TEST_ARRAY_1, null);
        testRecurrence = new Recurrence(Recurrence.RecurrenceType.Days, TEST_ARRAY_1, null);
        assertNotNull("contruction with empty last completed should be ok", testRecurrence);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullType() {
        Recurrence testRecurrence = new Recurrence(null, TEST_ARRAY_1, TEST_COMPLETED);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullValue() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, null, TEST_COMPLETED);
    }

    @Test
    public void testNullValueOkForDaily() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, null, TEST_COMPLETED);
        assertNotNull("Daily recurrence does not require a value", testRecurrence);
    }

    @Test
    public void testGetType() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, TEST_ARRAY_1, TEST_COMPLETED);
        assertEquals("getType should return type", Recurrence.RecurrenceType.Monthly, testRecurrence.getType());
    }

    @Test
    public void testGetValue() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, TEST_ARRAY_1, TEST_COMPLETED);
        assertTrue("getValue should return value", TEST_ARRAY_1.equals(testRecurrence.getValue()));
    }

    @Test
    public void testGetCompleted() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, TEST_ARRAY_1, TEST_COMPLETED);
        assertEquals("getCompleted should return last completed", TEST_COMPLETED, testRecurrence.getLastCompleted());
    }

    @Test
    public void testNullLastCompleted() {
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, TEST_ARRAY_1, null);
        assertEquals("getCompleted should return 0 if not specified", TEST_NEVER_COMPLETED, testRecurrence.getLastCompleted());
    }

    @Test
    public void testComplete() {

    }

    @Test
    public void testDailyOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -25);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, null, prevMS);
        assertTrue("daily overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testDailyNotOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -23);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Daily, null, prevMS);
        assertFalse("daily overdue should NOT be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testWeeklyOverdue() {
        // due Mon/Thurs
        // set date to Thurs, set last completed to Tues (AFTER NOW, just to make sure we are not looking at times)
        Calendar testDate = new GregorianCalendar(2016, Calendar.FEBRUARY, 25, 11, 01);  // this is THUR
        Calendar lastCompleted = new GregorianCalendar(2016, Calendar.FEBRUARY, 23, 11, 02);  // TUES
        Long prevMS = lastCompleted.getTimeInMillis();

        ArrayList<Integer> testWeekly = new ArrayList<Integer>();
        testWeekly.add(Calendar.MONDAY);
        testWeekly.add(Calendar.THURSDAY);
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weekly, testWeekly, prevMS);
        testRecurrence.setTestTimeMS(testDate.getTimeInMillis());
        assertTrue("weekly overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testWeeklyNotOverdue() {
        // due Mon/Thurs
        // set date to Wed, set last completed to Mon
        Calendar testDate = new GregorianCalendar(2016, Calendar.FEBRUARY, 24);  // this is WED
        Calendar lastCompleted = new GregorianCalendar(2016, Calendar.FEBRUARY, 22);  // MON
        Long prevMS = lastCompleted.getTimeInMillis();

        ArrayList<Integer> testWeekly = new ArrayList<Integer>();
        testWeekly.add(Calendar.MONDAY);
        testWeekly.add(Calendar.THURSDAY);
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weekly, testWeekly, prevMS);
        testRecurrence.setTestTimeMS(testDate.getTimeInMillis());
        assertFalse("weekly should NOT be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testWeeklyWrapOverdue() {
        // due Mon/Thurs
        // set date to SAT, set last completed to SUN, previous week
        Calendar testDate = new GregorianCalendar(2016, Calendar.FEBRUARY, 27);  // this is SAT
        Calendar lastCompleted = new GregorianCalendar(2016, Calendar.FEBRUARY, 14);  // SUN
        Long prevMS = lastCompleted.getTimeInMillis();

        ArrayList<Integer> testWeekly = new ArrayList<Integer>();
        testWeekly.add(Calendar.SATURDAY);
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weekly, testWeekly, prevMS);
        testRecurrence.setTestTimeMS(testDate.getTimeInMillis());
        assertTrue("weekly overdue wrapping week should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testMonthlyOverdue() {
        // due January/June
        // set date to June 1, last completed to Feb 15
        Calendar testDate = new GregorianCalendar(2016, Calendar.JUNE, 1, 10, 00) ;
        Calendar lastCompleted = new GregorianCalendar(2016, Calendar.FEBRUARY, 15, 11, 02);
        Long prevMS = lastCompleted.getTimeInMillis();

        ArrayList<Integer> testMonthly = new ArrayList<Integer>();
        testMonthly.add(Calendar.JANUARY);
        testMonthly.add(Calendar.JUNE);
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, testMonthly, prevMS);
        testRecurrence.setTestTimeMS(testDate.getTimeInMillis());
        assertTrue("monthly overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testMonthlyNotOverdue() {
        // due January/June
        // set date to May 31, last completed to Feb 15
        Calendar testDate = new GregorianCalendar(2016, Calendar.MAY, 31, 10, 00) ;
        Calendar lastCompleted = new GregorianCalendar(2016, Calendar.FEBRUARY, 15, 11, 02);
        Long prevMS = lastCompleted.getTimeInMillis();

        ArrayList<Integer> testMonthly = new ArrayList<Integer>();
        testMonthly.add(Calendar.JANUARY);
        testMonthly.add(Calendar.JUNE);
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, testMonthly, prevMS);
        testRecurrence.setTestTimeMS(testDate.getTimeInMillis());
        assertFalse("monthly should NOT be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testMonthlyWithWrapOverdue() {
        // due January/June
        // set date to Jan 1, last completed June 15, 2 years ago
        Calendar testDate = new GregorianCalendar(2016, Calendar.JANUARY, 1, 10, 00) ;
        Calendar lastCompleted = new GregorianCalendar(2014, Calendar.JUNE, 15, 11, 02);
        Long prevMS = lastCompleted.getTimeInMillis();

        ArrayList<Integer> testMonthly = new ArrayList<Integer>();
        testMonthly.add(Calendar.JANUARY);
        testMonthly.add(Calendar.JUNE);
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Monthly, testMonthly, prevMS);
        testRecurrence.setTestTimeMS(testDate.getTimeInMillis());
        assertTrue("monthly overdue with wrap should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testYearlyOverdue() {

    }

    @Test
    public void testDaysOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.add(Calendar.HOUR, -1);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Days, TEST_ARRAY_1, prevMS);
        assertTrue("daily overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testDaysNotOverdue() {
        Calendar cal = Calendar.getInstance();
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Days, TEST_ARRAY_1, prevMS);
        assertFalse("daily overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testWeeksOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.add(Calendar.HOUR, -1);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weeks, TEST_ARRAY_1, prevMS);
        assertTrue("weekly overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testWeeksNotOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.add(Calendar.HOUR, 1);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Weeks, TEST_ARRAY_1, prevMS);
        assertFalse("weekly overdue should not be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testMonthsOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.HOUR, -1);  //exceed due by 1 hour
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Months, TEST_ARRAY_1, prevMS);
        assertTrue("monthly overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testMonthsNotOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.HOUR, 1);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Months, TEST_ARRAY_1, prevMS);
        assertFalse("monthly overdue should NOT be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testYearsOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.HOUR, -1); //exceed due by 1 hour
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Years, TEST_ARRAY_1, prevMS);
        assertTrue("yearly overdue should be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

    @Test
    public void testYearsNotOverdue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.HOUR, 1);
        Long prevMS = cal.getTimeInMillis();
        Recurrence testRecurrence = new Recurrence(Recurrence.RecurrenceType.Years, TEST_ARRAY_1, prevMS);
        assertFalse("yearly overdue should NOT be due=" + testRecurrence.dueDeltaMS(), (testRecurrence.dueDeltaMS() < 0));
    }

}
