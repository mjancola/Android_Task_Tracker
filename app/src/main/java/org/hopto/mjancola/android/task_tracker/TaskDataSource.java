package org.hopto.mjancola.android.task_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskDataSource
{

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_TASK_ID,
                                    MySQLiteHelper.COLUMN_TASK_NAME,
                                    MySQLiteHelper.COLUMN_TASK_DESCRIPTION,
                                    MySQLiteHelper.COLUMN_TASK_INTERVAL,
                                    MySQLiteHelper.COLUMN_TASK_LAST_COMPLETED
    };

    public TaskDataSource( Context context )
    {
        dbHelper = new MySQLiteHelper( context );
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public Task createTask(String name, String description, long interval, long last_completed)
    {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TASK_NAME, name);
        values.put(MySQLiteHelper.COLUMN_TASK_DESCRIPTION, description);
        values.put(MySQLiteHelper.COLUMN_TASK_INTERVAL, interval);
        values.put(MySQLiteHelper.COLUMN_TASK_LAST_COMPLETED, last_completed);
        long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null,
            values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
            allColumns, MySQLiteHelper.COLUMN_TASK_ID + " = " + insertId, null,
            null, null, null);
        cursor.moveToFirst();
        Task newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
      }

    public void deleteTask(Task Task)
    {
        long id = Task.getId();
        System.out.println("Task deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_TASKS, MySQLiteHelper.COLUMN_TASK_ID
                + " = " + id, null);
    }

    public List<Task> getAllTasks()
    {
        List<Task> Tasks = new ArrayList<Task>();


        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
            allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          Task Task = cursorToTask(cursor);
          Tasks.add(Task);
          cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return Tasks;
    }
    public List<Task> getDueTasks()
    {
        List<Task> Tasks = new ArrayList<Task>();


        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task Task = cursorToTask(cursor);
            if (Task.isDue()) {
                Tasks.add(Task);
            }
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return Tasks;
    }

    private Task cursorToTask(Cursor cursor) {
        Task Task = new Task();
        Task.setId(cursor.getLong(0));
        Task.setName(cursor.getString(1));
        Task.setDescription(cursor.getString(2));
        Task.setInterval(cursor.getLong(3));
        Task.setLast_completed(cursor.getLong(4));
    return Task;
    }
} 