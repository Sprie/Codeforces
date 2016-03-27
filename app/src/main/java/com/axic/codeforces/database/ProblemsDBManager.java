package com.axic.codeforces.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.axic.codeforces.data.ProblemsIndex;
import com.axic.codeforces.data.contestFalse;
import com.axic.codeforces.fragment.Problems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59786 on 2016/3/27.
 */
public class ProblemsDBManager {
    public ProblemsDBHelper helper;
    private SQLiteDatabase db;
    protected String dbName = "problems.db";
    int tag = 0;//判断数据库中是否有查找的值

    public ProblemsDBManager(Context context, int dbVersion) {
        helper = new ProblemsDBHelper(context, dbName, dbVersion);
        //需将实例化DBManager的步骤放在Activity onCreate中
        db = helper.getWritableDatabase();
    }


    //程序退出时需要调用close()释放数据库资源
    public void closeDB() {
        db.close();
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM problems", null);
        return c;
    }

    public List<ProblemsIndex.ResultBean.ProblemsBean> getDataFromDBById(String id) {
        ArrayList<ProblemsIndex.ResultBean.ProblemsBean> problems =
                new ArrayList<ProblemsIndex.ResultBean.ProblemsBean>();
        Cursor c = db.rawQuery("SELECT * FROM problems " +
                "WHERE id = " + id, null);
        int tag = c.getCount();
        Log.d("cCount", c.getCount() + "");
        if (tag != 0) {
            while (c.moveToNext()) {
                ProblemsIndex.ResultBean.ProblemsBean problem =
                        new ProblemsIndex.ResultBean.ProblemsBean();
                problem.setContestId(c.getString(c.getColumnIndex("id")));
                problem.setName(c.getString(c.getColumnIndex("name")));
                problem.setType(c.getString(c.getColumnIndex("type")));
                problem.setPoints(c.getString(c.getColumnIndex("points")));
                problem.setIndex(c.getString(c.getColumnIndex("proIndex")));
                problem.setStrTags(c.getString(c.getColumnIndex("tags")));


                problems.add(problem);
//            Log.d("getdata",c.getString(c.getColumnIndex("id")));
            }

            c.close();
            return problems;
        } else {
            return null;
        }
    }

    public void add(List<ProblemsIndex.ResultBean.ProblemsBean> problems) {
        db.beginTransaction();//开始事务
        try {
            for (ProblemsIndex.ResultBean.ProblemsBean problem : problems) {
                String tags;

                List<String> tag = problem.getTags();
                if (tag.size() > 0) {
                    tags = tag.get(0);
                    for (int i = 1; i < tag.size(); i++) {
                        tags = tags + "、" + tag.get(i);
                    }
                } else {
                    tags = "null";
                }
                db.execSQL("INSERT INTO problems (id,proIndex,name,type,points,tags) VALUES(?,?,?,?,?,?)",
                        new Object[]{problem.getContestId(),
                                problem.getIndex(),
                                problem.getName(),
                                problem.getType(),
                                problem.getPoints(),
                                tags});
                Log.d("index", problem.getIndex());
            }
            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }
    }

    public void addDetail(String problemsDetails, String contestId, String proIndex) {
        db.beginTransaction();//开始事务
        try {

            db.execSQL("UPDATE problems " +
                            "SET details = ? " +
                            "WHERE id = ? " +
                            "AND proIndex = ?",
                    new String[]{problemsDetails, contestId, proIndex});

            db.setTransactionSuccessful();//设置事务成功完成
        } finally {
            db.endTransaction();//完成事务
        }
    }

    public String getDetails(String contestId, String proIndex) {
        String details = null;
        db.beginTransaction();//开始事务
        try {
            Cursor c = db.rawQuery("SELECT * FROM problems " +
                    "WHERE id = ? AND proIndex = ?" , new String[]{contestId,proIndex});
//        int tag = c.getCount();

//        Log.d("cCount", c.getCount() + "");
//        if (tag != 0) {
            while (c.moveToNext()) {
                details = c.getString(c.getColumnIndex("details"));

            }
            c.close();

            db.setTransactionSuccessful();//设置事务成功完成
        } finally

        {
            db.endTransaction();//完成事务

        }
        return details;

    }
}
