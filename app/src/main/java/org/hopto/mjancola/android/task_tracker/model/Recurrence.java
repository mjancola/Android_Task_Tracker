package org.hopto.mjancola.android.task_tracker.model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mjancola on 2/19/16.
 */


public class Recurrence {

    // NOTE, uses Java Calendar's DayOfWeek (Sun=1 -> Sat=7)

    public static enum RecurrenceType {Daily, Weekly, Monthly, Yearly, Days, Weeks, Months, Years};

    private RecurrenceType type;
    private ArrayList<Integer> value;
    private long lastCompleted;
    private Long testTimeMS; // MS since epoch - cached here to facilitate testing

    /**
     * Constructor for Recurrence type
     *
     * @param type enum Recurrence type
     * @param value depends on type
     *   Type daily -> value is ignored
     *   Type weekly -> array of Calendar.SUNDAY, MONDAY... etc
     *   Type monthly-> array of Calendar.JANUARY, FEBRUARY... etc
     *   Type yearly -> array of day of year (represented numerically as MMDD)
     *   Type Days, Weeks, Months, Years -> array(0) is integer number of items, for example every 3 months
     * @param lastCompleted long value ms since epoch, defaults to 0 if not specified
     */
    public Recurrence(RecurrenceType type, ArrayList<Integer> value, Long lastCompleted){
        if ((type == null) || (!type.equals(RecurrenceType.Daily) && value == null)) {
            throw new IllegalArgumentException("type and value required");
        }

        this.type = type;
        this.value = value;
        this.testTimeMS = null;
        if (lastCompleted != null) {
            this.lastCompleted = lastCompleted;
        } else {
            this.lastCompleted = 0l;
        }
    }

    // package private for testing
    void setTestTimeMS(long timeMS) {
        this.testTimeMS = timeMS;
    }

    /**
     * get system time, if test time not set
     * @return long time in MS
     */
    private long getTestTimeMS() {
        if (this.testTimeMS != null) {
            return this.testTimeMS;
        } else {
            return System.currentTimeMillis();
        }
    }

    /**
     * calculate how many MS until due
     * returns negative if overdue
     * @return
     */
    public Long dueDeltaMS() {
        Long now = getTestTimeMS(); // System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        Long dueDate = 0l;

        switch (type) {
            case Monthly:
                cal.setTimeInMillis(lastCompleted);
                int lastMonth = cal.get(Calendar.MONTH);
                int monthsDueAfterLast;
                // look for next month
                for (monthsDueAfterLast=1; monthsDueAfterLast<13; monthsDueAfterLast++) {  // max of 12 months available
                    if (this.getValue().contains((lastMonth+monthsDueAfterLast) % 7)) {
                        // found next month
                        break;
                    }
                }
                cal.add(Calendar.MONTH, monthsDueAfterLast);
                // adjust time to first day and minute of month
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR, 0);  // set the time to 12:01a
                cal.set(Calendar.MINUTE, 1);
                dueDate = cal.getTimeInMillis();
                break;
            case Weekly:
                cal.setTimeInMillis(lastCompleted);
                int lastDay = cal.get(Calendar.DAY_OF_WEEK);
                int daysDueAfterLast;
                // look for next day
                for (daysDueAfterLast=1; daysDueAfterLast<8; daysDueAfterLast++) {  // max of 7 days available
                    if (this.getValue().contains((lastDay+daysDueAfterLast) % 7)) {
                        // found next day
                        break;
                    }
                }
                cal.add(Calendar.DAY_OF_YEAR, daysDueAfterLast);
                cal.set(Calendar.HOUR, 0);  // set the time to 12:01a
                cal.set(Calendar.MINUTE, 1);
                dueDate = cal.getTimeInMillis();
                break;

            case Daily:
                cal.setTimeInMillis(lastCompleted);
                cal.add(Calendar.DAY_OF_YEAR, 1);  // by definition, daily is every day
                dueDate = cal.getTimeInMillis();
                break;
            case Years:
                cal.setTimeInMillis(lastCompleted);
                cal.add(Calendar.YEAR, value.get(0));
                dueDate = cal.getTimeInMillis();
                break;
            case Months:
                cal.setTimeInMillis(lastCompleted);
                cal.add(Calendar.MONTH, value.get(0));
                dueDate = cal.getTimeInMillis();
                break;
            case Weeks:
                cal.setTimeInMillis(lastCompleted);
                cal.add(Calendar.WEEK_OF_YEAR, value.get(0));
                dueDate = cal.getTimeInMillis();
                break;
            case Days:
                cal.setTimeInMillis(lastCompleted);
                cal.add(Calendar.DAY_OF_YEAR, value.get(0));
                dueDate = cal.getTimeInMillis();
                break;
            default:
                throw new IllegalArgumentException("unrecognized recurrence type");

        }

        return dueDate - now;
    }

    public Recurrence.RecurrenceType getType() {
        return type;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }

    public Long getLastCompleted() {
        return lastCompleted;
    }

}
