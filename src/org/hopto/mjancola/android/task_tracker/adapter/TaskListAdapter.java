package org.hopto.mjancola.android.task_tracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.hopto.mjancola.android.task_tracker.R;
import org.hopto.mjancola.android.task_tracker.Task;

public class TaskListAdapter extends ArrayAdapter<Task>
{

    Context context;
    int     layoutResourceId;
    Task data[] = null;

    public TaskListAdapter( Context context, int layoutResourceId, Task[] data )
    {
        super( context, layoutResourceId, data );
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TaskHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.do_task_icon);
            holder.txtTitle = (TextView)row.findViewById( R.id.task_name);

            row.setTag(holder);
        }
        else
        {
            holder = (TaskHolder)row.getTag();
        }

        Task task = data[position];
        holder.txtTitle.setText(task.getName());
        //holder.imgIcon.setImageResource(some image);

        return row;
    }

    static class TaskHolder
    {
        ImageView imgIcon;
        TextView  txtTitle;
    }
}