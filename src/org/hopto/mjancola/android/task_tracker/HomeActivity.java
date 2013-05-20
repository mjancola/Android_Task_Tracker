package org.hopto.mjancola.android.task_tracker;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class HomeActivity extends ListActivity
{
    private TaskDataSource     datasource;
    private ArrayAdapter<Task> adapter;
    private Context            context;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        updateData();
    }

    private void updateData()
    {
        datasource = new TaskDataSource( this );
        datasource.open();

        List<Task> values = datasource.getAllTasks();
        adapter = new ArrayAdapter<Task>(this,
             android.R.layout.simple_list_item_1, values);

        ListView allTasks = (ListView) findViewById( R.id.listView1 );

        allTasks.setAdapter( adapter );

        allTasks.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            public void onItemClick( AdapterView<?> parent, View view, int position, long id )
            {

                Intent editTask = TaskActivity.createIntent( context );
                editTask.putExtra( "EDIT_TASK", adapter.getItem( position ).getId() );

                startActivity( editTask );
            }
        } );

    }
    
      // Will be called via the onClick attribute
      // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Task> adapter = (ArrayAdapter<Task>) getListAdapter();
        Task task = null;
        switch (view.getId()) {
        case R.id.add:
            Intent addIntent = TaskActivity.createIntent( this );
            startActivity(addIntent);
          break;
        case R.id.delete:
          if (getListAdapter().getCount() > 0) {
            task = (Task) getListAdapter().getItem(0);
            datasource.deleteTask(task);
            adapter.remove(task);
          }
          break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }
    
    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}