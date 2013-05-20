package org.hopto.mjancola.android.task_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.hopto.mjancola.android.task_tracker.adapter.TaskListAdapter;

import java.util.List;

public class OverdueActivity extends Activity
{

    private ListView listView1;
    private TaskDataSource datasource;


    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.overdue);

        datasource = new TaskDataSource( this );
        datasource.open();

        listView1 = (ListView)findViewById(R.id.listView1);

        View header = (View)getLayoutInflater().inflate(R.layout.main_header, null);
        listView1.addHeaderView(header);

    }

    private void updateData()
    {
        List<Task> values = datasource.getAllTasks();

         // TODO filter for only the ones that are actually overdue
 //        for (Task t in values)
 //        {
 //            if
 //        }

         Task[] overdue_tasks = values.toArray(new Task[values.size()]);

         final TaskListAdapter adapter = new TaskListAdapter(this,
           R.layout.list_row, overdue_tasks);


         listView1.setAdapter(adapter);

         listView1.setOnItemClickListener( new AdapterView.OnItemClickListener()
         {
             public void onItemClick( AdapterView<?> parent, View view, int position, long id )
             {

                 Intent editTaskIntent = TaskActivity.createIntent( parent.getContext() );
                 editTaskIntent.putExtra( Task.INTENT_ID, adapter.getItem( position-1 ).getId() );  // subtract one for the header

                 startActivity( editTaskIntent );
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
