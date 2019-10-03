package com.wzl.scheduledemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    DBHelper(Context context) {
        super(context, "schedule.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table schedule(" +
                        "_id integer primary key autoincrement, " +
                        "course varchar(50), " +
                        "teacher varchar(50), " +
                        "location varchar(50), " +
                        "startweek integer, " +
                        "endweek integer, " +
                        "currentweek integer, " +
                        "startsection integer, " +
                        "endsection integer, " +
                        "whichweek integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
