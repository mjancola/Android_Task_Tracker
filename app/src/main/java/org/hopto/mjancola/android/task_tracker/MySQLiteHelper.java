package org.hopto.mjancola.android.task_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper
{

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "_id";
    public static final String COLUMN_TASK_NAME = "name";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_INTERVAL = "interval";
    public static final String COLUMN_TASK_LAST_COMPLETED = "last_completed";


    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
        + TABLE_TASKS + "("
        + COLUMN_TASK_ID + " integer primary key autoincrement, "
        + COLUMN_TASK_NAME + " text not null, "
        + COLUMN_TASK_DESCRIPTION + " text not null, "
        + COLUMN_TASK_INTERVAL + " integer not null, "
        + COLUMN_TASK_LAST_COMPLETED + " integer not null);";

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w( MySQLiteHelper.class.getName(),
             "Upgrading database from version " + oldVersion + " to "
             + newVersion + ", which will destroy all old data" );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

}