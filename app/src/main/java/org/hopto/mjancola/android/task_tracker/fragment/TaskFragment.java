package org.hopto.mjancola.android.task_tracker.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.hopto.mjancola.android.task_tracker.R;
import org.hopto.mjancola.android.task_tracker.Task;
import org.hopto.mjancola.android.task_tracker.TaskActivity;
import org.hopto.mjancola.android.task_tracker.TaskDataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TaskFragment extends Fragment
{
    private static final String TAG = "TaskFragment";
    private TextView mode;
    private Button   createButton;
    private Button   cancelButton;
    private Button deleteButton;
    private EditText taskName;
    private EditText taskDescription;
    private EditText taskInterval;
    private EditText taskLastCompleted;

    private SimpleDateFormat formatter;
    private Calendar completedDate = Calendar.getInstance();

    private int  day;
    private int  month;
    private int  year;
    private Long task_id;

    TaskDataSource dataSource;
    private Task oldTask; // if applicable


    @Override
    public View onCreateView( LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState )
    {
        View addTaskView = inflater.inflate( R.layout.task,
                                             container,
                                             false );
        mode = (TextView) addTaskView.findViewById( R.id.task_mode );
        createButton = (Button) addTaskView.findViewById( R.id.add_task_button );
        cancelButton = (Button) addTaskView.findViewById( R.id.add_task_cancel );
        deleteButton = (Button) addTaskView.findViewById( R.id.deleteTask);

        taskName = (EditText) addTaskView.findViewById( R.id.task_name );
        taskDescription = (EditText) addTaskView.findViewById( R.id.task_description );
        taskInterval = (EditText) addTaskView.findViewById( R.id.task_interval );
        taskLastCompleted = (EditText) addTaskView.findViewById( R.id.task_last_completed );

        // check if this is edit or create


        formatter = new SimpleDateFormat( "dd MMM yyyy" );

        return addTaskView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated( savedInstanceState );
        createButton.setOnClickListener( new createClickListener() );
        cancelButton.setOnClickListener( new cancelClickListener() );
        deleteButton.setOnClickListener( new deleteClickListener() );
        taskLastCompleted.setOnClickListener( new dateClickListener() );

        // grab a handle to the datasource
        dataSource = ((TaskActivity) getActivity()).getDatasource();


        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null)
        {
            // this is edit - prepopulate values and set a flag
            deleteButton.setVisibility(View.VISIBLE);
            task_id = extras.getLong( Task.INTENT_ID );
            if (task_id != null)
            {
                mode.setText( getActivity().getString( R.string.TASK_EDIT_TITLE ) );
                createButton.setText("Update");
                // get the old task
                List<Task> tasks = dataSource.getAllTasks();
                for (Task t : tasks)
                {
                    if (t.getId() == task_id)
                    {
                        // found it, get out
                        oldTask = t;
                        taskName.setText(oldTask.getName());
                        taskDescription.setText(oldTask.getDescription());
                        taskInterval.setText(String.valueOf(oldTask.getInterval()));
                        Calendar oldLastCompleted = Calendar.getInstance();
                        oldLastCompleted.setTimeInMillis( oldTask.getLast_completed() );
                        taskLastCompleted.setText( formatter.format( oldLastCompleted.getTime() ) );
                        break;
                    }
                }

            }

        } else {
            // add
            deleteButton.setVisibility(View.GONE);
        }

    }

    public void chooseDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                                        getActivity(), setDateCallback,
                                                        completedDate.get( Calendar.YEAR ),
                                                        completedDate.get( Calendar.MONTH ),
                                                        completedDate.get( Calendar.DAY_OF_MONTH ) );
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener setDateCallback = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet( DatePicker view, int year, int monthOfYear,
                               int dayOfMonth )
        {
            completedDate.set( Calendar.YEAR, year );
            completedDate.set( Calendar.MONTH, monthOfYear );
            completedDate.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            taskLastCompleted.setText( formatter.format( completedDate.getTime() ) );
        }
    };


    private class createClickListener implements View.OnClickListener
    {
        @Override
        public void onClick( View view )
        {

            // Save the new Task to the database
            if (!(taskInterval.getText().toString().isEmpty() || taskName.getText().toString().isEmpty())) {
                dataSource.createTask( taskName.getText().toString(),
                                   taskDescription.getText().toString(),
                                   Long.valueOf( taskInterval.getText().toString() ),
                                   completedDate.getTimeInMillis() );
                // task successfully created
                // if this was an edit, delete the old one
                if (mode.getText().equals(getActivity().getString(R.string.TASK_EDIT_TITLE)))
                {
                    dataSource.deleteTask(oldTask);
                }
                getActivity().finish();
            } else {
                // task not created
                Toast.makeText(getActivity(), "Error, check input fields", 2000).show();
                Log.e(TAG, "Task not created");
            }


        }
    }

    private class deleteClickListener implements View.OnClickListener
    {
        @Override
        public void onClick( View view )
        {
            // redundant check
            if (mode.getText().equals(getActivity().getString(R.string.TASK_EDIT_TITLE))) {
                dataSource.deleteTask(oldTask);
            }
            getActivity().finish();
        }
    }

    private class cancelClickListener implements View.OnClickListener
    {
        @Override
        public void onClick( View view )
        {
            getActivity().finish();
        }
    }


    private class dateClickListener implements View.OnClickListener
    {
        @Override public void onClick( View view )
        {
            chooseDate();
        }
    }
}
