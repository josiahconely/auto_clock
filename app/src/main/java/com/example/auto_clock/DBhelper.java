package com.example.auto_clock;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;




public class DBhelper extends SQLiteOpenHelper {

    //Table Name
    public static final String TABLE_NAME = "TimeLog";
    //Column Names
    public static final String COL_IN_TIME = "_in";
    public static final String COL_OUT_TIME = "_out";
    public static final String COL_LONGITUDE = "_longitude";
    public static final String COL_LATITUDE = "_latitude";
    static final String[] columns = new String[]{DBhelper.COL_IN_TIME,
            DBhelper.COL_OUT_TIME, DBhelper.COL_LONGITUDE,
            DBhelper.COL_LATITUDE};
    //Database Information
    private static final String DATABASE_NAME = "DataBase.db";
    private static final int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + COL_IN_TIME + " TEXT , "
            + COL_OUT_TIME + " TEXT , " + COL_LONGITUDE + " TEXT," + COL_LATITUDE + " TEXT);";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("DB Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        System.out.println("Table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        System.out.println("DB Updated");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
