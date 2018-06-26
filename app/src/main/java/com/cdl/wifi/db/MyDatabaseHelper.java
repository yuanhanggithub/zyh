//package com.cdl.wifi.db;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//
//public class MyDatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String CREATE_WIFI_TABLE = "create table wifiinfo (_id integer primary key autoincrement," +
//            "wifiname text," +
//            "wifipsd text," +
//            "wifitype  text);";
//
//    private Context mContext;
//    public static final String DB_WIFI_NAME = "wifisave.db";
//    public static final String DB_WIFI_TAB = "wifiinfo";
//
//    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//        mContext = context;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_WIFI_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists peopleinfo");
//        onCreate(db);
//    }
//}