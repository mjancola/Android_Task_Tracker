package org.hopto.mjancola.android.task_tracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Task {
    public static final String INTENT_ID  = "INTENT_ID";
    public static final int DUE_BUFFER_MS = 1000*60*60*24*7; // 7 days
    public static final String EDIT_TITLE = "Edit Task";
    private long   id;
    private String name;
    private String description;
    private long   interval;
    private long   last_completed;

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public long getInterval()
    {
        return interval;
    }

    public void setInterval( long interval )
    {
        this.interval = interval;
    }

    public long getLast_completed()
    {
        return last_completed;
    }

    public void setLast_completed( long last_completed )
    {
        this.last_completed = last_completed;
    }


    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString()
    {
        Calendar calendarLastCompleted = Calendar.getInstance();
        calendarLastCompleted.setTimeInMillis( last_completed );
        SimpleDateFormat formatter = new SimpleDateFormat( "dd MMM yyyy" );
        String due = formatter.format(calendarLastCompleted.getTime());

        return name + " due:" + due;
    }

    public boolean isValid() {
        return false;
    }

    public boolean isDue() {
        // assumes last completed exists
        /* long now = System.currentTimeMillis();
        long last = getLast_completed();
        long interval = getInterval();
        long intervalMS = interval*1000*60*60*24;
        long due = last + intervalMS;
        long showIfAfter = now-DUE_BUFFER_MS;
        if (showIfAfter > due) { */
        //if ((last + getInterval()) > nowDays - 7l) {
        if ((getLast_completed() + (getInterval()*1000*60*60*24)) < (System.currentTimeMillis() - DUE_BUFFER_MS)) {
            return true;
        } else {
            return false;
        }

    }
}