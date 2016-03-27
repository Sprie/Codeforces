package com.axic.codeforces.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.axic.codeforces.data.contestFalse;
import com.axic.codeforces.database.contestDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59786 on 2016/3/27.
 */
public class ContestDBManager {

    public contestDBHelper helper;
    private SQLiteDatabase db;
    private String dbName = "contestFalse.db";

    public ContestDBManager(Context context, int dbVersion) {
        helper = new contestDBHelper(context, dbName, dbVersion);
        //需将实例化DBManager的步骤放在Activity onCreate中
        db = helper.getWritableDatabase();
    }

    public void add(List<contestFalse.ResultBean> contests) {
        db.beginTransaction();//开始事务
        try {
            for (contestFalse.ResultBean contest : contests) {
                db.execSQL("INSERT INTO contestFalse VALUES(?,?,?,?)",
                        new String[]{contest.getId(),
                                contest.getName(),
                                contest.getType(),
                                contest.getPhase()});
            }
            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }
    }

    public List<contestFalse.ResultBean> getDataFromDB() {
        ArrayList<contestFalse.ResultBean> contests = new ArrayList<contestFalse.ResultBean>();
        Cursor c = queryTheCursor();
        while(c.moveToNext()){
            contestFalse.ResultBean contest = new contestFalse.ResultBean();
            contest.setId(c.getString(c.getColumnIndex("id"))) ;
            contest.setName(c.getString(c.getColumnIndex("name")));
            contest.setType(c.getString(c.getColumnIndex("type")));
            contest.setPhase(c.getString(c.getColumnIndex("phase")));
            contests.add(contest);
//            Log.d("getdata",c.getString(c.getColumnIndex("id")));
        }
        c.close();
        return contests;

    }
    //更新Phase的值
    public void updatePhase(String id,String phase){
        db.beginTransaction();//开始事务
        try {
//            for (contestFalse.ResultBean contest : contests) {
                db.execSQL("UPDATE contestFalse "+
                        "SET phase = ? "+
                        "where id  = ?",
                        new String[]{phase,id});
//            }
            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }

    }
    //根据id搜索phase值
    public String getPhaseById(String id){
        Cursor cursor;
        db.beginTransaction();//开始事务
        try {

            cursor  = db.rawQuery("SELECT phase FROM contestFalse "+
//                                "SET phase = ? "+
                                "where id  = ?",
                        new String[]{id});

            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }
        cursor.moveToNext();
        return cursor.getString(0);
    }

    public Cursor queryTheCursor(){
        Cursor c = db.rawQuery("SELECT *FROM contestFalse",null);
        return c;
    }

    //程序退出时需要调用close()释放数据库资源
    public void closeDB() {
        db.close();
    }

}
