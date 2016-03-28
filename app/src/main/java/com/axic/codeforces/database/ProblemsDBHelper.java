package com.axic.codeforces.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 59786 on 2016/3/27.
 */
public class ProblemsDBHelper extends SQLiteOpenHelper {

    public boolean dbStatus;


    public ProblemsDBHelper(Context context,String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    //数据库第一次被创建的时候调用
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS problems (" +
                "id VARCHAR ," +
                "proIndex VARCHAR,name VARCHAR," +
                "type VARCHAR,points VARCHAR," +
                "tags VARCHAR,"+
                "details TEXT," +
                "PRIMARY KEY(id,proIndex)," +
                "FOREIGN KEY(id) REFERENCES contestFalse(id)" +
                ")");
        Log.d("db", "数据库创建完成");
        dbStatus = true;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE contestFalse ADD COLUMN other STRING");
    }
}
