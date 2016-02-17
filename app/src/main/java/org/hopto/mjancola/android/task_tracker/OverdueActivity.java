package org.hopto.mjancola.android.task_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.hopto.mjancola.android.task_tracker.adapter.TaskListAdapter;

import java.util.List;

public class OverdueActivity extends Activity
{

    private ListView listView1;
    private TaskDataSource datasource;
    private boolean all = false;  // flag to show all
    private TextView headerText;


    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overdue);

        datasource = new TaskDataSource( this );
        datasource.open();

        listView1 = (ListView)findViewById(R.id.listView1);

        View overdueHeader = (View)getLayoutInflater().inflate(R.layout.main_header, null);
        headerText = (TextView) overdueHeader.findViewById(R.id.SoonOrOverdueTasksHeader);
        listView1.addHeaderView(overdueHeader);

    }

    private void updateData()
    {
        List<Task> values;
        if (all) {
            values = datasource.getAllTasks();
            headerText.setText("All Tasks");
        } else {
            values = datasource.getDueTasks();
            headerText.setText("Soon or Overdue Tasks");
        }

        Task[] overdue_tasks = values.toArray(new Task[values.size()]);

        final TaskListAdapter adapter = new TaskListAdapter(this,
           R.layout.list_row, overdue_tasks);


         listView1.setAdapter(adapter);

         listView1.setOnItemClickListener( new AdapterView.OnItemClickListener()
         {
             public void onItemClick( AdapterView<?> parent, View view, int position, long id )
             {

                 if ((position-1) < 0) {
                     Log.d("TAG", "header clicked, toggle");
                     all = !all;
                     updateData();
                 } else {
                     Intent editTaskIntent = TaskActivity.createIntent(parent.getContext());
                     editTaskIntent.putExtra(Task.INTENT_ID, adapter.getItem(position - 1).getId());  // subtract one for the header

                     startActivity(editTaskIntent);
                 }
             }
         } );

    }


      // Will be called via the onClick attribute
      // of the buttons in main.xml
    public void onClick(View view) {

        switch (view.getId()) {
        case R.id.add:
            Intent addIntent = TaskActivity.createIntent( this );
            startActivity(addIntent);
          break;
        }
    }

    @Override
     protected void onResume() {
         super.onResume();
         updateData();
     }

     @Override
     protected void onPause() {
         //datasource.close();
         super.onPause();
     }


}
