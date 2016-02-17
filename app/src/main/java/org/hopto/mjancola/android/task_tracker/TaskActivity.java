package org.hopto.mjancola.android.task_tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class TaskActivity extends Activity
{

    private TaskDataSource datasource;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.task_main );

        datasource = new TaskDataSource(this);
        datasource.open();
    }



    public static Intent createIntent( Context context )
    {
        return new Intent( context, TaskActivity.class );
    }

    public TaskDataSource getDatasource()
    {
        return datasource;
    }

}
